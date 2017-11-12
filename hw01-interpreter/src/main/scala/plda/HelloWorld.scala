package plda

import plda.ast._
import plda.interpreter.Interpreter

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
//    println {
//      Interpreter.interpret(`if`(op(const(1), Eq, const(1)), const(1), const(2)))
//    }
//
//    println {
//      Interpreter.interpret {
//        `if`(op(const(1), Eq, const(1)),
//          apply(λ(List("x"), eval("x")), Map("x" -> const(42)))
//          , const(2))
//      }
//    }
//
//    println {
//      Interpreter.interpret(`if`(op(const(2), Eq, const(1)),
//        const(2),
//        apply(λ(List("x"), eval("x")), Map("x" -> const(42)))))
//    }

    /**
      * fact(n) = if n == 0 then 1 else n * fact(n - 1)
      */
    println {
      Interpreter.interpret {
        let(
          Map("fact" -> λ(List("n"), {
            `if`(op(eval("n"), Eq, const(0)), const(1),
              op(eval("n"), Mul, apply(eval("fact"), Map("n" -> op(eval("n"), Sub, const(1))))))
          })), apply(eval("fact"), Map("n" -> const(5)))
        )
      }
    }
  }
}
