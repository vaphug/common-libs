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
    api("org.springframework:spring-webflux:6.2.7")
    api("org.springframework.boot:spring-boot-autoconfigure:3.5.0")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.springframework.boot:spring-boot:3.5.0")
    implementation("io.projectreactor.netty:reactor-netty-http:1.2.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
