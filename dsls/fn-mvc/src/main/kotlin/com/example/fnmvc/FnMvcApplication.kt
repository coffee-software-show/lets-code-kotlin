package com.example.fnmvc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
class FnMvcApplication

fun main(args: Array<String>) {
    runApplication<FnMvcApplication>(*args) {
        addInitializers(beans {
            bean {
                router {
                    GET("/hello") {
                        ServerResponse.ok().body(mapOf("greeting" to "Hello, world!"))
                    }
                }
            }
        })
    }
}