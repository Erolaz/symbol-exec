package software_testing_hw4

import java.nio.file.Paths

import software_testing_hw4.util.FileProcessor
import software_testing_hw4.util.OutputFormatter
import software_testing_hw4.execution.Executor
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    if (args.size < 2) {
        System.err.println("2 arguments required: inputFile outputFile")
        exitProcess(1)
    }

    val (inputPath, outputPath) = args.map { Paths.get(it).toFile() }

    if (!inputPath.exists()) {
        System.err.println("Input file not found: ${inputPath.path}")
        exitProcess(1)
    }

    val function = FileProcessor.parseFunction(inputPath)
    val states = Executor.executeFunction(function)

    OutputFormatter.writeFormattedResults(outputPath, states)
}