package com.example.demoservice.messaging.test

import com.example.demoservice.core.demoInteractors.DemoService
import com.example.demoservice.messaging.DemoRequest
import com.example.demoservice.messaging.RSocketController
import com.ninjasquad.springmockk.MockkBean
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.test.context.ContextConfiguration


@SpringBootTest
@ContextConfiguration(initializers = [RSocketTestConfiguration::class])
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Tag("integrationTest")
class RSocketTest(
        @Autowired val requester : RSocketRequester
) {

    @MockkBean lateinit var service : DemoService
    @Autowired lateinit var controller : RSocketController

    @Test
    fun test() {
        requester.route("demo").data(DemoRequest()).retrieveMono(String::class.java).subscribe(::println)
    }
}