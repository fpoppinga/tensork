import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlinVersion = "1.3.21"
val spekVersion = "2.0.1"
plugins {
    kotlin("jvm") version "1.3.21"
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("script-runtime"))
    implementation(group = "com.kyonifer", name = "koma-core-ejml", version = "0.12")

    testImplementation(kotlin("test"))
    testImplementation("org.spekframework.spek2:spek-dsl-jvm:$spekVersion")
    testRuntimeOnly("org.spekframework.spek2:spek-runner-junit5:$spekVersion")

    // spek requires kotlin-reflect, can be omitted if already in the classpath
    testRuntimeOnly("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

}

repositories {
    mavenCentral()
    jcenter()
    maven(url = "https://dl.bintray.com/kyonifer/maven")
}

sourceSets {
    main {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("src/main/kotlin")
        }
    }

    test {
        withConvention(KotlinSourceSet::class) {
            kotlin.srcDir("test/main/kotlin")
        }
    }
}

val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}

tasks {
    test {
        useJUnitPlatform {
            includeEngines("spek2")
        }
    }
}