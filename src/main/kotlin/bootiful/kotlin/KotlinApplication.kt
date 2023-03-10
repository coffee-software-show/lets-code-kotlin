package bootiful.kotlin

fun doSomething() {
    println("Hello".joshTransform())
}

fun String.joshTransform(
    transformer: (String) -> String = { input -> input.uppercase() }
): String {
    return transformer(this)
}

fun main() {


    println("Josh".joshTransform())
    println("Josh".joshTransform { str -> str.reversed() })


}


