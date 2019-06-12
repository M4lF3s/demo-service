package com.example.demoservice.data.test


import com.example.demoservice.data.DemoImpl
import com.example.demoservice.data.DemoRepository
import io.r2dbc.spi.ConnectionFactory
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.core.env.get
import org.springframework.test.context.ContextConfiguration
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.io.File


@SpringBootTest
@ContextConfiguration(initializers = [IntegrationTestConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@Tag("integrationTest")
class FunctionalTest(
        @Autowired val postgres : KPostgreSQLContainer,
        @Autowired val context : ApplicationContext,
        @Autowired val connectionFactory : ConnectionFactory,
        @Autowired val repo : DemoRepository

) {

    @BeforeAll
    fun setup() {

        Assertions.assertTrue(postgres.isRunning)

        val connection = Mono.from(connectionFactory.create())

        connection
                .flatMapMany {
                    it.createStatement("drop table if exists demo").execute()
                }
                .flatMap { r -> r.rowsUpdated }
                .`as` {
                    StepVerifier.create(it)
                }
                .verifyComplete()

        connection
                .flatMapMany {
                    it.createStatement("create table demo(id text PRIMARY KEY, test text)").execute()
                }
                .flatMap { r -> r.rowsUpdated }
                .`as` {
                    StepVerifier.create(it)
                }
                .verifyComplete()

    }

    @Test
    @Order(1)
    fun `Reading Connection Props from File`() {
        File(this::class.java.getResource("/application.properties").file).useLines {
            it.map {
                Assertions.assertEquals(it.split("=")[1], context.environment[it.split("=")[0]])
            }
        }
    }

    @Test
    @Order(2)
    fun `Writing Entries to Database`() {

        Flux.just("first", "second", "third")
                .map { DemoImpl(test = it) }
                .flatMap { repo.writeEntry(it) }
                .`as` { StepVerifier.create(it) }
                .expectNextCount(3)
                .verifyComplete()

    }

    @Test
    @Order(3)
    fun `Reading Entries from Database`() {
        repo.allEntries()
                .`as` { StepVerifier.create(it) }
                .expectNextCount(3)
                .verifyComplete()

    }

}



