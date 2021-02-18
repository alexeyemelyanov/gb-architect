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

object Parser {
    infix fun parse(_string: String): Expression {
        val string = _string.replace(" ", "")

        val operations = LinkedList<Operation>()
        val operands = LinkedList<Expression>()

        var index = 0
        while (index < string.length) {
            var c = string[index++]

            if (c == '(') {
                val start = index
                var counter = 1
                while (counter > 0 && index < string.length) {
                    c = string[index++]
                    if (c == '(') {
                        counter++
                    } else if (c == ')') {
                        counter--
                    }
                }
                operands.add(parse(string.substring(start, index - 1)))
            } else {
                val op = Operation.byChar(c)
                if (op == null) {
                    index = parseOperand(string, index - 1) { operands.add(it) }
                } else {
                    operations.add(op)
                }
            }
        }

        return resolveFinalExpression(operands, operations)
    }

    private inline fun parseOperand(string: String, start: Int, callback: (SimpleOperand) -> Unit): Int {
        var index = start
        var c = string[index]

        if (index == string.length) {
            val stringResult = c.toString()
            callback(SimpleOperand(stringResult))
        } else {
            while ((c.isDigit() || c == '.') && index < string.length) {
                ++index
                if (index == string.length) break
                else c = string[index]

            }
            val substring = string.substring(start, index)
            callback(SimpleOperand(substring))
        }

        return index
    }

    private fun resolveFinalExpression(
        operands: LinkedList<Expression>,
        operations: LinkedList<Operation>
    ): Expression {
        if (operations.isEmpty()) {
            if (operands.isEmpty()) {
                throw IllegalArgumentException()
            } else {
                return operands[0]
            }
        } else if (operations.size + 1 != operands.size) {
            throw IllegalArgumentException()
        } else {
            for (opToCheck in Operation.sortedByPriority) {
                var index = operations.indexOfFirst { it == opToCheck }
                while (index > -1) {
                    val op1 = operands[index]
                    val op2 = operands[index + 1]
                    val op = operations[index]
                    val expression = ComplexExpression(op1, op2, op)
                    operations.removeAt(index)
                    operands.removeAt(index + 1)
                    operands[index] = expression
                    index = operations.indexOfFirst { it == opToCheck }
                }
            }
            return operands[0]
        }
    }
}

interface Expression {
    val result: Number
    val rightPolandNotation: String
}

class SimpleOperand constructor(override val result: Number) : Expression {
    constructor(stringResult: String) : this(stringResult.toLongOrNull() ?: stringResult.toDouble())

    override val rightPolandNotation = result.toString()
}

class ComplexExpression(ex1: Expression, ex2: Expression, operation: Operation) : Expression {
    override val result: Number = operation.apply(ex1, ex2)
    override val rightPolandNotation: String = "${ex1.rightPolandNotation} ${ex2.rightPolandNotation} ${operation.char}"
}

enum class Operation(val char: Char, val priority: Int) {
    plus('+', 2), minus('-', 3), multiply('*', 0), divide('/', 1);

    fun apply(op1: Expression, op2: Expression): Number = when (this) {
        plus -> if (op1.result is Double || op2.result is Double) {
            val res = op1.result.toDouble() + op2.result.toDouble()
            val long = res.toLong()
            if (long.compareTo(res) == 0) long else res
        } else op1.result.toLong() + op2.result.toLong()

        minus -> if (op1.result is Double || op2.result is Double) {
            val res = op1.result.toDouble() - op2.result.toDouble()
            val long = res.toLong()
            if (long.compareTo(res) == 0) long else res
        } else op1.result.toLong() - op2.result.toLong()

        multiply -> if (op1.result is Double || op2.result is Double) {
            val res = op1.result.toDouble() * op2.result.toDouble()
            val long = res.toLong()
            if (long.compareTo(res) == 0) long else res
        } else op1.result.toLong() * op2.result.toLong()

        divide -> if (op1.result is Double || op2.result is Double || op1.result.toLong() % op2.result.toLong() != 0L) {
            val res = op1.result.toDouble() / op2.result.toDouble()
            val long = res.toLong()
            if (long.compareTo(res) == 0) long else res
        } else op1.result.toLong() / op2.result.toLong()
    }

    override fun toString() = "$char"

    companion object {

        val sortedByPriority = values().sortedBy { it.priority }

        fun byChar(char: Char) = when (char) {
            '+' -> plus
            '-' -> minus
            '*' -> multiply
            '/' -> divide
            else -> null
        }
    }
}
