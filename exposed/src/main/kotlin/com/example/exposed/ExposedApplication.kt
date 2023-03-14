package com.example.exposed

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.aot.hint.MemberCategory
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.TypeReference
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@SpringBootApplication
@ImportRuntimeHints(Hints::class)
class ExposedApplication

class Hints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        setOf(
            java.util.Collections::class,
            Column::class,
            Database::class,
            DdlAware::class,
            Expression::class,
            ExpressionWithColumnType::class,
            ForeignKeyConstraint::class,
            IColumnType::class,
            QueryBuilder::class,
            Table::class,
            Transaction::class,
            TransactionManager::class,
            Column::class,
            Database::class,
            kotlin.jvm.functions.Function0::class,
            kotlin.jvm.functions.Function1::class,
            kotlin.jvm.functions.Function2::class,
            kotlin.jvm.functions.Function3::class,
            kotlin.jvm.functions.Function4::class,
            kotlin.jvm.functions.Function5::class,
            kotlin.jvm.functions.Function6::class,
            kotlin.jvm.functions.Function7::class,
            kotlin.jvm.functions.Function8::class,
            kotlin.jvm.functions.Function9::class,
            kotlin.jvm.functions.Function10::class,
            kotlin.jvm.functions.Function11::class,
            kotlin.jvm.functions.Function12::class,
            kotlin.jvm.functions.Function13::class,
            kotlin.jvm.functions.Function14::class,
            kotlin.jvm.functions.Function15::class,
            kotlin.jvm.functions.Function16::class,
            kotlin.jvm.functions.Function17::class,
            kotlin.jvm.functions.Function18::class,
            kotlin.jvm.functions.Function19::class,
            kotlin.jvm.functions.Function20::class,
            kotlin.jvm.functions.Function21::class,
            kotlin.jvm.functions.Function22::class,
            kotlin.jvm.functions.FunctionN::class
        )
            .map { it.java }
            .forEach {
                hints.reflection().registerType(TypeReference.of(it.name), *MemberCategory.values())
            }
    }

}

fun main(args: Array<String>) {
    runApplication<ExposedApplication>(*args)
}

@Component
@Transactional
class Initializer : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        val collectionOfCustomers = Customers.selectAll().map { Customer(it[Customers.id], it[Customers.name]) }
        collectionOfCustomers.forEach { println(it) }
    }

}

data class Customer(val id: Int, val name: String)

object Customers : Table() {
    val id = integer("id").autoIncrement()  // Column<String>
    val name = text("name")
    override val primaryKey = PrimaryKey(this.id)
}