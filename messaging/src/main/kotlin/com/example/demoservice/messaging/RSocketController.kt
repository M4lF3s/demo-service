package com.example.demoservice.messaging

import com.example.demoservice.core.demoInteractors.DemoService
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.stereotype.Controller


@Controller
class RSocketController(
        val service : DemoService
){

    @MessageMapping("demo")
    fun demo(request : DemoRequest) : String {
        return service.test()
    }
}