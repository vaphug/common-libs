plugins {
    `java-library`
}

dependencies {
    api(project(":common-message-core"))
    api(project(":common-messages"))
    implementation(project(":common-validation"))

    compileOnly("org.springframework:spring-context:6.2.7")

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.5.0")
}
