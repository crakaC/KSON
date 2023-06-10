package com.crakac.kson

sealed interface JSONLiteral : JSONValue
data object JSONNull : JSONLiteral {
    override val value: Nothing? = null
}

data class JSONBool(override val value: Boolean) : JSONLiteral
data class JSONNumber<T : Number>(override val value: T) : JSONLiteral
data class JSONString(override val value: String) : JSONLiteral