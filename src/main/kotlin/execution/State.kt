package software_testing_hw4.execution

import ast.Statement

typealias Memory = MutableMap<String, String>
typealias PathCondition = MutableList<String>

data class ProgramState(
    val memory: Memory,
    val pathCondition: PathCondition,
    val stack: MutableList<Statement>,
    var result: String? = null
)
