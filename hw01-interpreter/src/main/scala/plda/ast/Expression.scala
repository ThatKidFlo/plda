package plda.ast

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
sealed trait Expression

case class const(value: Int) extends Expression

case class eval(symbol: String, environment: Map[String, Expression]) extends Expression

case class op(lhs: Expression, binaryOperator: BinaryOperator, rhs: Expression) extends Expression

case class `if`(condition: Expression, trueBranch: Expression, falseBranch: Expression) extends Expression

case class let(bindings: Map[String, Expression], body: Expression) extends Expression

case class λ(params: List[String], body: Expression) extends Expression

case class apply(fn: Expression, parameters: Expression*) extends Expression
