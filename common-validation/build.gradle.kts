plugins {
    `java-library`
}

dependencies {
    api(project(":common-messages"))
    api("jakarta.validation:jakarta.validation-api:3.1.1")

    testImplementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    testImplementation("org.glassfish:jakarta.el:4.0.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.11.4")
}
