package lectures.advanced.part2as

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    42
  }
  println(x)
  println(x)

  // examples of implications:
  // #1: side effects
  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no") // Boo is not printed

  // #2: in conjunction with call by name
  def byNameMethod(n: => Int): Int = {
    // call by need
    lazy val t = n
    t + t + t + 1
  }

  def retrieveMagicValue = {
    println("waiting")
    Thread.sleep(1000)
    42
  }

  println(byNameMethod(retrieveMagicValue))
  // we are waiting 3 sec instead of 1
  // solution: use lazy vals
  // now we waiting 1 sec

  //#3 filtering with lazy vals
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 25, 40, 5, 23)
  val lt30 = numbers.filter(lessThan30) // List(1, 25, 5, 23)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30lazy = numbers.withFilter(lessThan30) // lazy vals under the hood
  val gt20lazy = lt30lazy.withFilter(greaterThan20)
  println("----------")
  //  println(gt20lazy) // println are not executed in methods
  gt20lazy.foreach(println)

  //for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy vals!
  } yield a + 1
  // translates to:
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
  * EXERCISE: implement a lazily evaluated, singly linked STREAM of elements

  naturals = MyStream.from(1)(x => x + 1) = stream of natural numbers (potentially infinite!)
  naturals.take(100).foreach(println) // lazily evaluated stream of the first 100 naturals (finite stream)
  naturals.foreach(println) // will crash - infinite!
  naturals.map(_ * 2) // stream of all even numbers (potentially infinite)
   */
}
