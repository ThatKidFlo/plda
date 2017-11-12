package plda.ast

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
sealed trait Expression

case class const(value: Int) extends Expression {
  override def toString: String = value.toString
}

case class eval(symbol: String) extends Expression {
  override def toString: String = s"$$$symbol"
}

case class op(lhs: Expression, binaryOperator: BinaryOperator, rhs: Expression) extends Expression {
  override def toString: String = s"$lhs $binaryOperator $rhs"
}

case class `if`(condition: Expression, trueBranch: Expression, falseBranch: Expression) extends Expression {
  override def toString: String = s"if($condition) { $trueBranch } else { $falseBranch }"
}

case class let(bindings: Map[String, Expression], body: Expression) extends Expression

case class λ(params: List[String], body: Expression) extends Expression {
  override def toString: String = {
    s"""λ(${params.mkString(",")}) => { $body }""".stripMargin
  }
}

case class apply(fn: Expression, parameters: Map[String, Expression]) extends Expression

case class undef(message: String) extends Expression
