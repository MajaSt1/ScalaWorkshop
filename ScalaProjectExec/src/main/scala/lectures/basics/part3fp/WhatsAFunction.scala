package lectures.basics.part3fp

object WhatsAFunction extends App {

  // DREAM: use functions as first elements
  // problem: oop world -> we can simulate functional programming by using objects (instances of classes)

  val doubler = new MyFunction[Int, Int] :
    override def apply(element: Int): Int = element * 2

  // !!! instance acts like function
  println(doubler(2))

  // function types = Function1[A,B]
  val stringToIntConverter = new Function[String, Int] :
    override def apply(v1: String): Int = v1.toInt

  println(stringToIntConverter("3") + 4)

  val adder: (Int, Int) => Int = new Function2[Int, Int, Int] :
    override def apply(v1: Int, v2: Int): Int = v1 + v2

  // Function types Function2[A, B, R] === (A,B) => R

  // ALL SCALA FUNCTIONS ARE OBJECTS (instances of classes)
  // function traits, up to 22 params:
  //  trait Function1[-A, +B] {
  //    def apply(element: A): B
  //  }
  // syntactic sugar function types:
  // Function2[Int, String, Int]
  // (Int, String) => Int


  /*
  * 1. function which takes 2 strings and concatenates them
    2. transform the MyPredicate and MyTransformer into function types
    3. define a function which takes an int and returns another function which takes an int an returns an int
      - what's the type of this function
      - how to do it
  * */

  //1.
  val concatenateStrings: (String, String) => String = (v1: String, v2: String) => v1 + v2

  //3. Function1[Int, Functin1[Int,Int]]
  val supperAdder: Function1[Int, Function1[Int, Int]] = new Function[Int, Function1[Int, Int]] :
    override def apply(x: Int): Function1[Int, Int] = new Function[Int, Int] {
      override def apply(y: Int): Int = x + y
    }

    // anonymous function + LAMBDA
    val specialAdder: (Int) => ((Int) => Int) = (x: Int) => (y: Int) => x + y

    val adder5: Int => Int = supperAdder(3)
    println(adder5(5))
    println(supperAdder(3)(4)) // curried function
    println("Anonymous function: " + specialAdder(3)(4))

  def returnAnotherFunction(v1: Int): MyFunction[Int, Int] = {
    println("First number is: " + v1)
    (element: Int) => {
      println("The second number is: " + element)
      element + v1
    }
  }

  //  def returnSomethingElse(v1: Int): MyFunction[Int, Int] = new MyFunction[Int, Int] :
  //    override def subtract(element: Int): Int = {
  //      element + v1
  //    }
  //
  //    override def apply(element: Int): Int = {
  //      element - v1
  //    }

  println("And the result is: " + returnAnotherFunction(6).apply(4))
  //  println(returnSomethingElse(6).subtract(2))
}

trait MyFunction[A, B] {
  def apply(element: A): B

  //  def subtract(element: A): B
}