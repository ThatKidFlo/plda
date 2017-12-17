package plda.typechecker

import plda.ast.{λ, _}
import plda.types.{Any, Function, Number, Type, TypeError}

/**
  *
  * @author Florin-Gabriel Barbuceanu, florin.barbuceanu@sap.com
  * @since 17/12/2017
  */
object TypeChecker {
  private val EMPTY_TYPE_UNIVERSE: Map[String, Type] = Map()

  def checkTypes(expr: Expression): Type = checkTypesInternal(EMPTY_TYPE_UNIVERSE)(expr)

  private def checkTypesInternal(typeUniverse: Map[String, Type])(expr: Expression): Type = {
    def checkTypesInCurrentUniverse: Expression => Type = checkTypesInternal(typeUniverse)

    expr match {
      case const(_) => Number()

      case eval(symbol) =>
        typeUniverse.getOrElse(symbol, TypeError(s"Undefined variable found $symbol"))

      case op(lhsExpr, _, rhsExpr) =>
        val lhsType = checkTypesInCurrentUniverse(lhsExpr)
        val rhsType = checkTypesInCurrentUniverse(rhsExpr)
        if (isNumericType(lhsType) && isNumericType(rhsType)) Number()
        else TypeError(s"Cannot apply a numeric binary operation to non-numeric types: $lhsType, $rhsType")

      case `if`(_, trueBranch, falseBranch) =>
        val trueBranchType = checkTypesInCurrentUniverse(trueBranch)
        val falseBranchType = checkTypesInCurrentUniverse(falseBranch)
        if (trueBranchType.getClass != Any.getClass) trueBranchType
        else if (falseBranchType.getClass != Any.getClass) falseBranchType
        else TypeError(s"if/else branches cannot return distinct types: $trueBranchType, $falseBranchType")

      case let(bindings, body) =>
        val typeUniverseDelta = bindings.map {
          case (symbol, expression) =>
            symbol -> checkTypesInCurrentUniverse(expression)
        }
        checkTypesInternal(typeUniverse ++ typeUniverseDelta)(body)

      case λ(params, body) =>
        val paramTypes = params
          .map(typeAnnotation => typeAnnotation.symbol -> typeAnnotation.typeOf)
          .toMap
        Function(paramTypes, checkTypesInternal(typeUniverse ++ paramTypes)(body))

      case apply(fn, parameters) =>
        val typeUniverseDelta = parameters.map {
          case (symbol, expression) =>
            symbol -> checkTypesInCurrentUniverse(expression)
        }
        val functionType = checkTypesInternal(typeUniverse ++ typeUniverseDelta)(fn)
        if (!functionType.isInstanceOf[Function]) {
          TypeError(s"Non-function type cannot be applied: $functionType")
        } else {
          checkTypesInternal(typeUniverse ++ typeUniverseDelta) {
            fn match {
              case func: λ => func.body
              case _ => fn
            }
          }
        }
    }
  }

  private def isNumericType(typeDef: Type): Boolean = typeDef.isInstanceOf[Number] ||
    (typeDef.isInstanceOf[Function] && typeDef.asInstanceOf[Function].outTypeVar.getClass == Number.getClass)
}
