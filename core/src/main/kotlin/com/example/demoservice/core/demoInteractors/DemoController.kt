package com.example.demoservice.core.demoInteractors

import com.example.demoservice.core.demoGateways.DemoDataGateway

interface DemoController {

    @DslMarker
    annotation class BuilderDsl

    @BuilderDsl
    data class Builder(
            var dataGateway : DemoDataGateway? = null
    ) {
        fun dataGateway(dataGateway: DemoDataGateway?) = apply { this.dataGateway = dataGateway }
        fun build() : DemoService = DemoService(this.dataGateway!!) // TODO: Find alternative for the non-null Assertion here
    }

    val dataGateway : DemoDataGateway
}