plugins {
    `java-library`
    id("org.springframework.boot")
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    enabled = false
}

tasks.named<Jar>("jar") {
    enabled = true
}

dependencies {
    api(project(":common-messages"))
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")

    api("org.springframework:spring-context:6.2.7")
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    implementation("org.springframework.boot:spring-boot:3.5.0")
    implementation("jakarta.validation:jakarta.validation-api:3.1.1")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
