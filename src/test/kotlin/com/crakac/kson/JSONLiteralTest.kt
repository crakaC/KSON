package com.crakac.kson

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.data.forAll
import io.kotest.data.headers
import io.kotest.data.row
import io.kotest.data.table
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.kotest.property.checkAll

class JSONLiteralTest : StringSpec({
    "null" {
        val result = JSON.parse("null")
        result.shouldBeInstanceOf<JSONNull>()
    }
    "true" {
        val result = JSON.parse("true")
        result.value shouldBe true
    }
    "false" {
        val result = JSON.parse("false")
        result.value shouldBe false
    }
    "integer" {
        checkAll<Long> {
            val result = JSON.parse(it.toString())
            result.value shouldBe it
        }
    }
    "floating point" {
        checkAll<Double> {
            val result = JSON.parse(it.toString())
            result.value shouldBe it
        }
    }
    "invalid literal" {
        forAll(
            table(
                headers("input"),
                row("not quoted string"), //
                row("1e3e5"),
                row("123L"),
                row("1.2.3")
            )
        ) {
            shouldThrow<JSONException> {
                JSON.parse(it)
            }
        }
    }
})