package ru.geekbrains.architect.parser.oop

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