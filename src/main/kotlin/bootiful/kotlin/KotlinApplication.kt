package bootiful.kotlin

import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.core.queryForObject
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import javax.sql.DataSource


fun main() {


    data class Customer(val name: String, val age: Int)

    val db: DataSource = EmbeddedDatabaseBuilder().build()
    val jdbc = JdbcTemplate(db)
    val customer : Customer? = jdbc.queryForObject(
        "select *  from customers", Customer::class.java
    )
    val customersWithKotlin = jdbc.queryForObject<Customer> ( "select * from customers")


}




