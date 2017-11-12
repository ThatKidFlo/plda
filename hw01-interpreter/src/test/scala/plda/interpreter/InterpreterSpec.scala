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

  it should "correctly apply a binary operator to two constants" in {
    val triedInterpretedMultiplication = interpret {
      let(Map("x" -> const(21), "y" -> const(2)), {
        op(eval("x"), Mul, eval("y"))
      })
    }

    expectConst(42, triedInterpretedMultiplication)
  }

  it should "evaluate the true branch for a true condition in an if statement" in {
    val triedInterpretedIf = interpret {
      `if`(op(const(0), Eq, const(0)), const(42), const(1))
    }

    expectConst(42, triedInterpretedIf)
  }

  it should "evaluate the false branch for a false condition in an if statement" in {
    val triedInterpretedIf = interpret {
      `if`(op(const(42), Eq, const(0)), const(0), const(42))
    }

    expectConst(42, triedInterpretedIf)
  }

  it should "correctly define factorial" in {
    val triedInterpretedFact = interpret {
      λ(List("n"), {
        val n = eval("n")
        `if`(op(n, Eq, const(0)),
          const(1),
          op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
      })
    }

    expectLambda {
      triedInterpretedFact
    }

    val factorial = triedInterpretedFact.get.right.get

    interpret {
      apply(factorial, Map("n" -> const(10), "fact" -> factorial))
    } should be {
      Success(Left(const(3628800)))
    }
  }

  it should "bind a function to a symbol, and correctly evaluate it" in {
    val evaluatedFactorial = interpret {
      let(
        Map("fact" -> λ(List("n"), {
          val n = eval("n")
          `if`(op(n, Eq, const(0)),
            const(1),
            op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
        })), apply(eval("fact"), Map("n" -> const(5)))
      )
    }

    expectConst(120, evaluatedFactorial)
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
