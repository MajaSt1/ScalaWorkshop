package lectures.basics.part2oop

import scala.annotation.targetName
import scala.language.postfixOps

object MethodNotations extends App {

  class Person(val name: String, favoriteMovie: String, val age: Int = 0) {
    def likes(movie: String): Boolean = movie == favoriteMovie

    def +(person: Person): String = s"${this.name} is hanging out with ${person.name}"

    def +(nickname: String): Person = new Person(s"$name ($nickname)", favoriteMovie)

    def unary_! : String = s"$name, what the heck?!"

    def unary_+ : Person = new Person(name, favoriteMovie, age.+(1)) // or this.age + 1

    def isAlive: Boolean = true

    def apply(): String = s"Hi, my name is $name and I like $favoriteMovie"

    def apply(number: Int): String = s"$name watched $favoriteMovie $number times"

    def learns(subject: String): String = s"$name learns $subject"

    def learnsScala: String = this learns "Scala"
  }

  val mary = new Person("Mary", "Inception")

  // infix notation = operator notation (syntactic sugar) => for method with one parameter
  println(mary.likes("Inception"))
  println(mary likes "Inception") // equivalent

  val tom = new Person("Tom", "Fight Club")

  // "operators" in Scala
  println(mary + tom)
  println(mary.+(tom))

  println(1 + 2)
  println(1.+(2))
  // ALL OPERATORS ARE METHODS.
  // Akka actors have ! ? - asynchronous actors

  // prefix notation
  // unary_ prefix only works with few operators: - + ~ !
  val x = -1 // equivalent with 1.unary_-
  val y = 1.unary_-

  println(!mary)
  println(mary.unary_!)

  //postfix notation - only available with function that has no parameters
  println(mary.isAlive)
  println(mary isAlive)

  // apply is special !!
  println(mary.apply())
  println(mary()) // equivalent

  // EXERCISES
  println((mary + "the rockstar") ())
  println((mary + "the rockstar").apply())

  println((+mary).age) // must be () !!!
  println(mary.unary_+.age)

  println(mary.learnsScala)
  println(mary learnsScala)

  println(mary(2))
  println(mary apply 2)
}
