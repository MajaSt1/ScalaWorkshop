package lectures.part1basics

object CBNvsCBV extends App {

  def calledByValue(x: Long): Unit = { // function with side effects
    println("by value " + x)
    println("by value " + x)
  }

  // expression is evaluated every time (call it by name)
  def calledByName(x: => Long): Unit = {
    println("by name " + x) // println("by name " + System.nanoTime())
    println("by name " + x) // println("by name " + System.nanoTime())
  }

  calledByValue(System.nanoTime())
  calledByName(System.nanoTime())

  // nanotime caunts since 1970

  def infinite(): Int = 1 + infinite()

  def printFirst(x: Int, y: => Int) = println(x)

  //  printFirst(infinite(), 34)
  // call by name is lazy evaluated !!
  printFirst(34, infinite())


  // Takeaways:
  /* Call by value:
    - value is computed before call !!
    - same value used everywhere

    Call by name:
    - expression is passed literally
    - expression is evaluated at every use within (which means is lazy)
    */
}
