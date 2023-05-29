package lectures.basics.part1basics

object Expressions extends App {
  val x = 1 + 2 // EXPRESSION
  println(x)

  println(2 + 3 * 4)
  // + - * / & | ^ << >> >>> (right shift with zero extension)

  println(1 == x)
  // == != > >= < <=

  println(!(1 == x))
  // ! && ||

  var aVariable = 2
  aVariable += 3 // also works with -= *= /= .... side effects
  println(aVariable)

  // Instructions are executed (change variable, println etc.) - Imperative languages like Java, Python, C  vs Expressions are evaluated (VALUE AND/OR TYPE) !!! - in functional programming languagae

  val xIsEven = x % 2 == 0

  //IF expression
  val aCondition = true
  val aConditionedValue = if (aCondition) 5 else 3 // IF EXPRESSION - if gives a value
  println(aConditionedValue)
  println(if (aCondition) 5 else 3)
  println(1 + 3)

  var i = 0
  while (i < 10) {
    println(i)
    i += 1
  } // TODO: while is a side effects because it's return UNIT!!!!!!

  // NEVER WRITE THIS AGAIN - while and looping in general is specific to imperative programming
  // EVERYTHING IN SCALA IS AN EXPRESSION!
  val aWeirdValue = (aVariable = 3) // Unit === void
  println(aWeirdValue) // == ()

  // side effects (are reminiscent of Imperative programming! like instructions): println(), whiles, reassigning -> return VOID


  // Code blocks are expressions -> compiler says that this is a String (type of the last expression)
  val aCodeBlock = {
    val y = 2
    val z = y + 1

    if (z > 2) "hello" else "goodbye"
  }


}
