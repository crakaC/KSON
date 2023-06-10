package com.crakac.kson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf

class JSONArrayTest : StringSpec({
    "empty array" {
        val result = JSON.parse("[]") as JSONArray
        result.size shouldBe 0
    }

    "trailing comma" {
        shouldThrow<JSONException> {
            JSON.parse(
                """
                ["a b c", ]
            """.trimIndent()
            )
        }
    }

    "composite array" {
        val input = """
            [1, null, "string", [{}], {"key": "value"}]
        """.trimIndent()
        val result = JSON.parse(input) as JSONArray
        result[0].value shouldBe 1
        result[1].value shouldBe null
        result[2].value shouldBe "string"
        result[3][0].shouldBeInstanceOf<JSONObject>()
        result[4]["key"].value shouldBe "value"
    }

    "nested array" {
        val input = "[[[3]], [2], 1]"
        val result = JSON.parse(input)
        result[0][0][0].value shouldBe 3
    }

    "index out of bounds" {
        shouldThrow<IndexOutOfBoundsException> {
            val array = JSON.parse(
                """
                    [0]
                """.trimIndent()
            )
            array[1]
        }
    }
})