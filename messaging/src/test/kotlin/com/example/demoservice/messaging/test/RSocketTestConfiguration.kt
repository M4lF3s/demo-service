package com.example.demoservice.messaging.test

import com.example.demoservice.core.demoInteractors.DemoService
import io.rsocket.RSocket
import io.rsocket.RSocketFactory
import io.rsocket.frame.decoder.PayloadDecoder
import io.rsocket.transport.netty.client.TcpClientTransport
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.support.GenericApplicationContext
import org.springframework.context.support.beans
import org.springframework.messaging.rsocket.RSocketRequester
import org.springframework.messaging.rsocket.RSocketStrategies
import org.springframework.util.MimeTypeUtils


@SpringBootApplication
@ComponentScan(basePackages = ["com.example.demoservice.messaging"])
class RSocketTestConfiguration : ApplicationContextInitializer<GenericApplicationContext> {
    override fun initialize(context: GenericApplicationContext) {
        beans(context).initialize(context)
    }

    fun beans(context : ApplicationContext) = beans {
        bean<RSocket> {
            val rSocket : RSocket = RSocketFactory
                    .connect()
                    .dataMimeType(MimeTypeUtils.APPLICATION_JSON_VALUE)
                    .frameDecoder(PayloadDecoder.ZERO_COPY)
                    .transport(TcpClientTransport.create(7000))
                    .start()
                    .block()!!
            rSocket
        }

        bean<RSocketRequester> {
            RSocketRequester.wrap(ref<RSocket>(), MimeTypeUtils.APPLICATION_JSON, ref<RSocketStrategies>())
        }
    }
}