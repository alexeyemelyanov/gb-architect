package ru.geekbrains.architect

class SimpleOperand constructor(override val result: Number) : Expression {
    constructor(stringResult: String) : this(stringResult.toLongOrNull() ?: stringResult.toDouble())

    override val rightPolandNotation = result.toString()
}