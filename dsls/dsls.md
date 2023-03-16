# Kotlin Across the Wide and Wonderful World of Springdom 

Kotlin is a wonderful lang8age that makes it trivial to take old Java libraries and make them just that much more concise, just by virtue of the Kotlin syntax itself. it really shines, however, when you write DSLs.

Here's some inside baseball for you: the Spring teams do their level headed best to be cohesive, to align on core themes, to make Spring better than the sum of its parts. You see this in every major release: XML namespaces in Spring Framework 2.0. Java Config in 3.0. Conditionals and autoconfigurations when Spring Boot 1.0 first shipped along side Spring Framework 4.0. Reactive programming with Spring Framework 5.0. And of course ahead-of-time compilation in Spring Framework 6.0. And, obviously, whenever the baseline revisions of platform specifications like Java or Jakarta EE change, so too do the minimums for all the projects building on the corresponding Spring Framework release. But not with Kotlin. It's one of those things that sort of grew organically. There was no mandate from on high. It started in Spring Framework, and different teams, when they saw the opportunity, added appropriate support to their respective projects, when they could, and often in tandem with the community. Kotlin's awesome. 

Kotlin has a number of fgeatures that make it easy to build DSLs:

 * functions that accept lambdsa can accept the lambas _outside_ of the parenthesis for the funciton invocation'
 * if the only argument expected fort the function happens to be a lambda, there's no need to specify the parenthesis at all
 * DSLs may be written in such a way that the `this` reference - the _reciever_ - of the lambda can point to an arbitrary context object of the framework's choosing. So rather than having all DSLs look like this: `{ context -> context.a() } ` we can instead just write `{ a() }`.
 * extension functions are a typesafe way to add new functions to existing types, without havint to change the source code for thos types. This mweans types that work one way in Java can have alternative extended behavior in Kotlin. 


