package lectures.basics.part1basics

import scala.annotation.tailrec

/*
* Recursion
tailrecursion makes recursive function not use additional stack frames
when calling them. That used to cause a lot of stack overflow errors ,
*  but with tailrec there are converted to iterations in the JVM bytecode
* */
object Recursion extends App {

  def factorial(n: Int): Int =
    if (n <= 1) 1
    else {
      println("Computing factorial of " + n + " - I first need factorial of " + (n - 1))
      val result = n * factorial(n - 1)
      // it needs additional recursive stack frame for each calls
      println("Computed factorial of " + n)

      result
    }

  println(factorial(10))

  // jvm use a callstack to keep partial result - stack frame


  // recursive depth is too big then the result is StackOverflow error -> jvm keeps internal stack which has limited memory
  def anotherFactorial(n: Int): BigInt = {
    @tailrec // it tells compiler that is tail recursive (if it would be not tail recursive compiler will result with ERROR)
    def factorialHelper(x: Int, accumulator: BigInt): BigInt =
      if (x <= 1) accumulator
      else factorialHelper(x - 1, x * accumulator)
    // TAIL RECURSION = use recursive call as the LAST expression!
    //WHY IT WORKS? -> it preserve the current stack frame (without using additional stack frame memory), not using additional stack frame for recursive calls

    factorialHelper(n, 1)
  }

  /*
    anotherFactorial(10) = factorialHelper(10, 1)
    = factorialHelper(9, 10 * 1)
    = factorialHelper(8, 9 * 10 * 1)
    = factorialHelper(7, 8 * 9 * 10 * 1)
    = ...
    = factorialHelper(1, 2 * ... * 10 * 1)
    = 2 * ... * 10 * 1 => accumulator
  */

  println(anotherFactorial(5000)) // overflow the Integer representation so it will be 0 at some point


  // WHEN YOU NEED LOOPS , USE _TAIL_ RECURSION.
  // exc.1
  def concatenateString_n_times(word: String, n: Int): String = {
    @tailrec
    def concatenateHelper(x: Int, accumulator: String): String =
      if (x <= 1) accumulator
      else concatenateHelper(x - 1, word + accumulator)

    concatenateHelper(n, word)
  }

  println(concatenateString_n_times("Bon", 1))

  //exc.2

  def isPrime(n: Int): Boolean = {
    @tailrec
    def isPrimeUntilRecursive(t: Int, isStillPrime: Boolean): Boolean = {
      if (!isStillPrime) false
      else if (t <= 1) true
      else isPrimeUntilRecursive(t - 1, n % t != 0 && isStillPrime)
    }

    isPrimeUntilRecursive(n / 2, true)
  }

  println(isPrime(2003))
  println(isPrime(629))

  // exc.3
  def fibonacci(n: Int): Int = {
    @tailrec
    def fiboTailrec(i: Int, last: Int, nextLast: Int): Int =
      if (i >= n) last
      else fiboTailrec(i + 1, last + nextLast, last)

    if(n <= 2) 1
    else fiboTailrec(2, 1, 1)
  }

  println(fibonacci(3))
}
/// one thing from my sight ,  besides that test for prod env if
// everything goes well, I can start working at smile integration
// , so setting up subfolders  in repo and so on
