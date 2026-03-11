plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	pmd
}

group = "id.ac.ui.cs.advprog"
version = "0.0.1-SNAPSHOT"
description = "eshop"

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release.set(17)
}

repositories {
	mavenCentral()
}

val seleniumJavaVersion = "4.14.1"
val seleniumJupiterVersion = "5.0.1"
val webdrivermanagerVersion = "5.6.3"
val junitJupiterVersion = "5.9.1"


dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")

	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

	developmentOnly("org.springframework.boot:spring-boot-devtools")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.seleniumhq.selenium:selenium-java:$seleniumJavaVersion")
	testImplementation("io.github.bonigarcia:selenium-jupiter:$seleniumJupiterVersion")
	testImplementation("io.github.bonigarcia:webdrivermanager:$webdrivermanagerVersion")
	testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")

	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test>().configureEach {
	useJUnitPlatform()
}

tasks.test {
	exclude("**/*FunctionalTest*")
	finalizedBy(tasks.jacocoTestReport)
}

tasks.register<Test>("functionalTest") {
	description = "Runs functional tests"
	group = "verification"
	useJUnitPlatform()
	include("**/*FunctionalTest*")
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required.set(true)
		html.required.set(true)
		csv.required.set(false)
	}
}
pmd {
	toolVersion = "7.0.0-rc4"
	isConsoleOutput = true
	isIgnoreFailures = false
	ruleSets = emptyList()
	ruleSetFiles = files("$rootDir/config/pmd/ruleset.xml")
}
tasks.withType<Pmd>().configureEach {
	reports {
		xml.required.set(true)
		html.required.set(true)
	}
}