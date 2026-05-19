plugins {
    `java-library`
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

dependencies {
    api(project(":common-secret-manager"))
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    api("org.mybatis.spring.boot:mybatis-spring-boot-starter:3.0.4")
    api("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.5.2")

    api("org.springframework:spring-jdbc:6.2.7")
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    implementation("org.springframework.boot:spring-boot:3.5.0")

    runtimeOnly("org.postgresql:postgresql:42.7.5")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
