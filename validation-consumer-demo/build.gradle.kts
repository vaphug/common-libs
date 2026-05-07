plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencies {
    implementation(project(":common-message-core"))
    implementation(project(":common-validation"))

    implementation("org.springframework.boot:spring-boot-starter-web:3.5.0")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.5.0")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
}
