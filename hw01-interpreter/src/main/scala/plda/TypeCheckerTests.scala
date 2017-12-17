package plda

import plda.TestExpressions._
import plda.typechecker.TypeChecker.checkTypes

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
object TypeCheckerTests {
  def main(args: Array[String]): Unit = {
//    println {
//      s"RESULT:: ${checkTypes(fnEq2)}"
//    }
//
//    println {
//      s"RESULT:: ${checkTypes(if1Eq1Then1)}"
//    }
//
//    println {
//      s"RESULT:: ${checkTypes(if1Eq1Then1)}"
//    }
//
//    println {
//      s"RESULT:: ${checkTypes(if1Eq2Then2Else42)}"
//    }
//
//    println {
//      s"RESULT:: ${checkTypes(letNEq42InEvalNMinus1)}"
//    }

    println {
      s"RESULT:: ${checkTypes(factorial)}"
    }

    println {
      s"RESULT:: ${checkTypes(factorialDefinition("fact"))}"
    }
  }
}
