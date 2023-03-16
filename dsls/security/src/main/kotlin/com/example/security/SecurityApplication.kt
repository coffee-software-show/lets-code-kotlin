package com.example.security

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.core.userdetails.User
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
@EnableWebSecurity
class SecurityApplication

fun main(args: Array<String>) {
    runApplication<SecurityApplication>(*args) {
        addInitializers(beans {

            bean {
                val http = ref<HttpSecurity>()
                http {
                    httpBasic {}
                    authorizeRequests {
                        authorize("/hello/**", hasAuthority("ROLE_ADMIN"))
                    }
                }
                .run { http.build() }

            }

            bean {
                InMemoryUserDetailsManager(
                    User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("password")
                        .roles("ADMIN")
                        .build()
                )
            }

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

