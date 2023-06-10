package com.crakac.kson

data class JSONObject(
    private val members: MutableMap<String, JSONValue> = mutableMapOf()
) : JSONValue, MutableMap<String, JSONValue> by members {
    override val value: Map<String, JSONValue>
        get() = LinkedHashMap(members)

    override operator fun get(key: String): JSONValue {
        return members[key] ?: throw JSONException("$key is not found")
    }

    operator fun set(key: String, token: JSONValue) {
        if (key in members) {
            throw JSONException("$key is duplicated")
        }
        members[key] = token
    }
}