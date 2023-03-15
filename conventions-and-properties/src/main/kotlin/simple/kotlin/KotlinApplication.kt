package simple.kotlin

import java.time.Instant
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


fun main() {


    data class LineItem(val sku: String)
    data class Cart(
        val username: String,
        val lineItems: MutableList<LineItem> = mutableListOf()
    )

    operator fun Cart.plusAssign(li: LineItem) {
        this.lineItems.add(li)
    }

    val cart = Cart("jlong")
    cart += LineItem("1")
    cart += LineItem("2")
    println(cart.lineItems)

    val (username, lineItems) = cart
    println("username=$username")
    println("lineitems=$lineItems")

    class Customer(
        private val name: String,
        private val age: Int
    ) {
        operator fun component1() = this.name
        operator fun component2() = this.age
    }

    val customer = Customer("Josh", 39)
    val (_, age) = customer
    println("the age is $age  ")


    class Foo {

        private var name: String = ""
            get() = field
            set(newValue: String) {
                println("setting name to ${newValue}")
                field = newValue
            }

        init {
            println("the name is $name ")
            name = "Bob"
            println("the name now equals $name ")
        }


    }

    val foo = Foo()

    class MyDelegateThingy {
        operator fun getValue(value: ValueProducer?, property: KProperty<*>): Int = 1

        operator fun setValue(value: ValueProducer?, property: KProperty<*>, s: Int) {

        }
    }

    class Container : ValueProducer {

        var name: Int by MyDelegateThingy()
    }

    val expensiveComputation: Int by lazy {
        Thread.sleep(1000)
        42
    }
    println("${Instant.now()} the expensive computation is $expensiveComputation")
    println("${Instant.now()} the expensive computation is $expensiveComputation")

    var myObservableProperty by Delegates
        .observable("Anonymous") { kProperty, old, new -> println("changing ${kProperty.name} from $old to $new ") }
    myObservableProperty = "jlong"
    println(myObservableProperty)

}

interface ValueProducer
