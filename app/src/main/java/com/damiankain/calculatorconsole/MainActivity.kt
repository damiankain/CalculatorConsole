package com.damiankain.calculatorconsole

fun main() {
    // работает вот с таким ->  (42 + 53 + (1 + 1 * 1) / 2)  - в такой записи со скобками
    // или  2+(2*2)
    // или 2+2*2
    var input: String? = readLine()
    // удаляю лишние пробелы из строки при вводе
    input = input?.replace("\\s".toRegex(), "")

    println(input?.let { buildStringPolishNotation(it) }?.let { getAnswer(it) })
}

fun buildStringPolishNotation(exp: String): String { // строю строку польской нотации
    val stackOperators = ArrayDeque<Char>() // стек для знаков операций
    var temp: String = ""  // временная переменная строки для записи чисел через пробел
    var priority: Int  // получаем приоритет символа

    for (i in exp.indices) {
        priority = getPriority(exp[i])
        when (priority) {
            0 -> temp += exp[i]

            1 -> stackOperators.addLast(exp[i])

            2, 3 -> {
                temp += " "
                while (!stackOperators.isEmpty()) {
                    if (getPriority(stackOperators.last()) >= priority) {
                        temp += stackOperators.removeLast()
                    } else break
                }
                stackOperators.addLast(exp[i])
            }
            -1 -> {
                temp += " "
                while (getPriority(stackOperators.last()) != 1) {
                    temp += stackOperators.removeLast()
                }
                stackOperators.removeLast()
            }
        }
    }
    while (!stackOperators.isEmpty()) {
        temp += stackOperators.removeLast()
    }
    return temp
}

fun getAnswer(rpn: String): Double { // получаем ответ из строки польской нотации
    var operand: String = ""
    val stack = ArrayDeque<Double>()
    var priority: Int

    try {
        for (i in 0..rpn.length) {
            priority = getPriority(rpn[i])

            when (priority) {
                0 -> {
                    operand += rpn[i]
                }

                -2 -> {
                    if (operand.equals("")) {
                        continue
                    }
                    stack.addLast(operand.toDouble())
                    operand = ""
                }

                2, 3 -> {
                    if (operand != "") {
                        stack.addLast(operand.toDouble())
                    }
                    var b = stack.removeLast()
                    var a = stack.removeLast()
                    when (rpn[i]) {
                        '+' -> stack.addLast(b + a)
                        '-' -> stack.addLast(b - a)
                        '*' -> stack.addLast(b * a)
                        '/' -> stack.addLast(b / a)
                    }
                }
                else -> {
                    stack.addLast(operand.toDouble())
                    operand += rpn[i]
                }
            }
        }
    } catch (e: StringIndexOutOfBoundsException) { // РАЗОБРАТЬСЯ ПОЧЕМУ ТАКАЯ ОШИБКА

    }
    return stack.last()
}

fun getPriority(a: Char): Int { // функция определения приоритета символа
    return when (a) {
        '*', '/' -> 3
        '+', '-' -> 2
        '(' -> 1
        ')' -> -1
        ' ' -> -2
        else -> 0
    }
}
