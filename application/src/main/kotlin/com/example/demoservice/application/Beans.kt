package com.example.demoservice.application

import com.example.demoservice.core.demoInteractors.DemoService
import com.example.demoservice.data.DemoRepository
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.ApplicationContext
import org.springframework.context.support.beans

fun beans(context: ApplicationContext) = beans {

    bean<ConnectionFactory> {
        PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .database(context.environment.getProperty("app.postgres.databaseName", "app"))
                        .password(context.environment.getProperty("app.postgres.password", "app"))
                        .username(context.environment.getProperty("app.postgres.userName", "app"))
                        .host("localhost")
                        .port(context.environment.getProperty("app.postgres.userName", "5432").toInt())
                        .build()
        )
    }

    bean {
        DemoService.build {
            dataGateway(DemoRepository(ref<ConnectionFactory>()))
        }
    }
}