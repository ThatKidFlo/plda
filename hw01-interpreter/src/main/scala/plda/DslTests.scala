package plda

import plda.dsl.expressionDecorators._
import plda.interpreter.Interpreter.interpret
import plda.types.Number

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 20/11/2017
  */
object DslTests {
  def main(args: Array[String]): Unit = {
    println {
      interpret {
        let("x" -> 42, "y" -> 10) in {
          eval("x") + eval("y")
        }
      }
    }

    println {
      interpret {
        iff(1) {
          42 + 45
        } otherwise {
          0
        }
      }
    }

    println {
      interpret {
        let("fact" -> (λ("n" ofType Number()) in {
          iff(eval("n") === 0) {
            1
          } otherwise {
            eval("n") * eval("fact").apply("n" -> (eval("n") - 1))
          }
        })) in {
          eval("fact").apply("n" -> 5)
        }
      }
    }

    println {
      interpret {
        let("n" -> 42) in {
          eval("n") apply ("n" -> 42)
        }
      }
    }
  }
}
