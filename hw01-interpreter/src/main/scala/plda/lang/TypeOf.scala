package plda.lang

import plda.types.Type

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
case class TypeOf(symbol: String, typeOf: Type) {
  override def toString: String = s"$symbol: ${typeOf.getClass.getSimpleName}"
}

