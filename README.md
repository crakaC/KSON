A simple Kotlin JSON parser

```
    val input = "{\"language\":\"kotlin\"}"
    val obj = JSON.parse(input)
    println(obj["laguage"].value) // => kotlin
```

## References
- https://www.ietf.org/rfc/rfc4627.txt
- https://android.googlesource.com/platform/libcore/+/refs/heads/android13-platform-release/json/src/main/java/org/json/JSONTokener.java
