plugins {
    application
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

application {
    mainClass.set("main/kooozel/kotlin/Runner")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.reflections", "reflections", "0.10.2")
    implementation("org.slf4j:slf4j-nop:2.0.9")
    implementation("com.google.guava:guava:32.1.3-jre")
    compileOnly("org.projectlombok:lombok:1.18.30")
    annotationProcessor("org.projectlombok:lombok:1.18.30")
    testImplementation(kotlin("test"))
    implementation("org.junit.jupiter", "junit-jupiter-api", "5.10.1")
    implementation("org.junit.jupiter", "junit-jupiter-engine", "5.10.1")
    implementation("org.hamcrest", "hamcrest", "2.2")
    implementation("com.github.stefanbirkner:system-lambda:1.2.1")

    testCompileOnly("org.projectlombok:lombok:1.18.30")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.30")

    testImplementation("org.junit.jupiter", "junit-jupiter-api", "5.10.1")
    testImplementation("org.junit.jupiter", "junit-jupiter-engine", "5.10.1")
    testImplementation("org.hamcrest", "hamcrest", "2.2")
    testImplementation("com.github.stefanbirkner:system-lambda:1.2.1")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
        test {
            kotlin.srcDirs("src")
        }


    }
    test {
        useJUnitPlatform()
    }

    wrapper {
        gradleVersion = "8.4"
    }
}
