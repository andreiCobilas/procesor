import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.17"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.9.0"
	kotlin("plugin.spring") version "1.9.0"
}

group = "org.game"
version = project.findProperty("projVersion") ?: "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
	mavenCentral()
}

dependencies {

	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("com.h2database:h2")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")
	implementation("com.squareup.okhttp3:okhttp:4.9.0")
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-jackson:2.9.0")
//	implementation("com.squareup.okhttp3:okhttp-urlconnection:4.10.0")
	implementation("org.springframework.retry:spring-retry:2.0.0")
	implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.1")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.mockito:mockito-core:3.12.4")
	testImplementation("org.testng:testng:7.1.0")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
	kotlin.sourceSets.all {
		languageSettings.optIn("kotlin.RequiresOptIn")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
