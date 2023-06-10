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
        (result[0] as JSONNumber<*>).value shouldBe 1
        result[1].shouldBeInstanceOf<JSONNull>()
        (result[2] as JSONString).value shouldBe "string"
        result[3].shouldBeInstanceOf<JSONObject>()
        val innerObject = result[4] as JSONObject
        (innerObject["key"] as JSONString).value shouldBe "value"
    }

    "nested array" {
        val input = "[[[3]], [2], 1]"
        val result = JSON.parse(input)
        result[0][0][0] shouldBe JSONNumber(3)
    }
})