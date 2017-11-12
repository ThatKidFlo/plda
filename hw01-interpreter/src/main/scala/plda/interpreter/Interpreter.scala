package plda.interpreter

import plda.ast._
import plda.interpreter.exception.EvaluationException

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.{Failure, Success, Try}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object Interpreter {
  def interpret(program: Expression): Try[Either[const, λ]] = interpretInternal(program, Map())

  private def interpretInternal(program: Expression, environment: Map[String, Expression]): Try[Either[const, λ]] =
    program match {
      case self@`const`(_) =>
        foundConst(self)

      case eval(name) =>
        val evaluatedSymbol = environment.get(name)
        println(s"Searched for $name in $environment, found $evaluatedSymbol")
        evaluatedSymbol
          .map(evaluated => interpretInternal(evaluated, environment))
          .getOrElse(failed(s"symbol $name was not found in $environment"))

      case op(lhs, binaryOperator, rhs) =>
        for {
          evaluatedLhs <- interpretInternal(lhs, environment) if evaluatedLhs.isInstanceOf[Left[const, λ]]
          evaluatedRhs <- interpretInternal(rhs, environment)
        } yield {
          evaluatedLhs -> evaluatedRhs match {
            case (Left(x1), Left(x2)) => Left(binaryOperator.apply(x1.value, x2.value))
            case _ => throw new EvaluationException(
              s"""Unable to compare non-numeric values::
                 | left-hand side: $evaluatedLhs
                 | right-hand side: $evaluatedRhs
            """.stripMargin)
          }
        }

      case `if`(condition, trueBranch, falseBranch) =>
        interpretInternal(condition, environment) match {
          case Success(Left(const(0))) => interpretInternal(falseBranch, environment)
          case _ => interpretInternal(trueBranch, environment)
        }

      case let(bindings, body) =>
        interpretInternal(body, environment ++ bindings)

      case λ(params, body) =>
        foundLambda(λ(params, body))

      case apply(lambda, parameters) =>
        interpretInternal(lambda, environment).flatMap {
          case Right(λ(_, body)) =>
            interpretInternal(body, environment ++ evaluateParameters(parameters, environment))
          case Left(expr) =>
            println(s"gonna throw $expr, because $environment")
            throw new EvaluationException(s"Unable to apply non-function value $expr to $parameters")
        }

      case undef(msg) => throw new EvaluationException(msg)
    }

  private def evaluateParameters(parameters: Map[String, Expression],
                                 environment: Map[String, Expression]): Map[String, Expression] = {
    parameters
      .map {
        case (name, expr) => name -> interpretInternal(expr, environment)
      }
      .foldRight(mutable.Map[String, Expression]()) {
        case ((name, Success(Left(constant))), env) => env + (name -> constant)
        case ((name, Success(Right(function))), env) => env + (name -> function)

          //TODO:: could be more permissive here, in case the formal parameter is not actually used
        case ((name, Failure(err)), _) =>
          throw new EvaluationException(s"Failed to evaluate parameter $name due to $err")
      }.toMap
  }

  private def foundConst(const: const) = Success(Left(const))

  private def foundLambda(lambda: λ) = Success(Right(lambda))

  private def failed(message: String) = Failure(new EvaluationException(s"Program crashed:: $message"))
}
