package plda

import plda.interpreter.Interpreter._
import plda.TestExpressions._

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object InterpreterTests {
  def main(args: Array[String]): Unit = {
    println {
      interpret {
        fnEq2
      }
    }

    println {
      interpret(if1Eq1Then1)
    }

    println {
      interpret {
        if1Eq1Then42
      }
    }

    println {
      if1Eq2Then2Else42
    }

    println {
      interpret {
        letNEq42InEvalNMinus1
      }
    }

    println {
      interpret {
        factorial
      }
    }
  }
}
