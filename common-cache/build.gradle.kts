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
    api(project(":common-secret-manager"))
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot:3.5.0")

    api("org.springframework.data:spring-data-redis:3.5.0")
    implementation("io.lettuce:lettuce-core:6.4.2.RELEASE")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
