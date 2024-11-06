plugins {
    id("org.springframework.boot") version "3.3.5"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
    kotlin("plugin.jpa") version "1.9.23"
}
apply(plugin = "io.spring.dependency-management")

group = "pro.dionea"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("com.vdurmont:emoji-java:5.1.1")
    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars:jquery:3.7.1")
    implementation("org.webjars:popper.js:2.11.7")
    implementation("org.webjars:summernote:0.8.10")
    implementation("org.webjars:font-awesome:6.3.0")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.liquibase:liquibase-core:4.22.0")
    implementation("org.postgresql:postgresql:42.7.3")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    implementation("org.telegram:telegrambots:5.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.7")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("com.github.peterarsentev:SpringFakeTest:v.1.0.3")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6:3.1.1.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    testImplementation("org.junit.vintage:junit-vintage-engine:5.8.2")
    testImplementation("org.assertj:assertj-core:3.23.1")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

tasks.test {
    useJUnitPlatform()
}

sourceSets {
    main {
        java.srcDirs("src/main/kotlin")
    }
    test {
        java.srcDirs("src/test/kotlin")
    }
}
