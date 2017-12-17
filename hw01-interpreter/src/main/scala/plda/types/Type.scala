package plda.types

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
sealed trait Type

case class Number() extends Type {
  override def toString: String = s"Num"
}

case class Function(inTypeVar: Map[String, Type], outTypeVar: Type) extends Type {
  override def toString: String = s"(${inTypeVar.values.mkString(" -> ")} -> $outTypeVar)"
}

case class Any() extends Type {
  override def toString: String = "AnyType"
}

case class TypeError(explanation: String) extends Type
