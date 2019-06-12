package com.example.demoservice.data

import com.example.demoservice.core.demoGateways.Demo
import com.example.demoservice.core.demoGateways.DemoDataGateway
import io.r2dbc.spi.Connection
import io.r2dbc.spi.ConnectionFactory
import io.r2dbc.spi.Result
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class DemoRepository(
        val connectionFactory : ConnectionFactory
) : DemoDataGateway {

    private fun connection() : Mono<Connection> {
        return Mono.from(connectionFactory.create())
    }

    override fun allEntries(): Flux<DemoImpl> {
        return this.connection()
                .flatMapMany {
                    Flux.from(it.createStatement("select * from demo").execute()) }
                .flatMap {
                    it.map {
                        row, _ ->
                        DemoImpl(row.get("id", String::class.java)!!, row.get("test", String::class.java)!!)
                    }
                }
    }

    override fun writeEntry(d : Demo) : Flux<DemoImpl> {
        val result : Flux<out Result> = this.connection()
                .flatMapMany {
                    it.createStatement("insert into demo(id, test) values ($1, $2)")
                            .bind("$1", d.id)
                            .bind("$2", d.test)
                            .add()
                            .execute()
                }
        return result.switchMap { Flux.just(DemoImpl(d.id, d.test)) }
    }

}