package com.crakac.kson

class JSON private constructor() {
    companion object {
        fun parse(jsonString: String): JSONValue {
            return JSONParser().parse(jsonString)
        }

        fun escape(string: String): String {
            val sb = StringBuilder()
            for (c in string.toCharArray()) {
                when (c) {
                    '\t' -> sb.append("\\t")
                    '\n' -> sb.append("\\n")
                    '\r' -> sb.append("\\r")
                    '\b' -> sb.append("\\b")
                    FormFeed -> sb.append("\\f")
                    else -> sb.append(c)
                }
            }
            return sb.toString()
        }
    }
}