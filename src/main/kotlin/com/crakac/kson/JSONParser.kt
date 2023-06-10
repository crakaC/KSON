package com.crakac.kson

// https://www.ietf.org/rfc/rfc8259.txt
// https://android.googlesource.com/platform/libcore/+/refs/heads/android13-platform-release/json/src/main/java/org/json/JSONTokener.java

private const val Quote = '"'
private const val EOF = (-1).toChar()
internal const val FormFeed = 0x0C.toChar()
private const val NotLiteralChars = "{}[]\\:, \r\n\t$FormFeed"

class JSONParser {
    private var pos = 0
    private var bounds = 0
    private var input = ""

    fun parse(jsonText: String): JSONValue {
        if (jsonText.isEmpty()) {
            throw IllegalArgumentException("Empty text is not permitted")
        }
        pos = 0
        input = jsonText
        bounds = jsonText.length
        val ret = nextValue()
        println(ret.toString())
        if (nextChar() != EOF) {
            throw syntaxError("Extra tokens followed")
        }
        return ret
    }

    private fun nextChar(): Char {
        while (pos < bounds && input[pos].isWhitespace()) {
            pos++
        }
        return if (pos < bounds) {
            input[pos++]
        } else {
            EOF
        }
    }

    private fun nextString(): String {
        val start = pos
        while (pos < bounds) {
            val c = input[pos]
            if (c in NotLiteralChars) {
                return input.substring(start, pos)
            }
            pos++
        }
        return input.substring(start)
    }

    private fun readString(): JSONString {
        val sb = StringBuilder()
        var start = pos
        while (pos < bounds) {
            val c = input[pos++]
            if (c == Quote) {
                sb.append(input, start, pos - 1)
                return JSONString(sb.toString())
            }

            if (c == '\\') {
                if (pos == bounds) {
                    throw syntaxError("Unterminated escape sequence")
                }
                sb.append(input, start, pos - 1)
                sb.append(readEscapeCharacter())
                start = pos
            }
        }
        throw syntaxError("Unterminated string")
    }

    private fun readEscapeCharacter(): Char {
        when (val escaped = input[pos++]) {
            'u' -> {
                if (pos + 4 > bounds) {
                    throw syntaxError("Unterminated escape sequence")
                }
                val hex = input.substring(pos, pos + 4)
                pos += 4
                try {
                    return hex.toInt(16).toChar()
                } catch (_: NumberFormatException) {
                    throw syntaxError("Invalid escape sequence: $hex")
                }
            }

            't' -> return '\t'
            'b' -> return '\b'
            'n' -> return '\n'
            'r' -> return '\r'
            'f' -> return FormFeed
            '/', '\\', Quote -> return escaped
            else -> throw syntaxError("invalid escape")
        }
    }

    private fun nextValue(): JSONValue {
        return when (nextChar()) {
            EOF -> throw syntaxError("End of input")
            '{' -> readObject()
            '[' -> readArray()
            Quote -> readString()
            else -> {
                pos--
                readLiteral()
            }
        }
    }

    private fun syntaxError(message: String): JSONException {
        var line = 0
        var column = 0
        var lineString = ""
        var p = 0
        while (p < pos) {
            line++
            val index = input.indexOf("\n", p)
            if (index == -1) {
                column = pos - p
                lineString = input.substring(p, input.length)
                break
            } else if (index > pos) {
                column = pos - p
                lineString = input.substring(p, index)
                break
            }
            p = index + 1
        }
        return JSONException(
            """
            $message
            $lineString
            ${" ".repeat(column)}^
            line: $line, column: $column
        """.trimIndent()
        )
    }

    private fun readObject(): JSONObject {
        if (nextChar() == '}') {
            return JSONObject()
        }
        pos--
        val obj = JSONObject()
        while (true) {
            val name = nextValue()
            if (name !is JSONString) {
                throw syntaxError("name must be string")
            }
            if (nextChar() != ':') {
                throw syntaxError("Expected ':'")
            }
            obj[name.value] = nextValue()
            when (nextChar()) {
                '}' -> return obj
                ',' -> continue
                else -> throw syntaxError("Unterminated object")
            }
        }
    }

    private fun readArray(): JSONArray {
        val result = JSONArray()
        if (nextChar() == ']') return result
        pos--
        while (true) {
            result.add(nextValue())
            when (nextChar()) {
                ']' -> return result
                ',' -> continue
                else -> throw syntaxError("Unterminated array")
            }
        }
    }

    private fun readLiteral(): JSONLiteral {
        val literal = nextString()
        if (literal.isEmpty()) throw syntaxError("Expected literal value")
        when (literal) {
            "null" -> return JSONNull
            "true" -> return JSONBool(true)
            "false" -> return JSONBool(false)
        }
        if ('.' !in literal) {
            try {
                val longValue = literal.toLong()
                return if (longValue in Int.MIN_VALUE..Int.MAX_VALUE) {
                    JSONNumber(longValue.toInt())
                } else {
                    JSONNumber(longValue)
                }
            } catch (_: NumberFormatException) {
                // 浮動小数点型のときはエラーが出るので次に進む
            }
        }
        try {
            return JSONNumber(literal.toDouble())
        } catch (_: NumberFormatException) {
            // 浮動小数点でもだめ
        }
        throw syntaxError("Invalid literal: $literal")
    }
}