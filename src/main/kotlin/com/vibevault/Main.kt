package com.vibevault

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.apache.catalina.startup.Tomcat
import org.flywaydb.core.Flyway
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext
import org.springframework.web.servlet.DispatcherServlet
import java.io.File

@Configuration
@ComponentScan(basePackages = ["com.vibevault"])
class AppConfig

fun main() {
    val port = (System.getenv("PORT") ?: "8080").toInt()

    val dbUrl = requireEnv("DB_URL")
    val dbUser = requireEnv("DB_USER")
    val dbPass = requireEnv("DB_PASS")

    val hikari = HikariConfig().apply {
        jdbcUrl = dbUrl
        username = dbUser
        password = dbPass
        maximumPoolSize = 5
    }
    val ds = HikariDataSource(hikari)

    Flyway.configure()
        .dataSource(ds)
        .locations("classpath:db/migration")
        .load()
        .migrate()

    val springCtx = AnnotationConfigWebApplicationContext().apply {
        register(AppConfig::class.java)
        refresh()
    }

    val tomcat = Tomcat()
    tomcat.setPort(port)

    val baseDir = File("build/tomcat").apply { mkdirs() }
    tomcat.setBaseDir(baseDir.absolutePath)

    val context = tomcat.addContext("", File(".").absolutePath)

    val servlet = DispatcherServlet(springCtx)
    val servletName = "dispatcher"
    Tomcat.addServlet(context, servletName, servlet).setLoadOnStartup(1)
    context.addServletMappingDecoded("/", servletName)

    tomcat.start()
    println("SpotYourStats running on http://localhost:$port")
    tomcat.server.await()
}

private fun requireEnv(key: String): String =
    System.getenv(key) ?: error("Missing required env var: $key")
