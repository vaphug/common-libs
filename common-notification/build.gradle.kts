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
    api(project(":common-notification-history"))

    api("org.springframework:spring-webflux:6.2.7")
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot:3.5.0")
    implementation("io.projectreactor.netty:reactor-netty-http:1.2.6")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.30.1")

    api("software.amazon.awssdk:ses:2.31.63")
    implementation("software.amazon.awssdk:auth:2.31.63")
    implementation("software.amazon.awssdk:regions:2.31.63")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
