plugins {
    `java-library`
}

dependencies {
    api(project(":common-messages"))
    api("jakarta.validation:jakarta.validation-api:3.1.1")
    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")

    testImplementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    testImplementation("org.glassfish:jakarta.el:4.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
    testCompileOnly("org.projectlombok:lombok:1.18.38")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
}
