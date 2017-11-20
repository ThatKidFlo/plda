package plda.interpreter.api

import plda.ast.{const, λ}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 20/11/2017
  */
sealed trait EvaluationResult

case class Constant(n: const) extends EvaluationResult

case class Lambda(fn: λ) extends EvaluationResult
