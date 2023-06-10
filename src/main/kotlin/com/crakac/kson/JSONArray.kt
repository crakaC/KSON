package com.crakac.kson

data class JSONArray(private val array: MutableList<JSONValue> = mutableListOf()) : JSONValue {
    override val value: List<JSONValue>
        get() = array.toList()

    fun add(e: JSONValue) {
        array.add(e)
    }

    operator fun get(index: Int): JSONValue {
        return array[index]
    }

    val size: Int get() = array.size
}