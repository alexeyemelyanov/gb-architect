package ru.geekbrains.architect.parser.oop

import java.util.*

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