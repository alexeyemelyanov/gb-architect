package ru.geekbrains.architect.parser.procedural

import java.util.*

fun main() {
    println("Type 'exit' to stop")

    Scanner(System.`in`).use { scanner ->
        while (scanner.hasNextLine()) {
            try {
                val line = scanner.nextLine()
                if (line == "exit") break
                eval(line)
            } catch (e: Throwable) {
                System.err.println(e)
            }
        }
    }
}

private fun eval(line: String) {

}