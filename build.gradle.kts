plugins {
    java
    id("org.springframework.boot") version "4.0.6"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:4.0.1")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.thymeleaf.extras:thymeleaf-extras-springsecurity6")
    implementation("org.mapstruct:mapstruct:1.6.3")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.6.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
    // Spring Boot 4 ではテストスライスが機能別モジュールに分離された。
    // @WebMvcTest / @AutoConfigureMockMvc は spring-boot-webmvc-test、
    // @JdbcTest / @AutoConfigureTestDatabase は spring-boot-jdbc-test が提供する。
    // MockMvc への Spring Security 連携 (@WithMockUser を効かせる springSecurity() の自動適用) は
    // spring-boot-security-test が提供する (Boot 3 では test-autoconfigure に同梱されていた)。
    testImplementation("org.springframework.boot:spring-boot-webmvc-test")
    testImplementation("org.springframework.boot:spring-boot-jdbc-test")
    testImplementation("org.springframework.boot:spring-boot-security-test")
    // テスト用に H2 をインメモリで使う。runtime のみで十分 (JDBC ドライバとして読み込まれる)
    testRuntimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
    if (project.hasProperty("verboseTest")) {
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName = "${rootProject.name}-${version}.jar"
}
