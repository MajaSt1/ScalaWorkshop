package lectures.basics.part1basics

object StringOps extends App {

  val str: String = "Hello, I am learning Scala"

  // strings are indexed from 0
  println(str.charAt(2))
  println(str.substring(7, 11))
  println(str.split(" ").toList)
  println(str.startsWith("Hello"))
  println(str.replace(" ", "-"))
  println(str.toLowerCase())
  println(str.length)

  val aNumberString = "45"
  val aNumber = aNumberString.toInt
  println('a' +: aNumberString :+ 'z') // appending + prepending -> Scala specific
  println(str.reverse) // Scala specific
  println(str.take(2))

  // Scala-specific: String interpolators

  // s-interpolators
  val name = "David"
  val age = 12
  val greeting = s"Hello, my name is $name and I am $age years old"
  val anotherGreeting = s"Hello, my name is $name and I am ${age + 1} years old"

  // F-interpolators
  val speed = 1.2f
  // 2 characters total, minimum and 2 decimal precision
  val myth = f"$name%s can eat $speed%2.2f burgers per minute"

  // raw-interpolator - will print literally
  println(raw"This is a \n newline")
  val escaped = "This is a \n newline"
  println(raw"$escaped") // injected variables do get escapes!
}
