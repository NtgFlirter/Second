package com.yashwant.calculator

object CalculatorEngine {


    private fun sanitize(expr: String): String {

        if (expr.isBlank()) return "0"

        var e = expr

        // 1. Replace multiple operators
        e = e.replace("++", "+")
            .replace("--", "+")
            .replace("+-", "-")
            .replace("-+", "-")
            .replace("××", "×")
            .replace("÷÷", "÷")
            .replace("×÷", "×")
            .replace("÷×", "÷")

        // 2. Remove trailing operators
        while (e.isNotEmpty() &&
            (e.last() == '+' ||
                    e.last() == '-' ||
                    e.last() == '×' ||
                    e.last() == '÷' ||
                    e.last() == '.')) {
            e = e.dropLast(1)
        }

        // 3. Fix empty result
        if (e.isBlank()) return "0"

        return e
    }


    fun calculate(input: String): String {
        return try {

            if (input.isBlank()) return "0"

            val safeInput = sanitize(input)

            val step1 = preprocess(safeInput)
            val step2 = handlePercent(step1)
            val step3 = fixImplicitMultiplication(step2)

            val result = evaluate(step3)

            format(result)

        } catch (e: Exception) {
            "Error"
        }
    }

    private fun preprocess(input: String): String {
        return input
            .replace("×", "*")
            .replace("÷", "/")
    }

    private fun handlePercent(input: String): String {

        var expr = input

        //  CONTEXT: A + B%
        val contextRegex = Regex("(\\d+(?:\\.\\d+)?)([+\\-×÷])(\\d+(?:\\.\\d+)?)%")

        while (true) {

            val match = contextRegex.find(expr) ?: break

            val a = match.groupValues[1].toDouble()
            val op = match.groupValues[2]
            val b = match.groupValues[3].toDouble()

            val replaced = when (op) {

                "+" -> a + (a * b / 100)
                "-" -> a - (a * b / 100)
                "×" -> a * (b / 100)
                "÷" -> a / (b / 100)

                else -> a
            }

            expr = expr.replaceRange(match.range, replaced.toString())
        }

        // STANDALONE: 10% → 0.1
        val standaloneRegex = Regex("(\\d+(?:\\.\\d+)?)%")

        while (true) {

            val match = standaloneRegex.find(expr) ?: break

            val value = match.groupValues[1].toDouble() / 100

            expr = expr.replaceRange(match.range, value.toString())
        }

        return expr
    }


    private fun fixImplicitMultiplication(input: String): String {

        val sb = StringBuilder()
        var i = 0

        while (i < input.length) {

            val ch = input[i]
            sb.append(ch)

            if (ch == '%') {

                val next = if (i + 1 < input.length) input[i + 1] else ' '

                // if next is digit or '(' → multiply automatically
                if (next.isDigit() || next == '(') {
                    sb.append("*")
                }
            }

            i++
        }

        return sb.toString()
    }

    private fun fixPercentImplicitMultiplication(input: String): String {

        val sb = StringBuilder()
        var i = 0

        while (i < input.length) {

            val ch = input[i]
            sb.append(ch)

            if (ch == '%') {

                val next = if (i + 1 < input.length) input[i + 1] else null

                if (next != null && next.isDigit()) {
                    sb.append("*")
                }
            }

            i++
        }

        return sb.toString()
    }


    private fun evaluate(expr: String): Double {

        return object {

            var pos = -1
            var ch = 0
            val str = expr

            fun nextChar() {
                ch = if (++pos < str.length) str[pos].code else -1
            }

            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.code) nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val value = parseExpression()

                if (pos < str.length) {
                    throw RuntimeException("Unexpected: ${ch.toChar()}")
                }

                return value
            }

            fun parseExpression(): Double {
                var x = parseTerm()

                while (true) {
                    when {
                        eat('+'.code) -> x += parseTerm()
                        eat('-'.code) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()

                while (true) {
                    when {
                        eat('*'.code) -> x *= parseFactor()
                        eat('/'.code) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }

            fun parseFactor(): Double {

                if (eat('+'.code)) return parseFactor()
                if (eat('-'.code)) return -parseFactor()

                val startPos = pos
                var x: Double

                if (eat('('.code)) {

                    x = parseExpression()
                    eat(')'.code)

                } else {

                    while (
                        ch in '0'.code..'9'.code ||
                        ch == '.'.code
                    ) {
                        nextChar()
                    }

                    val number = str.substring(startPos, pos)

                    if (number.isEmpty()) {
                        throw RuntimeException("Invalid number")
                    }

                    x = number.toDouble()
                }

                return x
            }

        }.parse()
    }

    private fun format(value: Double): String {

        if (value.isNaN() || value.isInfinite())
            return "Error"

        val abs = kotlin.math.abs(value)

        return when {

            value == 0.0 -> "0"

            abs >= 1e15 || abs < 1e-7 -> {
                String.format("%.6E", value)
                    .replace("E", "E")
            }

            value % 1 == 0.0 -> {
                value.toLong().toString()
            }

            else -> {
                String.format("%.10f", value)
                    .trimEnd('0')
                    .trimEnd('.')
            }
        }
    }
}
