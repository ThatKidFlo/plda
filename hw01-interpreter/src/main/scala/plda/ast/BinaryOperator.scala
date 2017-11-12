package plda.ast

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
sealed trait BinaryOperator {
  def apply(lhs: Int, rhs: Int): const
}

case object Add extends BinaryOperator {
  override def apply(lhs: Int, rhs: Int): const = const(lhs + rhs)

  override def toString: String = "+"
}

case object Sub extends BinaryOperator {
  override def apply(lhs: Int, rhs: Int): const = const(lhs - rhs)

  override def toString: String = "-"
}

case object Mul extends BinaryOperator {
  override def apply(lhs: Int, rhs: Int): const = const(lhs * rhs)

  override def toString: String = "*"
}

case object Div extends BinaryOperator {
  override def apply(lhs: Int, rhs: Int): const = const(lhs / rhs)

  override def toString: String = "/"
}

case object Eq extends BinaryOperator {
  override def apply(lhs: Int, rhs: Int): const = const(if(lhs == rhs) 1 else 0)

  override def toString: String = "="
}