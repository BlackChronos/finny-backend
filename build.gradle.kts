import dev.socialbooster.gradle.reactiveapi.task.GenerateReactiveAPI

plugins {
    id("java")
    id("org.springframework.boot").version("3.0.1")
    id("io.spring.dependency-management").version("1.1.0")
    id("dev.socialbooster.gradle.reactiveapi").version("1.2.3-SNAPSHOT")
}

group = "com.github.sbooster.backend"
version = "0.0.2-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
    all {
        exclude(module = "spring-boot-starter-logging")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.security:spring-security-rsocket")
    implementation("com.auth0:java-jwt:4.2.1")
    implementation("org.apache.logging.log4j:log4j-slf4j-impl")
    implementation("org.jetbrains:annotations:23.1.0")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<GenerateReactiveAPI> {
        prettyPrint = true
    }
}
