package lectures.basics.part2oop

import scala.annotation.tailrec

object Exceptions extends App {

  val x: String = null
  //  println(x.length)
  //  this ^^ will crash with NPE


  // throwing and catching exceptions
  //  val aWeirdValue: Nothing = throw new NullPointerException()

  // throwable classes extend the Throwable class.
  // Exception and Error are the major Throwable subtypes = crash jvm
  // Exception - something went wrong with the program
  // Errors - something went wrong in the system jvm i.e. StackOverflow - memory of the jvm stack

  //2. how to catch exceptions
  def getInt(withExceptions: Boolean): Int =
    if (withExceptions) throw new RuntimeException("No int for you!")
    else 42

  //expression
  val potentialFail: AnyVal = try {
    //code that might throw
    getInt(true)
  } catch {
    case _: RuntimeException => println("Caught a Runtime exception") // unit -> AnyVal
  } finally {
    //code that will get executed NO MATTER WHAT
    // optional
    // does not influence the return type of this expression
    // use finally only for side effects
    println("finally")
  }

  val potentialFailWithInt: Int = try {
    //code that might throw
    getInt(false)
  } catch {
    case _: RuntimeException => 43 // unit -> AnyVal
  } finally {
    //code that will get executed NO MATTER WHAT
    println("finally")
  }

  // 3. how to define your own exceptions
  //  class MyException extends Exception
  //  val exception = new MyException
  //
  //  throw exception

  /*
  1. Crash your program with OutOfMemoryError
  2. Crash with SOError
  3. PocketCalculator
    - add(x,y)
    - subtract(x,y)
    - multiply(x,y)
    - divide(x,y)

    Throw:
      - OverflowException if add(x,y) exceeds Int.MAX_VALUE
      - UnderflowException if subtract(x,y) exceeds Int.MIN_VALUE
      - MathCalculationException for division by 0
  * */

  // 1. OOM
  //  val array = Array.ofDim[Int](Int.MaxValue)

  // 2. SO
  //  def infiniteRecursiveMethod: Int = 1 + infiniteRecursiveMethod
  //  val noLimit = infiniteRecursiveMethod

  //3.
  class OverflowException extends RuntimeException

  class UnderFlowException extends RuntimeException

  class MathCalculationException extends RuntimeException("Division by 0!")

  object PocketCalculator {
    def add(x: Int, y: Int) = {
      val result = x + y

      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result > 0) throw new UnderFlowException
      else result
    }

    def subtract(x: Int, y: Int) = {
      val result = x - y

      if (x > 0 && y < 0 && result < 0) throw new OverflowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderFlowException
      else result
    }

    def multiply(x: Int, y: Int) = {
      val result = x * y
      if (x > 0 && y > 0 && result < 0) throw new OverflowException
      else if (x < 0 && y < 0 && result < 0) throw new OverflowException
      else if (x > 0 && y < 0 && result > 0) throw new UnderFlowException
      else if (x < 0 && y > 0 && result > 0) throw new UnderFlowException
      else result
    }

    def divide(x: Int, y: Int) = {
      if (y == 0) throw new MathCalculationException
      else x / y
    }
  }

//  println(PocketCalculator.add(Int.MaxValue, 10))
//  println(PocketCalculator.divide(2, 0))
}
