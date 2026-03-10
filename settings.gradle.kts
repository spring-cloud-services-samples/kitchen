rootProject.name = "kitchen"

dependencyResolutionManagement {
    val secArtifactoryUsername: String? by settings
    val secArtifactoryPassword: String? by settings

    repositories {
        mavenCentral()
        maven {
            url = uri("https://packages.broadcom.com/artifactory/spring-enterprise")
            credentials {
                username = System.getenv("SEC_ARTIFACTORY_USERNAME") ?: secArtifactoryUsername ?: ""
                password = System.getenv("SEC_ARTIFACTORY_PASSWORD") ?: secArtifactoryPassword ?: ""
            }
        }
    }
}
