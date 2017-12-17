package plda

import plda.ast._
import plda.lang.TypeOf
import plda.types.Number

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
object TestExpressions {
  def fnEq2: Expression = op(λ(List(), const(42)), Eq, const(42))

  def if1Eq1Then1: Expression = `if`(op(const(1), Eq, const(1)), const(1), const(2))

  def if1Eq1Then42: Expression = `if`(op(const(1), Eq, const(1)),
    apply(λ(List(TypeOf("x", Number())), eval("x")), Map("x" -> const(42)))
    , const(2))

  def if1Eq2Then2Else42: Expression = `if`(op(const(2), Eq, const(1)),
    const(2),
    apply(λ(List(TypeOf("x", Number())), eval("x")), Map("x" -> const(42))))

  def letNEq42InEvalNMinus1: Expression = let(Map("n" -> const(42)), op(eval("n"), Sub, const(1)))

  /**
    * fact n = if n == 0 then 1 else n * fact(n - 1)
    */
  def factorial: Expression = let(
    factorialDefinition, apply(eval("fact"), Map("n" -> const(20)))
  )

  def factorialDefinition: Map[String, Expression] = Map("fact" -> λ(List(TypeOf("n", Number())), {
    val n = eval("n")
    `if`(op(n, Eq, const(0)),
      const(1),
      op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
  }))
}
