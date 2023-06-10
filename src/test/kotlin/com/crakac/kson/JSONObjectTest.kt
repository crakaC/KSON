package com.crakac.kson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class JSONObjectTest : StringSpec({
    "empty object" {
        val result = JSON.parse("{}") as JSONObject
        result.size shouldBe 0
    }

    "simple object" {
        val input = """
            {"key": "value"}
        """.trimIndent()
        val result = JSON.parse(input) as JSONObject
        (result["key"] as JSONString).value shouldBe "value"
    }

    "nested object" {
        val input = """
            {
              "grand-father": {
                "parent": {
                  "child": {
                    "grand-child": null
                  }
                }
              },
              "grand-mother": {
                "parent": {
                  "child": {
                    "grand-child": 1
                  }
                }
              }
            }
        """.trimIndent()
        val result = JSON.parse(input) as JSONObject
        result["grand-father"]["parent"]["child"]["grand-child"] shouldBe JSONNull
        result["grand-mother"]["parent"]["child"]["grand-child"] shouldBe JSONNumber(1)
    }

    "missing name separator" {
        val input = """
            {"a" "b"}
        """.trimIndent()
        shouldThrow<JSONException> {
            JSON.parse(input)
        }
    }

    "invalid name separator" {
        val input = """
            {"a", "b"}
        """.trimIndent()
        shouldThrow<JSONException> {
            JSON.parse(input)
        }
    }

    "missing {}" {
        val input = """
            "a":"b"
        """.trimIndent()
        shouldThrow<JSONException> {
            JSON.parse(input)
        }
    }
})