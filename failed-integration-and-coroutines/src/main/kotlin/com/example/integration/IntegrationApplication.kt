package com.example.integration

import kotlinx.coroutines.reactive.awaitSingle
import org.reactivestreams.Publisher
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.integration.dsl.IntegrationFlow
import org.springframework.integration.file.dsl.Files
import org.springframework.messaging.Message
import org.springframework.util.SystemPropertyUtils
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.coRouter
import java.io.File

@SpringBootApplication
class IntegrationApplication

fun main(args: Array<String>) {
    runApplication<IntegrationApplication>(*args)
}

@Configuration
class IntegrationConfiguration {

    @Bean
    fun routes() = coRouter {
        GET("/file") {
            val newFileName: Message<String> = flow().awaitSingle()
            println("did we get a message? ${newFileName.payload}")
            ServerResponse.ok().bodyValueAndAwait(newFileName)
        }
    }

    @Bean
    fun flow(): Publisher<Message<String>> =
        IntegrationFlow
            .from(Files.inboundAdapter(File(SystemPropertyUtils.resolvePlaceholders("\${HOME}/Desktop/in")))) { p ->
                p.poller { pm -> pm.fixedDelay(1000) }
            }
            .transform(File::class.java) { it.absolutePath }
            .toReactivePublisher<String>(true)


}