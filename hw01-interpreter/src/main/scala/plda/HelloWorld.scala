package plda

import plda.ast._
import plda.ast._

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 08/11/2017
  */
object HelloWorld {
  def main(args: Array[String]): Unit = {
    println {
      "hello world"
      `if`(op(const(1), Eq, const(1)), const(1), const(2))
    }
  }
}
