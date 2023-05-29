package lectures.basics.part4pm

object BracelessSyntax extends App {

  // if - expression
  val anyIfExpression = if (2 > 3) "bigger" else "smaller"

  // java style
  val anyIfExpression_v2 =
    if (2 > 3) {
      "bigger"
    } else {
      "smaller"
    }

  // compact style
  val anyIfExpression_v3 =
    if (2 > 3) "bigger"
    else "smaller"

  // scala 3
  val anyIfExpression_v4 =
    if 2 > 3 then
      "bigger" // higher indentation than the if part
    else
      "smaller"

  val anyIfExpression_v5 =
    if 2 > 3 then
      val result = "bigger"
      result
    else
      val result = "smaller"
      result

  // scala 3 one-liner
  val anyIfExpression_v6 = if 2 > 3 then "bigger" else "smaller"


  // for-comprehension
  val aForComprehension = for {
    n <- List(1, 2, 3)
    s <- List("black", "white")
  } yield s"$n$s"

  // scala 3
  val aForComprehension_v2 =
    for
      n <- List(1, 2, 3)
      s <- List("black", "white")
    yield s"$n$s"


  //pattern matching
  // scala 3
  val meaningOfLife = 42
  val aPatternMatch_v2 = meaningOfLife match
    case 1 => "the one"
    case 2 => "the second"
    case _ => ""


  // methods without braces
  def computeMeaningOfLife(arg: Int): Int =
    val partialResult = 40

    partialResult + 2


  // class definition with significant indentation (same for traits, objects, enums etc.)
  class Animal: // compiler expects the body of Animal
    def eat(): Unit =
      println("I'm eating")
    end eat

    def grow(): Unit =
      println("I'm growing")

    // 3000 more lines of codes
  end Animal // for if, match, for, methods, classes, traits, enums, objects


  // anonymous classes
  val aSpecialAnimal = new Animal :
    override def eat(): Unit = println("I'm special")


  // indentation = strictly larger indentation
  // 3 spaces + 2 tabs > 2 spaces + 2 tabs
  // 3 spaces + 2 tabs > 3 spaces + 1 tab
  // 3 tabs + 2 spaces ??? 2 tabs + 3 spaces


  println(anyIfExpression_v5)
  println(computeMeaningOfLife(78))
}
