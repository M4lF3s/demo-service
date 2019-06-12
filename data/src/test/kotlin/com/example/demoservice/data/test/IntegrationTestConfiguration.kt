package com.example.demoservice.data.test

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.support.GenericApplicationContext
import org.testcontainers.containers.PostgreSQLContainer

class KPostgreSQLContainer : PostgreSQLContainer<KPostgreSQLContainer>()

@SpringBootApplication
@ComponentScan(basePackages = ["com.example.demoservice.data"])
class IntegrationTestConfiguration : ApplicationContextInitializer<GenericApplicationContext> {


    override fun initialize(context: GenericApplicationContext) {
        beans(context).initialize(context)
    }

    fun beans(context : ApplicationContext) = org.springframework.context.support.beans {

        bean<KPostgreSQLContainer> {
            val postgres = KPostgreSQLContainer()
                    .withDatabaseName(context.environment.getProperty("app.postgres.databaseName", "app"))
                    .withUsername(context.environment.getProperty("app.postgres.userName", "app"))
                    .withPassword(context.environment.getProperty("app.postgres.password", "app"))

            postgres.start()

            TestPropertyValues.of(
                    "app.postgres.port=${postgres.getMappedPort(5432)}"
            ).applyTo(context as ConfigurableApplicationContext)

            postgres
        }

        bean<ConnectionFactory> {
            PostgresqlConnectionFactory(
                    PostgresqlConnectionConfiguration.builder()
                            .database(context.environment.getProperty("app.postgres.databaseName", "app"))
                            .password(context.environment.getProperty("app.postgres.password", "app"))
                            .username(context.environment.getProperty("app.postgres.userName", "app"))
                            .host("localhost")
                            .port(ref<KPostgreSQLContainer>().getMappedPort(5432))
                            .build()
            )
        }


    }

}