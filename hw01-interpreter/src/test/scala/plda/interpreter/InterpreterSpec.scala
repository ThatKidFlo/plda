//package plda.interpreter
//
//import org.scalatest.{FlatSpec, Matchers}
//import plda.ast._
//import plda.interpreter.Interpreter._
//import plda.interpreter.api.{Constant, EvaluationResult, Lambda}
//import plda.interpreter.exception.EvaluationException
//
//import scala.util.{Success, Try}
//
///**
//  *
//  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
//  * @since 12/11/2017
//  */
//class InterpreterSpec extends FlatSpec with Matchers {
//
//  "The interpreter" should "correctly evaluate constants" in {
//    expectConst(42, interpret {
//      const(42)
//    })
//  }
//
//  it should "evaluate an enhanced existing environment with new bindings from a let expression" in {
//    expectConst(42, interpret {
//      let(Map("x" -> const(42)),
//        eval("x"))
//    })
//  }
//
//  it should "correctly apply a binary operator to two constants" in {
//    val triedInterpretedMultiplication = interpret {
//      let(Map("x" -> const(21), "y" -> const(2)), {
//        op(eval("x"), Mul, eval("y"))
//      })
//    }
//
//    expectConst(42, triedInterpretedMultiplication)
//  }
//
//  it should "fail to compare any non-constant" in {
//    val failedComparison = interpret {
//      op(λ(List(), const(42)), Eq, const(42))
//    }
//
//    assertThrows[EvaluationException] {
//      failedComparison.get
//    }
//  }
//
//  it should "evaluate the true branch for a true condition in an if statement" in {
//    val triedInterpretedIf = interpret {
//      `if`(op(const(0), Eq, const(0)), const(42), const(1))
//    }
//
//    expectConst(42, triedInterpretedIf)
//  }
//
//  it should "evaluate the false branch for a false condition in an if statement" in {
//    val triedInterpretedIf = interpret {
//      `if`(op(const(42), Eq, const(0)), const(0), const(42))
//    }
//
//    expectConst(42, triedInterpretedIf)
//  }
//
//  it should "correctly define factorial" in {
//    val triedInterpretedFact = interpret {
//      λ(List("n"), {
//        val n = eval("n")
//        `if`(op(n, Eq, const(0)),
//          const(1),
//          op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
//      })
//    }
//
//    val factorial = expectLambda {
//      triedInterpretedFact
//    }
//
//    interpret {
//      apply(factorial, Map("n" -> const(10), "fact" -> factorial))
//    } should be {
//      Success(Constant(const(3628800)))
//    }
//  }
//
//  it should "fail to apply any non-function value" in {
//    val failedApplication = interpret {
//      apply(const(42), Map())
//    }
//
//    assertThrows[EvaluationException] {
//      failedApplication.get
//    }
//  }
//
//  it should "bind a function to a symbol, and correctly evaluate it" in {
//    val evaluatedFactorial = interpret {
//      let(
//        Map("fact" -> λ(List("n"), {
//          val n = eval("n")
//          `if`(op(n, Eq, const(0)),
//            const(1),
//            op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
//        })), apply(eval("fact"), Map("n" -> const(5)))
//      )
//    }
//
//    expectConst(120, evaluatedFactorial)
//  }
//
//  private def expectConst(value: Int, interpretationResult: Try[EvaluationResult]) = {
//    assert(interpretationResult.isInstanceOf[Success[_]])
//    assert(interpretationResult.get.isInstanceOf[Constant])
//    assert(interpretationResult.get.asInstanceOf[Constant].n.value == value)
//  }
//
//  private def expectLambda(interpretationResult: Try[EvaluationResult]): λ = {
//    assert(interpretationResult.isInstanceOf[Success[_]])
//    assert(interpretationResult.get.isInstanceOf[Lambda])
//    interpretationResult.get.asInstanceOf[Lambda].fn
//  }
//}
