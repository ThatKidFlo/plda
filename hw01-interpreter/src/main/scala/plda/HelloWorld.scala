package plda

import plda.ast._
import plda.interpreter.Interpreter._

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println {
      interpret(`if`(op(const(1), Eq, const(1)), const(1), const(2)))
    }

    println {
      interpret {
        `if`(op(const(1), Eq, const(1)),
          apply(λ(List("x"), eval("x")), Map("x" -> const(42)))
          , const(2))
      }
    }

    println {
      interpret(`if`(op(const(2), Eq, const(1)),
        const(2),
        apply(λ(List("x"), eval("x")), Map("x" -> const(42)))))
    }

    println {
      interpret {
        let(Map("n" -> const(42)), op(eval("n"), Sub, const(1)))
      }
    }

    /**
      * fact(n) = if n == 0 then 1 else n * fact(n - 1)
      */
    println {
      interpret {
        let(
          Map("fact" -> λ(List("n"), {
            val n = eval("n")
            `if`(op(n, Eq, const(0)),
              const(1),
              op(n, Mul, apply(eval("fact"), Map("n" -> op(n, Sub, const(1))))))
          })), apply(eval("fact"), Map("n" -> const(30)))
        )
      }
    }
  }
}
