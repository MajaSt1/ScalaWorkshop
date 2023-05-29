package lectures.advanced.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description: Unit = numbers match
    case head :: Nil => println(s"the only element is $head")
    case _ =>

  /*
  - constants
  - wildcards
  - case classes
  - tuples
  - some special magic like above
  */

  // #1 how to do this class compatible with pattern matching? ( even though is not case class)
  class Person(val name: String, val age: Int)

  object Person {
    // person: Person -> the object we want to decompose
    // Option[(String, Int)] -> results as an Option or Option(tuple)
    def unapply(person: Person): Option[(String, Int)] =
      if (person.age < 21) None
      else Some((person.name, person.age))

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 25)

  // the compiler searches the appropriate unapply for us
  val greeting = bob match
    case Person(n, a) => s"Hi, my name is $n and I am $a yo."

  println(greeting)

  val legalStatus = bob.age match
    case Person(status) => s"My legal status is $status"

  println(legalStatus)

  /*
  Exercise. u can reused those pattern matching conditions !!!!!!!!!
  * */

  object even {
    def unapply(arg: Int): Boolean = arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg > -10 && arg < 10
  }

  val n: Int = 4
  val mathProperty = n match
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"

  println(mathProperty)

  // #2 infix patterns works only when you have 2 patterns
  numbers match
    case head :: Nil => println("single element" + head)
    //equivalent:
    case ::(head, Nil) => println("single element" + head)

  case class Or[A, B](a: A, b: B) // Scala: Either

  val either = Or(2, "two")
  val humanDescription = either match
    case number Or string => s"$number is written $string"

  println(humanDescription)

  // #3 decomposing sequences
  val vararg = numbers match
    case List(1, _*) => "starting with 1"

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  // !!!!!
  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  val decomposed = myList match
    case MyList(1, 2, _*) => "starting with 1, 2"
    case _ => "something else"

  println(decomposed)

  // #4 custom return types for unapply
  // isEmpty: Boolean, get: something.

  abstract class Wrapper[T] {
    def isEmpty: Boolean

    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      def isEmpty = false

      def get = person.name
    }
  }

  println(bob match
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "An alien"
  )
}
