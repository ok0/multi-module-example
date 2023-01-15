import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.7.5"
  id("io.spring.dependency-management") version "1.1.0"

  val kotlinVersion = "1.7.21"
  kotlin("jvm") version kotlinVersion
  kotlin("plugin.spring") version kotlinVersion
  kotlin("kapt") version kotlinVersion
}

java.sourceCompatibility = JavaVersion.VERSION_17

allprojects {
  repositories {
    mavenCentral()
  }

  group = "kr.co.ok0"
  version = "0.0.1"

  tasks.withType<Test> {
    useJUnitPlatform()
  }

  tasks.withType<KotlinCompile> {
    kotlinOptions {
      freeCompilerArgs = listOf("-Xjsr305=strict")
      jvmTarget = JavaVersion.VERSION_17.toString()
    }
  }
}

subprojects {
  apply {
    plugin("kotlin")
    plugin("kotlin-spring")
    plugin("org.springframework.boot")
    plugin("io.spring.dependency-management")
    plugin("kotlin-allopen")
  }

  tasks.bootJar {
    archiveFileName.set("app.jar")
  }
}

extra["logbackVersion"] = "0.1.5"
allprojects {
  dependencies {
    implementation("ch.qos.logback.contrib:logback-jackson:${property("logbackVersion")}")
    implementation("ch.qos.logback.contrib:logback-json-classic:${property("logbackVersion")}")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  }
}