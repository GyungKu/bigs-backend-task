import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.2.4" apply false
	id("io.spring.dependency-management") version "1.1.4"
	id ("org.jetbrains.kotlin.plugin.allopen") version "1.5.21"
	id ("org.jetbrains.kotlin.plugin.noarg") version "1.5.21"
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	kotlin("plugin.jpa") version "1.9.23" apply false
}

java.sourceCompatibility = JavaVersion.VERSION_17

noArg {
	annotation("javax.persistence.Entity")
}

allprojects {

	group = "com.example"
	version = "0.0.1-SNAPSHOT"

	apply(plugin = "java")
	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")
	apply(plugin = "kotlin-jpa")

	apply(plugin = "org.jetbrains.kotlin.jvm")
	apply(plugin = "org.jetbrains.kotlin.plugin.spring")
	apply(plugin = "org.jetbrains.kotlin.plugin.jpa")
	apply(plugin = "org.springframework.boot")
	apply(plugin = "io.spring.dependency-management")

	allOpen {
		annotation("javax.persistence.Entity")
		annotation("javax.persistence.MappedSuperclass")
		annotation("javax.persistence.Embeddable")
	}

	repositories {
		mavenCentral()
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs += "-Xjsr305=strict"
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}

}

subprojects {

	dependencies {
		implementation("org.springframework.boot:spring-boot-starter-data-jpa")
		implementation("org.springframework.boot:spring-boot-starter-web")
		implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
		implementation("org.jetbrains.kotlin:kotlin-reflect")
		compileOnly("org.projectlombok:lombok")
		runtimeOnly("com.mysql:mysql-connector-j")
		annotationProcessor("org.projectlombok:lombok")
		testImplementation("org.springframework.boot:spring-boot-starter-test")
	}

}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}