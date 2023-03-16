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

