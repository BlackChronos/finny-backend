import dev.socialbooster.gradle.reactiveapi.task.GenerateReactiveAPI
import org.springframework.boot.gradle.tasks.bundling.BootBuildImage

plugins {
    id("org.springframework.boot").version("2.6.6")
    id("io.spring.dependency-management").version("1.0.11.RELEASE")
    id("java")
    id("org.hibernate.orm")
    // ReactiveAPI
    id("dev.socialbooster.gradle.reactiveapi").version("1.2.3-SNAPSHOT")
}

group = "com.github.sbooster.backend"
version = "0.0.1-SNAPSHOT"
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
    maven("https://repo.spring.io/release")
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-rsocket")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.security:spring-security-messaging")
    implementation("org.springframework.security:spring-security-rsocket")

    implementation("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.xml.bind:jaxb-api:2.4.0-b180830.0359")

    implementation("org.apache.logging.log4j:log4j-slf4j-impl")

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}

tasks {
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
    withType<BootBuildImage> {
        builder = "paketobuildpacks/builder:tiny"
        environment = mapOf("BP_NATIVE_IMAGE" to "true")
    }
    withType<GenerateReactiveAPI> {
        prettyPrint = true
    }
}