In this blog, I want ot introduce some exapoles of DSLs acros the wide and wonderful world of Springdom, highlighting some (but not all!) of my favorite DSLs.  The code for all these examples, and the corresponding Kotlin-language Gradle build files, [is here](https://github.com/coffee-software-show/lets-code-kotlin), if you want to follow along at home. Inspect the `dsls` folder for the examples we'll look at in this blog. 

Let's dive right in.

## Spring Framework Functional Bean Registrations

We introduced functional bean registration in Spring Framework 5.0, way back in 2017.  It's a way to programatically register beans with the Spring Framework in an `ApplicationContextInitializer`. It sidesteps some of the reflection and component scaning required for Javca configuration. We quite like the approach, and indeed, when you use Spring's GraalVM native image support, we _transpile_, sort of, your `@Configuration` Java configuration classes into functional bean registrations before feeding the whole thing to the GraalVM native image compiler. It's a nice DSL, but I love the way it pulls together when using Kotlin. I dont' have a standalone example of this in the sample code, but in most of the examples I use the functional style so I want to get it out of the way: 


```kotlin
package com.example.beans

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

@SpringBootApplication
class FunctionalBeanRegistrationApplication

fun main(args: Array<String>) {
    runApplication<FunctionalBeanRegistrationApplication>(*args) {
        addInitializers(beans {
            bean {
            	val db = ref<javax.sql.DataSource>()
                CustomerService(db)
            }
        })
    }
}
```

There are a few other niceties there, too: notice that when using Spring Boot, you're not using the normal `SpringApplication.run(Class, String[] args)`, but are instead using `runApplication`. The last parameter of `runApplication` is a lambda that has as its receive a reference to the `GenericApplicationContext` that gets created when calling `SpringApplication#run`. This gives us a chance to postprocess the `GenericApplicationContext` and to call `addInitializers`. 

Then, we use the convenient `beans` DSL, rather than writing an implementation of `ApplicationContextInitializer<GenericApplicationContext>` ourselves. 

Note also that we can use the `ref` method and the reified generics for the bean type to look up and inject another bean (of type `javax.sql.DataSource`). 

Barfe in mind that Sprihng doesn't care how you furnish your bean definitinos: use XML, Java Configuratin, component scanning, fucntinal bean registration, etc., and Spring is happy eiother way. You can also all of them in the sample application, from Java or Kotlin. Again, it doesn't matter: they all end up as canonicalized `BeanDefinition`s that then get wired together to form the final, running application. So you can mix and mathc. I often do!

## Functional HTTP Endpoints with Spring MVC and Spring Webflux 


Everybody knows Spring's `@Controller` abstraction, but many other frameworks support an alternative syntax, a la Ruby's Sinatra, where a lambda is associated with a predicate describing how to match an incming request. Spring finally got one in Spring Framework 5. The DSL in Java is super concise, but it's eve nicer in Kotlin. There are implementations of htis functional endpoint style fort both Spring MVC _and_ Sprihng Webflux. The MVC implementation came later, though, so some folks may not have tried it out yet. 

```kotlin
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
```

Pretty straightforward: when an HTTP `GET` request arrives, produce a response, which in this case is a `Map<String,String>`. Spring MVC will in turn serialize thart, just as if you had returned a `Map<String,String>` from a Spring MVC `@Controller` handler method. Nice!


## Corouitines 

Coroutinbes are one of the most powerful ways to describe scalable, concurrent coe in Kotklin without muddying the code with chains of invocations (à la Promises in Javascript or `Publisher<T>s` in Reactor), or callbacks, or the like. If you're using the reactive stack in Spring, then youre alreadys et to use coroutines, as we've worked to make it so that everywhere you would've used a reactive type can also be `await-ed`. You really jut need to see it to believe it: 


```kotlin

```

## Spring Security 

This example looks at the custom Spring Security DSL. 

```kotlin
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


```


The example uses functional bean registration. Most of this is familiar, what may be novel is the fact that we're using the injected `HttpSecurity` reference and implicitly calling an extension method, `invoke` , that gives us a DSL in which we can configure thigns like the fact that we want HTTP BASIC, we want to authorize certain endpoints, etc. We're defining a bean, so we need to return a value.  

Very convenient! 

## Spring Data MongoDB Type Safe Queries

There are countless third party data access libraries out there that ship with an annotatiohn processor that performs code generation so that you can access your domain model in a typesafe fashion, with checks guaranteed by the compiler. IUn Kotlin, its possible to do a lot of htat without an extra tool beyond the Kotlin compiler and language, too. 


Here;s a simple exampel that wrirtes some data to the databas, then queries it using Kotlin's field referenc mechanisdm: 


```kotlin
package com.example.mongodb

import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.MongoOperations
import org.springframework.data.mongodb.core.find
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.isEqualTo
import org.springframework.data.repository.CrudRepository

@SpringBootApplication
class MongodbApplication

fun main(args: Array<String>) {
    runApplication<MongodbApplication>(*args)
}

@Configuration
class TypeSafeQueryExampleConfiguration {

    @Bean
    fun runner(cr: CustomerRepository, mongoOperations: MongoOperations) = ApplicationRunner {
        cr.deleteAll()
        cr.save(Customer(null, "A"))
        cr.save(Customer(null, "B"))
        cr.findAll().forEach {
            println(it)
        }
        val customers: List<Customer> = mongoOperations.find<Customer>(
            Query(Customer::name isEqualTo "B")
        )
        println(customers)
    }
}

data class Customer(@Id val id: String?, val name: String)

interface CustomerRepository : CrudRepository<Customer, String>


```



It's a typical application otherwise: we have a Spring Data repository, an entity, etc. We even use one of Spring's well known `\*Template` variants! The only thing exceptional here is that query in the `find()` call where we say `Customer::name isEqualTo "B"`. 


## Go with the Flow with Spring Integration 

Spring INtegration is one of the odlest SPring projects and offers a fit for pirpopsde way to describe integratin pipelines - we call them _flows_ - to act on events (we model them as `Mesasage<T>`s). Th3e pipelines can have many operations, each chained together. There's a lovely `IntegrationFlow` DSL that uses context objrects to provide the DSL. But, for me at least, it just feels that muhch cleaner wehen expressed in Kotlin. 

```kotlin
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


```

Does this inbound flow make sense to you? It says: scan teh directory (my compiuter's `$HOME/Desktop/in` folder) every 1000 milliseconds (a second), and when there's a new `java.io.File` detected, pass it to the `transform` operation which will turn the `File` into a `String`. The `String` is then sent to the next `transform` operation which uppercases the text. That uppercased texrt is then sent to the last operation, `handle` , wher I print ou tthe uppercased text. 

## Easy Microproxies with Spring Cloud Gateway 

Spring Cloud Gateway is ne of my favvorite Spring Cloud modules. It makes it trivall to handle cross cutting concertns at the HTTP abnd service level. Theres also integration for things like GRPC and wesockets. It's pretty easy to understand: you us the `RouteLocatorBuilder` to define `routes` which in turn have preicates that match incvoming requests. If they're matched, then you can apply zero or more filters to the reequest before sending it onward to the final `uri` you specify. It's a functrional piopeline, so it shouldnt be surprising it expresses nicely in a Kotlin DSL. Let's look at an example. 


```kotlin

package com.example.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder
import org.springframework.cloud.gateway.route.builder.filters
import org.springframework.cloud.gateway.route.builder.routes
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

@SpringBootApplication
class GatewayApplication

fun main(args: Array<String>) {
    runApplication<GatewayApplication>(*args)
}

@Configuration
class GatewayConfiguration {

    @Bean
    fun gateway(rlb: RouteLocatorBuilder) = rlb
        .routes {
            route {
                path("/proxy")
                filters {
                    setPath("/bin/astro.php")
                    addResponseHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*")
                }
                uri("https://www.7timer.info/")
            }
        }
}
```

This example martches request bound for localhost:8080/proxy and forwards the request on to a randojm open HTTP webservice I fouind on the internet that is supposed to give you weater reports. I use the filter to augment thte respond, ading some custom headers, `ACCESS_CONTROL_ALLOW_ORIGIN`, to the response. Try it out in the browser, as i think tye defgault response with out any of the parameters is some binary data - an image. 

