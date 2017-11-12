package plda.interpreter

import org.scalatest.{FlatSpec, Matchers}
import plda.ast._
import plda.interpreter.Interpreter._

import scala.util.{Success, Try}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 12/11/2017
  */
class InterpreterSpec extends FlatSpec with Matchers {

  "The interpreter" should "correctly evaluate constants" in {
    expectConst(42, interpret {
      const(42)
    })
  }

  it should "evaluate an enhanced existing environment with new bindings from a let expression" in {
    expectConst(42, interpret {
      let(Map("x" -> const(42)),
        eval("x"))
    })
  }

  it should "correctly define factorial" in {
    expectLambda {
      interpret {
        λ(List("n"), {
          val n = eval("n")
          `if`(op(n, Eq, const(0)),
            const(1),
            op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
        })
      }
    }
  }

  private def expectConst(value: Int, interpretationResult: Try[Either[const, λ]]) = {
    assert(interpretationResult.isInstanceOf[Success[_]])
    assert(interpretationResult.get.isInstanceOf[Left[_, _]])
    assert(interpretationResult.get.left.get.value == value)
  }

  private def expectLambda(interpretationResult: Try[Either[const, λ]]): λ = {
    assert(interpretationResult.isInstanceOf[Success[_]])
    assert(interpretationResult.get.isInstanceOf[Right[_, _]])
    interpretationResult.get.right.get
  }
}
