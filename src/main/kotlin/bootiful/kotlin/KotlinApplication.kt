package bootiful.kotlin

import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import java.sql.ResultSet
import javax.sql.DataSource

fun initSchema(jt: JdbcTemplate, classpathResourceName: String) {
    val file = ClassPathResource(classpathResourceName).file
    with(file.reader()) {
        val lines = this.readText().split(";")
        lines.forEach { jt.execute(it) }
    }
}

suspend fun main() {
    data class Customer(val id: Int, val name: String)
    val db: DataSource = EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).build()
    val jdbc = JdbcTemplate(db)
        .apply { afterPropertiesSet() }
    initSchema(jdbc, "/schema.sql")
    initSchema(jdbc, "/data.sql")
    val lambda: (ResultSet, Int) -> Customer = { rs, _ -> Customer(rs.getInt("id"), rs.getString("name")) }
    val customers = jdbc.query<Customer>("select * from customers", lambda)
    println(customers)
}




