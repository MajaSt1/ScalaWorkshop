package lectures.part3fp

// instantiating a function is still much tied to oop way
object AnonymousFunctions extends App {

  // oop way:
  val doubler = new Function[Int, Int] :
    override def apply(x: Int): Int = x * 2

  // syntactic sugar in Scala => anonymous function (LAMBDA)
  // val properDoubler: Int => Int = (x: Int) => x * 2
  val properDoubler: Int => Int = x => x * 2
  //val properDoubler: = x => x * 2 -> will not compile, specify type!

  // multiple params in a lambda
  val adder: (Int, Int) => Int = (a: Int, b: Int) => a + b
  val adder1: (Int, Int) => Int = (a, b) => a + b

  // no params
  val justDoSomething: () => Int = () => 3
  val justDoSomething1 = () => 3

  println(justDoSomething) // function itself- prints instance of anonymous function
  println(justDoSomething()) // call

  // curly braces with lambdas
  val stringToInt = { (str: String) =>
    str.toInt
  }

  // MORE syntactic sugar - underscores
  val niceIncrementer: Int => Int = _ + 1 // equivalent to x => x + 1
  val niceAdder: (Int, Int) => Int = _ + _ // equivalent to (a,b) => a + b


  /*
  1. MyList : replace all FunctionX calls with lambdas
  2. Rewrite the "special" adder as an anonymous function
  */
}
