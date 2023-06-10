A simple Kotlin JSON parser

```kotlin
    val obj = JSON.parse("{\"language\":\"kotlin\"}")
    println(obj["language"].value) // => kotlin
```

## References
- https://www.ietf.org/rfc/rfc4627.txt
- https://android.googlesource.com/platform/libcore/+/refs/heads/android13-platform-release/json/src/main/java/org/json/JSONTokener.java
