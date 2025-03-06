package software_testing_hw4.execution

import ast.*
import software_testing_hw4.evaluation.Evaluator.evalExpression

object Executor {
    fun executeInner(initialState: ProgramState): List<ProgramState> {
        val completedStates = mutableListOf<ProgramState>()
        val queue = ArrayDeque(listOf(initialState))

        while (queue.isNotEmpty()) {
            val state = queue.removeFirst()

            if (state.stack.isEmpty()) {
                completedStates += state
                continue
            }

            when (val stmt = state.stack.removeFirst()) {
                is Assignment -> {
                    val value = evalExpression(state.memory, stmt.value)
                    state.memory[stmt.name] = value
                    queue += state
                }
                is IfStmt -> {
                    val condition = evalExpression(state.memory, stmt.condition)

                    queue += state.branch(condition, stmt.thenBlock)
                    queue += state.branch("!($condition)", stmt.elseBlock)
                }
                is ReturnStmt -> {
                    state.result = evalExpression(state.memory, stmt.returnExpr)
                    queue += state
                }
                else -> error("Unsupported statement type: ${stmt::class.simpleName}")
            }
        }

        return completedStates
    }

    fun executeFunction(function: ast.Function): List<ProgramState> {
        val initialMemory = function.parameters.associate { it.name to "'${it.name}'" }.toMutableMap()
        val program = function.body + ReturnStmt(function.returnValue!!)
        val initialState = ProgramState(initialMemory, mutableListOf(), program.toMutableList())

        return executeInner(initialState)
    }

    private fun ProgramState.branch(
        condition: String,
        branchStmts: List<Statement>
    ) = copy(
        memory = memory.toMutableMap(),
        pathCondition = (pathCondition + condition).toMutableList(),
        stack = (branchStmts + stack).toMutableList(),
        result = result
    )
}
