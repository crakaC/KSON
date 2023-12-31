plugins {
    kotlin("jvm") version "1.8.21"
}

group = "com.crakac"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(platform("io.kotest:kotest-bom:5.6.2"))
    testImplementation("io.kotest:kotest-assertions-core")
    testImplementation("io.kotest:kotest-property")
    testImplementation("io.kotest:kotest-runner-junit5")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}

tasks {
    compileKotlin {
        kotlinOptions {
            languageVersion = "1.9"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            languageVersion = "1.9"
        }
    }
}