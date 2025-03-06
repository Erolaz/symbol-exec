package software_testing_hw4.evaluation

import ast.*

object Evaluator {

     enum class Precedence(val value: Int) {
        LOWEST(0),
        LOGICAL_OR(1),
        LOGICAL_AND(2),
        COMPARISON(3),
        ADD_SUBTRACT(4),
        UNARY(5),
    }

    fun evalExpression(
        memory: Map<String, String>,
        expr: Expression,
        parentPrecedence: Precedence = Precedence.LOWEST
    ): String {
        return when (expr) {
            is IntConstant -> expr.value.toString()
            is BoolConstant -> expr.value.toString()
            is VarRef -> memory[expr.identifier]
                ?: error("Variable '${expr.identifier}' not initialized")

            is BinOp -> {
                val (opStr, precedence) = when (expr.kind) {
                    BinOpKind.BO_Add -> "+" to Precedence.ADD_SUBTRACT
                    BinOpKind.BO_Sub -> "-" to Precedence.ADD_SUBTRACT
                    BinOpKind.BO_Lt -> "<" to Precedence.COMPARISON
                    BinOpKind.BO_Gt -> ">" to Precedence.COMPARISON
                    BinOpKind.BO_LAnd -> "&" to Precedence.LOGICAL_AND
                    BinOpKind.BO_LOr -> "|" to Precedence.LOGICAL_OR
                }

                val left = evalExpression(memory, expr.lhs, precedence)
                val right = evalExpression(memory, expr.rhs, precedence)

                val combined = "$left $opStr $right"
                if (precedence.value < parentPrecedence.value) "($combined)" else combined
            }

            is UnOp -> {
                val opStr = when (expr.kind) {
                    UnOpKind.UO_Neg -> "!"
                }
                val inner = evalExpression(memory, expr.subExpr, Precedence.UNARY)
                val combined = "$opStr$inner"
                if (Precedence.UNARY.value < parentPrecedence.value) "($combined)" else combined
            }

            else -> error("Unsupported expression type: ${expr::class}")
        }
    }
}
