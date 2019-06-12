package com.example.demoservice.application

import com.example.demoservice.application.beans
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.support.GenericApplicationContext

@SpringBootApplication
class DemoServiceApplication

fun main(args: Array<String>) {
    SpringApplicationBuilder()
            .sources(DemoServiceApplication::class.java)
            .initializers(
                    ApplicationContextInitializer<GenericApplicationContext>{
                        beans(it).initialize(it)
                    }
            )
            .run(*args)
}


