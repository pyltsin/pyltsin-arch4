package com.otus.arch4

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.core.env.Environment
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@SpringBootApplication
@EnableTransactionManagement
@EnableJdbcRepositories
class Arch4Application{

    @Autowired
    lateinit var env: Environment

    @Bean
    fun datasource(): DataSource {
        val uri = env.getProperty("DATABASE_URI", "jdbc:postgresql://localhost:5432/myapp")
        val password = env.getProperty("POSTGRES_PASSWORD", "passwd")
        val user = env.getProperty("POSTGRES_USER", "myuser")
        println(uri)
        println(password)
        println(user)
        val build = DataSourceBuilder.create()
                .url(uri)
                .password(password)
                .username(user)
                .build()
        return build
    }
}

fun main(args: Array<String>) {
    runApplication<Arch4Application>(*args)
}
