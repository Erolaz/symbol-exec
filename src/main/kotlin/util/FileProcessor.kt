package software_testing_hw4.util

import ast.ASTBuilder
import ast.Function
import generated.mygrammarLexer
import generated.mygrammarParser
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.File

object FileProcessor {
    fun parseFunction(file: File): Function {
        val input = CharStreams.fromPath(file.toPath())
        val lexer = mygrammarLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = mygrammarParser(tokens)
        val tree = parser.function()

        val builder = ASTBuilder.create().also {
            ParseTreeWalker.DEFAULT.walk(it, tree)
        }

        return builder.getFunction()
    }
}
