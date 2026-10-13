plugins {
	java
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.1.7"
}

version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}

extra["tanzu.spring.application-services.version"] = "2.0.0"

dependencyManagement {
	imports {
		mavenBom("com.vmware.tanzu.spring:tanzu-spring-sdk-dependencies:1.2.0")
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("com.vmware.tanzu.spring.applicationservices:application-services-starter-feature-flag")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
