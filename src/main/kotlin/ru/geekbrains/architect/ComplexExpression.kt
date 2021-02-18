package ru.geekbrains.architect

class ComplexExpression(ex1: Expression, ex2: Expression, operation: Operation) : Expression {
    override val result: Number = operation.apply(ex1, ex2)
    override val rightPolandNotation: String = "${ex1.rightPolandNotation} ${ex2.rightPolandNotation} ${operation.char}"
}