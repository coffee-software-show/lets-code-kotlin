package bootiful.reactive

import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.data.annotation.Id
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.coRouter

@SpringBootApplication
class ReactiveApplication

fun main(args: Array<String>) {
    runApplication<ReactiveApplication>(*args) {
        addInitializers(beans {
            bean {
                val repo = ref<CustomerRepository>()
                coRouter {
                    GET("/customers") {
                        val customers = repo.findAll().asFlow()
                        ServerResponse.ok().bodyAndAwait(customers)
                    }
                }
            }
        })
    }
}

@RestController
class CustomerHttpController(private val repo: CustomerRepository) {

    @GetMapping("/customers/{id}")
    suspend fun customersById(@PathVariable id: Int): Customer {
        val customer = this.repo.findById(id).awaitSingle()
        println("the id is ${customer.id} and the name is ${customer.name}")
        return customer
    }
}

data class Customer(@Id val id: Int, val name: String)

interface CustomerRepository : ReactiveCrudRepository<Customer, Int>
