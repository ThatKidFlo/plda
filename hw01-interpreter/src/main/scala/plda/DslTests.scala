package plda

import plda.ast.expressionDSL._
import plda.ast.{let => letClass, _}
import plda.interpreter.Interpreter.interpret

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 20/11/2017
  */
object DslTests {
  def main(args: Array[String]): Unit = {
    println {
      interpret {
        let("x" -> const(42), "y" -> const(10)) in {
          op(eval("x"), Add, eval("y"))
        }
      }
    }

    println {
      interpret {
        iff(1) {
          42
        } otherwise {
          0
        }
      }
    }

    println {
      interpret {
        let("fact" -> λ(List("n"), {
          iff(op(eval("n"), Eq, const(0))) {
            const(1)
          } otherwise {
            op(eval("n"), Mul, apply(eval("fact"), Map("n" -> op(eval("n"), Sub, const(1)))))
          }
        }
        )) in {
          apply(eval("fact"), Map("n" -> const(20)))
        }
      }
    }
  }
}
