package com.example.demoservice.core.demoInteractors

import com.example.demoservice.core.demoGateways.DemoDataGateway

class DemoService(override val dataGateway: DemoDataGateway) : DemoController {

    companion object {
        inline fun build(buildDemoService: DemoController.Builder.() -> Unit) : DemoService {
            val builder = DemoController.Builder()
            builder.buildDemoService()
            return builder.build()
        }
    }

    fun test() : String {
        return "HelloWorld"
    }

}