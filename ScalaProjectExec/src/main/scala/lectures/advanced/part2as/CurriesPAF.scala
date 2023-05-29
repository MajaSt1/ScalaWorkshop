package lectures.advanced.part2as

object CurriesPAF extends App {

  // curried functions => function returning function
  val supperAdder: Int => Int => Int =
    x => y => x + y

  val add3 = supperAdder(3) // Int => Int = y => 3 + y
  println(add3(5))
  println(supperAdder(3)(5))

  // METHODs are not instances of function(x) themselves !!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION = technic that created functions out of methods
  // functions != methods (JVM limitation)
  def inc(x: Int) = x + 1 // method

  List(1, 2, 3).map(x => inc(x)) // ETA-expansion

  // Partial function applications
  val add5 = curriedAdder(5) _ // turn curriedAdder into a function after you apply 5 = Int => Int

  // EXERCISE
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y => 7 + y
  // as many different implementations of add7 using the above
  // be creative!

  val add7 = (x: Int) => simpleAddFunction(7, x) // simple solution
  val add7_2 = simpleAddFunction.curried(7)
  val add7_6 = simpleAddFunction(7, _: Int)

  val add7_3 = curriedAddMethod(7) _ // PAF = partially applied function
  val add7_4 = curriedAddMethod(7)(_) // PAF - alternative syntax
  val add7_5 = simpleAddMethod(7, _: Int) // alternative syntax for turing methods into function values
  // y => simpleAddMethod(7,y)

  // underscores are powerful
  def concatenator(a: String, b: String, c: String) = a + b + c

  val insertName: String => String = concatenator("Hello, I'm ", _: String, " how are you?")
  println(insertName("Maja"))

  val fillingTheBlanks = concatenator("Hello, ", _: String, _: String) //(x,y) => concatenator("Hello", x, y)
  println(fillingTheBlanks("Maja", " Scala!"))

  // EXERCISE
  /*
  * 1. Process a list of numbers and return their string representations with different formats
      Use the %4.2f, %8.6f and %14.12f with a curried formatter function.
  * */

  def formatter(f: String)(number: Double) = s"${f.format(number)}"

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)
  val simpleFormat = formatter("%4.2f") _ // lift
  val seriousFormat = formatter("%8.6f") _
  val preciseFormat = formatter("%14.12f") _
  numbers.foreach(x => println(preciseFormat(x)))
  //  numbers.foreach(_ => println(formatter("%14.12f")))

  /*
  * 2. difference between
    - functions vs methods
    - parameters: by-name vs 0-lambda
  * */

  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42
  /*
  * calling byName and byFunction
    - int
    - method
    - parenMethod
    - lambda
    - PAF
  * */

  byName(24) // ok
  byName(method) // ok
  byName(parenMethod()) // ok
  //  byName(parenMethod) does not compile methods with arg lists (even empty) called without paren
  //  byName(() => 42) // not ok => byName argument of value type is not the same as function parameter
  byName((() => 42) ()) // ok => because we actual called the function -> turned this expression into value
  //  byName(parenMethod _) // not ok => this is a function value

  //  byFunction(42) // not ok
  // byFunction(method) // not ok !!! - method is not converted to function value = ETA-expension
  byFunction(parenMethod) // ok - ETA-expansion!!
  byFunction(() => 46) //ok
  byFunction(parenMethod _) // also works - ETA-expansion

  // we inform compiler to do ETA-expansion by adding `_`!

  // PARTIAL function applications:
  // val add5 = curriedAdder(5) _
  // lifting methods to functions = ETA-expansion


}
