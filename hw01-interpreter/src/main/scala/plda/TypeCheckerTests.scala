package plda

import plda.TestExpressions._
import plda.dsl.expressionDecorators._
import plda.typechecker.TypeChecker.checkTypes
import plda.types.{Function, Number}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
object TypeCheckerTests {
  def main(args: Array[String]): Unit = {
    println {
      checkTypes(fnEq2)
    }

    println {
      checkTypes(if1Eq1Then1)
    }

    println {
      checkTypes(if1Eq1Then1)
    }

    println {
      checkTypes(if1Eq2Then2Else42)
    }

    println {
      checkTypes(letNEq42InEvalNMinus1)
    }

    println {
      checkTypes(factorial)
    }

    println {
      checkTypes(factorialDefinition("fact"))
    }

    println {
      checkTypes {
        λ("x" ofType Number(), "y" ofType Number()) in {
          eval("x") + eval("y")
        } apply("x" -> 5, "y" -> 10)
      }
    }

    println {
      checkTypes {
        λ("x" ofType Number(), "y" ofType Number(), "z" ofType Function(Map("n" -> Number()), Number())) in {
          eval("x") + eval("y")
        } apply("x" -> 5, "y" -> 6, "z" -> (λ() in {eval("")}))
      }
    }
  }
}
