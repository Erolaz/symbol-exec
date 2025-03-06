package software_testing_hw4.util

import software_testing_hw4.execution.ProgramState
import java.io.File

object OutputFormatter {
    fun writeFormattedResults(outputFile: File, states: List<ProgramState>) {
        outputFile.printWriter().use { writer ->
            states.forEach { state ->
                writer.println(formatState(state))
                writer.println()
            }
        }
    }

    private fun formatState(state: ProgramState): String {
        val memoryContents = state.memory.entries.joinToString("\n") { (k, v) -> "$k = $v" }
        val pathCondition = state.pathCondition.joinToString(" && ")

        return """
{
$memoryContents
pc: $pathCondition
result: ${state.result}
}
        """.trimIndent()
    }
}
