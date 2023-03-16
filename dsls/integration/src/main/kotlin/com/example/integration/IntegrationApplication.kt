package com.example.integration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.integration.dsl.integrationFlow
import org.springframework.integration.file.dsl.Files
import org.springframework.integration.file.transformer.FileToStringTransformer
import java.io.File

@SpringBootApplication
class IntegrationApplication

fun main(args: Array<String>) {
    runApplication<IntegrationApplication>(*args) {
        addInitializers(beans {
            bean {
                integrationFlow(
                    Files.inboundAdapter(File("/Users/jlong/Desktop/in")),
                    { poller { it.fixedDelay(1000) } }
                ) {
                    transform(FileToStringTransformer())
                    transform<String> { it.uppercase() }
                    handle {
                        println("new message: ${it.payload}")
                    }
                }
            }
        })
    }
}

