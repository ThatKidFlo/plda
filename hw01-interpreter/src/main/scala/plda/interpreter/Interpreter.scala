package plda.interpreter

import plda.ast._
import plda.config.Constants.Logging._
import plda.config.Constants.{Interpreter => InterpreterConfig}
import plda.interpreter.api.{Constant, EvaluationResult, Lambda}
import plda.interpreter.exception.EvaluationException

import scala.util.{Failure, Success, Try}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object Interpreter {
  def interpret(program: Expression): Try[EvaluationResult] = interpretInternal(program, Map())

  private def interpretInternal(program: Expression, environment: Map[String, Expression]): Try[EvaluationResult] =
    program match {
      case self@`const`(_) =>
        foundConst(self)

      case eval(name) =>
        val evaluatedSymbol = environment.get(name)
        debug(s"Evaluating symbol $name in environment ${prettify(environment)}, found $evaluatedSymbol")
        evaluatedSymbol
          .map(evaluated => interpretInternal(evaluated, environment))
          .getOrElse(failed(s"${ERROR_LEVEL}symbol $name was not found in ${prettify(environment)}"))

      case op(lhs, binaryOperator, rhs) =>
        val triedOpEvaluation: Try[EvaluationResult] = for {
          evaluatedLhs <- interpretInternal(lhs, environment) if evaluatedLhs.isInstanceOf[Constant]
          evaluatedRhs <- interpretInternal(rhs, environment)
        } yield {
          evaluatedLhs -> evaluatedRhs match {
            case (Constant(x1), Constant(x2)) => Constant(binaryOperator.apply(x1.value, x2.value))
            case _ => throw new EvaluationException(
              s"${ERROR_LEVEL}Unable to compare non-numeric values:: left-hand side: <$evaluatedLhs>, " +
                s"right-hand side: <$evaluatedRhs>")
          }
        }

        triedOpEvaluation
          .transform(
            Success(_),
            _ => Failure {
              new EvaluationException(s"${ERROR_LEVEL}Unable to compare non-numeric values:: " +
                s"left-hand side: <$lhs>, right-hand side: <$rhs>")
            })

      case `if`(condition, trueBranch, falseBranch) =>
        interpretInternal(condition, environment) match {
          case Success(Constant(const(0))) =>
            debug(s"Condition $condition evaluated to false, will evaluate the false branch")
            interpretInternal(falseBranch, environment)
          case _ =>
            debug(s"Condition $condition evaluated to true, will evaluate the true branch")
            interpretInternal(trueBranch, environment)
        }

      case let(bindings, body) =>
        interpretInternal(body, environment ++ bindings)

      case λ(params, body) =>
        foundLambda(λ(params, body))

      case apply(lambda, parameters) =>
        interpretInternal(lambda, environment).flatMap {
          case Lambda(λ(_, body)) =>
            interpretInternal(body, environment ++ evaluateParameters(parameters, environment))
          case Constant(expr) =>
            debug(s"Interpretation will terminated, because non-function value $expr " +
              s"cannot be applied to ${prettify(parameters)}")
            throw new EvaluationException(s"Unable to apply non-function value $expr to ${prettify(parameters)}")
        }
    }

  private def evaluateParameters(parameters: Map[String, Expression],
                                 environment: Map[String, Expression]): Map[String, Expression] = {
    //TODO:: maybe it would be nice to be lazy instead, and not evaluate parameters unless actually required
    parameters
      .map {
        case (name, expr) => name -> interpretInternal(expr, environment)
      }
      .foldRight(Map[String, Expression]()) {
        case ((name, Success(Constant(constant))), env) => env + (name -> constant)
        case ((name, Success(Lambda(function))), env) => env + (name -> function)

        //TODO:: could be more permissive here, in case the formal parameter is not actually used
        case ((name, Failure(err)), _) =>
          throw new EvaluationException(s"Failed to evaluate parameter $name due to $err")
      }
  }

  private def foundConst(const: const) = Success(Constant(const))

  private def foundLambda(lambda: λ) = Success(Lambda(lambda))

  private def failed(message: String) = Failure(new EvaluationException(s"Program crashed:: $message"))

  private def debug(msg: => String): Unit = {
    if (InterpreterConfig.DEBUG) println {
      s"$DEBUG_LEVEL$msg"
    }
  }

  private def prettify(environment: Map[String, Expression]): String = environment.mkString("[", ", ", "]")
}
