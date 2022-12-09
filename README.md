# multi-module-example
Gradle & Spring Boot 멀티 모듈 예제.

![](https://velog.velcdn.com/images/ok0/post/e0023b15-e538-4a83-833a-b072df6b5549/image.jpg)

# Gradle을 이용한 Multi Module 만들기
## Overview
3개의 hierarchy(계층)를 갖는 멀티 프로젝트를 구성합니다.
- Root Module
- API
    + Member API
    + Product API

---
## Root Module
두가지의 코드를 작성합니다.
- 프로젝트 전반에 영향을 끼치지만, 비즈니스 로직에는 전혀 관여하지 않는 'Log' 관련 코드.
- 구조 접근을 테스트할 수 있는 'Welcome Class'를 정의합니다.

### settings.gradle.kts
프로젝트 이름은 "multi-module-example"으로 생성합니다.
settings.gradle.kts를 열람해보시면 아래처럼 한줄이 추가되어있습니다.
```kotlin
rootProject.name = "multi-module-example"
```
해당 파일은 빌드에 관여하는 프로젝트의 hierarchy를 정의하고 선언합니다.
IntelliJ같은 툴을 사용해서 멀티 모듈을 구성하다 보면, 우리가 뜻하지 않는 방향으로 진행되는 경우가 있는데요.
그 때마다 `settings.gradle.kts`에 우리의 의도대로 작성되어있는지 확인합니다.

### build.gradle.kts
빌드 작업을 자동화할 수 있는 스크립트들이 작성됩니다. 우리는 멀티 모듈을 만들것이기 때문에, 프로젝트 전반에 사용되는 디펜던시를 추가하겠습니다. [allprocjects와 subprocjtes의 차이점](https://kwonnam.pe.kr/wiki/gradle/multiproject)을 확인 부탁드립니다.
### allprojects
프로젝트의 모든 모듈에 적용되는 스크립트가 작성됩니다. 여기서는 전체 프로젝트에 영향을 끼치는 Log 관련 디펜던시가 추가되었습니다.

### subprojctes
하위 모듈을 제어하는 스크립트가 작성됩니다. spring application 개발을 위해서 디펜던시를 추가해줍니다.
```kotlin
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
```

---
## multi-module-api
### build.gradle.kts
jar로 wrapping될 필요가 없기때문에 관련 코드도 함께 작성되어있습니다.
하위 프로젝트에서 사용 될 Spring Boot 관련 디펜던시를 추가합니다.
```kotlin
tasks.getByName<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
  enabled = false
}

tasks.getByName<Jar>("jar") {
  enabled = true
}

subprojects {
  dependencies {
    implementation(project(rootProject.path))
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
  }
}
```
### multi-module-api-member, multi-module-api-product
Spring Boot Application을 선언하고, 간단한 컨트롤러를 만듭니다. : )

---