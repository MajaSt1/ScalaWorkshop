package lectures.advanced.part1as

import scala.annotation.targetName
import scala.util.Try

object DarkSugars extends App {

  //syntax sugar #1: methods with single param allow {}
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // write some complex code
    42
  }

  val aTryInstance = Try { // java's try {...}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }

  // syntax sugar #2: single abstract method - types with single abstract methods can be reduced to lambdas
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1 //magic !!!!!!!

  // example: Runnables -> instances of a traits, can be passed on to threads
  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hello, Scala")
  })

  val aSweeterThread = new Thread(() => println("sweet, Scala!"))

  abstract class AnAbstractType {
    def implemented: Int = 23

    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // syntax sugar #3: the :: and #:: methods are special
  // Methods with `:` are special
  // - right-associative if ending in `:`
  // - left-associative otherwise
  val prependedList = 2 :: List(3, 4)
  // 2.::(List(3,4))
  // List(3,4).::(2)
  // ?!

  // scala spec: last char decides associativity of method !!!
  var extendedList = 1 :: 2 :: 3 :: List(4, 5)
  extendedList = List(4, 5).::(3).::(2).::(1)

  class MyStream[T] {
    def -->:(value: T): MyStream[T] = this // actual implementation here
  }

  val myStream = 1 -->: 2 -->: 3 -->: new MyStream[Int]

  // syntax sugar #4: multi-word method naming
  class TeenGirl(name: String) {
    def `and then said`(gossip: String) = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")
  lilly `and then said` "Scala is so sweet!"

  // syntax sugar #5: infix types as the same way we infix method types
  class Composite[A, B]

  val composite: Int Composite String = ???

  class -->[A, B]

  val towards: Int --> String = ???

  trait <[A, B]
  val lessThan: Int < String = ???

  // syntax sugar #6: update() is very special, much like apply()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten to anArray.update(2,7)
  // used in mutable collections
  // remember apply() AND update()!

  // syntax sugar #7: setters for mutable containers
  class Mutable {
    // priv shortcut for println()!!!
    private var internalMember: Int = 0

    def member = internalMember // getter

    def member_=(value: Int): Unit =
      internalMember = value // setter (=! equal in the name is important here)
  }

  val aMutableCOntainer = new Mutable
  aMutableCOntainer.member = 42 // rewritten as aMutableContainer.member_=(42)
}
