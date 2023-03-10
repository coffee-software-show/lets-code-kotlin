package bootiful.kotlin


fun main() {

    fun transform(
        string: String,
        counter: (String) -> Int
    ): String {
        return "there are ${counter(string)}"
    }

    transform("Josh", { str -> str.length })
    transform("Josh") { str -> str.length }

    fun foo(myFunc: (String) -> Int) {
        // todo use myFunc
    }
    foo { name -> name.length }
    foo() { name -> name.length }
    foo({ name -> name.length })

//    fun reverse (string :String ) = string.reversed()
    val reverse: (String, Int) -> String = { name, num -> name.reversed() }
    val name: String = "Josh"
    val age = 39
    println("the name is $name and the age is $age")

}

