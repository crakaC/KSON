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
        result["grand-father"]["parent"]["child"]["grand-child"].value shouldBe null
        result["grand-mother"]["parent"]["child"]["grand-child"].value shouldBe 1
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

    "duplicated key" {
        val input = """
            {
                "key" : "value1",
                "key" : "value2"
            }
        """.trimIndent()
        shouldThrow<JSONException> {
            JSON.parse(input)
        }
    }

    "missing key" {
        val json = JSON.parse(
            """
            {"key" : "value"}
        """.trimIndent()
        )
        shouldThrow<JSONException> {
            json["not a kay"]
        }
    }
})