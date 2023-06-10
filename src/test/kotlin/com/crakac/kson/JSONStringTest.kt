package com.crakac.kson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.filterNot
import io.kotest.property.arbitrary.map
import io.kotest.property.arbitrary.string
import io.kotest.property.checkAll

class JSONStringTest : StringSpec({
    "struct characters in string" {
        val input = """
            "{}[]:,"
        """.trimIndent()
        val result = JSON.parse(input) as JSONString
        result.value shouldBe "{}[]:,"
    }

    "escaping quote" {
        val input = """
            "\\\\\"Hello!\"\/\/"
        """.trimIndent()
        val result = JSON.parse(input) as JSONString
        result.value shouldBe """
            \\"Hello!"//
        """.trimIndent()
    }

    "escaping \\b, \\f, \\n, \\r, \\t, \\u" {
        val formFeed = 0x0C.toChar()
        val input = """
             "\b\f\n\r\t\uD834\uDD1E"
        """.trimIndent()
        val result = JSON.parse(input) as JSONString
        result.value shouldBe "\b$formFeed\n\r\t\uD834\uDD1E"
    }

    "invalid escaping \\a" {
        val validChars = charArrayOf('"', 'b', 'f', 'n', 'r', 't', 0x0C.toChar(), '/', '\\')
        val invalidChars = Arb.char().filterNot { it in validChars }
        checkAll(invalidChars) {
            val input = """
                "\$it"
            """.trimIndent()
            shouldThrow<JSONException> {
                JSON.parse(input)
            }
        }
    }

    "string" {
        val arbString = Arb.string(minSize = 1).filterNot { it.contains("\\u") }.map { it.escape() }
        checkAll(arbString) {
            val input = """
                "$it" 
            """.trimIndent()
            val result = JSON.parse(input)
            result.shouldBeInstanceOf<JSONString>()
        }
    }

    "escape" {
        """
             abc"\b\f\n\r\t\uD834\uDD1E"def
             2nd
        """.trimIndent().escape() shouldBe "abc\\\"\\b\\f\\n\\r\\t\\uD834\\uDD1E\\\"def\n2nd"
    }

    "demo" {
        val input = "{\"language\":\"kotlin\"}"
        val obj = JSON.parse(input)
        println(obj["language"].value) // => kotlin
    }
})

fun String.escape(): String {
    val sb = StringBuilder()
    for ((i, c) in toCharArray().withIndex()) {
        if (c == '"') {
            sb.append("\\\"")
            continue
        }
        if (c == '\\') {
            if (i + 1 !in indices || this[i + 1] !in "utbnrf") {
                sb.append("\\\\")
            } else {
                sb.append('\\')
            }
            continue
        }
        sb.append(c)
    }
    return sb.toString()
}