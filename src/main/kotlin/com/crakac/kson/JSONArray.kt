package com.crakac.kson

data class JSONArray(
    private val array: MutableList<JSONValue> = mutableListOf()
) : JSONValue, MutableList<JSONValue> by array {
    override val value: List<JSONValue>
        get() = array.toList()
}