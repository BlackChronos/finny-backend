pluginManagement {
    repositories {
        maven("https://repo.spring.io/release")
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.animecraft.fun/repository/maven-snapshots/")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "org.hibernate.orm") {
                useModule("org.hibernate:hibernate-gradle-plugin:5.6.7.Final")
            }
        }
    }
}

// TODO change project name
rootProject.name = "backend-template"
