package com.crakac.kson

sealed interface JSONLiteral : JSONValue
data object JSONNull : JSONLiteral
data class JSONBool(val value: Boolean) : JSONLiteral
data class JSONNumber<T : Number>(val value: T) : JSONLiteral
data class JSONString(val value: String) : JSONLiteral