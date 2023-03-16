package bootiful.reactive

import kotlinx.coroutines.flow.Flow
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans
import org.springframework.data.annotation.Id
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
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
                        val customers : Flow<Customer> = repo.findAll()
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
        val customer:Customer = this.repo.findById(id) !!
        println("the id is ${customer.id} and the name is ${customer.name}")
        return customer
    }
}

data class Customer(@Id val id: Int, val name: String)

interface CustomerRepository : CoroutineCrudRepository<Customer, Int>
