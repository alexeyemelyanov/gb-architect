package ru.geekbrains.architect

import java.util.*

fun main() {
    println("Type 'exit' to stop")

    Scanner(System.`in`).use { scanner ->
        while (scanner.hasNextLine()) {
            try {
                val line = scanner.nextLine()
                if (line == "exit") break
                val expression = Parser parse line
                println(expression.result)
                println(expression.rightPolandNotation)
            } catch (e: Throwable) {
                System.err.println(e)
            }
        }
    }
}