package com.example.demoservice.core.demoGateways

import reactor.core.publisher.Flux

interface DemoDataGateway {

    fun allEntries(): Flux<out Demo>

    fun writeEntry(d : Demo) : Flux<out Demo>

}