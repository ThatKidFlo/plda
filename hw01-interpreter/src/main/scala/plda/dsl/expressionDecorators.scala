package plda.dsl

import plda.ast._

import scala.language.implicitConversions

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 20/11/2017
  */
object expressionDecorators {

  case class ifIntermediary(condition: Expression) {
    def apply(trueBranch: Expression): elseIntermediary = {
      elseIntermediary(condition, trueBranch)
    }
  }

  case class elseIntermediary(condition: Expression, trueBranch: Expression) {
    def otherwise(falseBranch: Expression): Expression = {
      `if`(condition, trueBranch, falseBranch)
    }
  }

  case class letIntermediary(bindings: (String, Expression)*) {
    def in(body: Expression): Expression = {
      plda.ast.let(bindings.toMap, body)
    }
  }

  case class lambdaIntermediary(freeVariables: String*) {
    def in(body: Expression): Expression = plda.ast.λ(freeVariables.toList, body)
  }

  implicit class RichExpression(currentExpression: Expression) {
    def +(other: Expression): Expression = op(currentExpression, Add, other)

    def -(other: Expression): Expression = op(currentExpression, Sub, other)

    def *(other: Expression): Expression = op(currentExpression, Mul, other)

    def /(other: Expression): Expression = op(currentExpression, Div, other)

    def ===(other: Expression): Expression = op(currentExpression, Eq, other)

    def apply(params: (String, Expression)*): Expression = plda.ast.apply(currentExpression, params.toMap)
  }

  implicit def intToConst(int: Int): const = const(int)

  def let(bindings: (String, Expression)*): letIntermediary = letIntermediary(bindings: _*)

  def iff(condition: Expression): ifIntermediary = ifIntermediary(condition)

  def λ(freeVariables: String*): lambdaIntermediary = lambdaIntermediary(freeVariables: _*)

  def eval(variable: String): Expression = plda.ast.eval(variable)
}
