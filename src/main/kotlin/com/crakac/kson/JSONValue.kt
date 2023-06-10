package com.crakac.kson

sealed interface JSONValue {
    val value: Any?
}

operator fun JSONValue.get(key: Any): JSONValue {
    return when (this) {
        is JSONObject -> get(key as String)
        is JSONArray -> get(key as Int)
        else -> {
            throw JSONException("Invalid operation")
        }
    }
}