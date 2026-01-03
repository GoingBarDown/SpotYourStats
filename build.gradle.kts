plugins {
    kotlin("jvm") version "2.0.21"
    application
}

repositories { mavenCentral() }

dependencies {
    // Spring (NO Boot)
    implementation("org.springframework:spring-context:6.1.14")
    implementation("org.springframework:spring-webmvc:6.1.14")

    // Embedded Tomcat (Servlet 6 / Jakarta)
    implementation("org.apache.tomcat.embed:tomcat-embed-core:10.1.31")
    implementation("org.apache.tomcat.embed:tomcat-embed-el:10.1.31")

    // JSON
    implementation("com.fasterxml.jackson.core:jackson-databind:2.17.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.2")

    // DB + pooling
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:5.1.0")

    // Migrations
    implementation("org.flywaydb:flyway-core:10.20.1")

    // Logging
    implementation("org.slf4j:slf4j-api:2.0.16")
    runtimeOnly("ch.qos.logback:logback-classic:1.5.8")

    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.vibevault.MainKt")
}

kotlin {
    jvmToolchain(21) // change to 17 if you prefer
}
