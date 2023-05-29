package lectures.exercise

import scala.annotation.tailrec

abstract class MyStream[+A] {
  def isEmpty: Boolean

  def head: A

  def tail: MyStream[A]

  def #::[B >: A](element: B): MyStream[B] // prepend operator

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] //concatenate two streams

  def foreach(f: A => Unit): Unit

  def map[B](f: A => B): MyStream[B]

  def flatMap[B](f: A => MyStream[B]): MyStream[B]

  def filter(predicate: A => Boolean): MyStream[A]

  def take(n: Int): MyStream[A] // takes the first n elements out of this stream

  def takeAsList(n: Int): List[A] = take(n).toList()

  // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
  /*
  [1 2 3].toList([]) =
  [2 3].toList([1]) =
  [3].toList([2 1]) =
  [].toList([3 2 1] =
  List(1,2,3)
  * */
  @tailrec
  final def toList[B >: A](acc: List[B] = Nil): List[B] =
  if (isEmpty) acc.reverse
  else tail.toList(head :: acc) // add element at the beginning of a list
}

object EmptyStream extends MyStream[Nothing] {
  def isEmpty: Boolean = true

  def head: Nothing = throw new NoSuchElementException

  def tail: MyStream[Nothing] = throw new NoSuchElementException

  def #::[B >: Nothing](element: B): MyStream[B] = new NonEmptyStream[B](element, this) // prepend operator

  def ++[B >: Nothing](anotherStream: => MyStream[B]): MyStream[B] = anotherStream //concatenate two streams

  def foreach(f: Nothing => Unit): Unit = ()

  def map[B](f: Nothing => B): MyStream[B] = this

  def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this

  def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

  def take(n: Int): MyStream[Nothing] = this // takes the first n elements out of this stream
}

class NonEmptyStream[+A](h: A, t: => MyStream[A]) extends MyStream[A] {
  def isEmpty: Boolean = false

  // !!!
  override val head: A = h

  override lazy val tail: MyStream[A] = t // call by need !

  /*
   val s = new NonEmptyStream(1, EmptyStream) // EmptyStream is lazy evaluated = call by name
   val prepended = 1 #:: s = new NonEmptyStream(1,s) // tail is still unevaluated
  * */
  def #::[B >: A](element: B): MyStream[B] = new NonEmptyStream[B](element, this) // prepend operator

  def ++[B >: A](anotherStream: => MyStream[B]): MyStream[B] = new NonEmptyStream[B](head, tail ++ anotherStream) //concatenate two streams

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  /*
   s = new NonEmptyStream(1, ?)
  mapped = s.map(_ + 1) = new NonEmptyStream(2, s.tail.map(_ + 1))
  ... mapped.tail - by need bases
  * */
  def map[B](f: A => B): MyStream[B] = new NonEmptyStream[B](f(head), tail map f) // preserves lazy evaluation

  // tail.flatMap(f) - expression was eagerly eval without lazy evaluation of ++ parameter
  def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)

  def filter(predicate: A => Boolean): MyStream[A] = {
    if (predicate(head)) new NonEmptyStream[A](head, tail.filter(predicate))
    else tail.filter(predicate) // preserve lazy eval!
  }


  def take(n: Int): MyStream[A] = {
    if (n <= 0) EmptyStream
    else if (n == 1) new NonEmptyStream[A](head, EmptyStream)
    else new NonEmptyStream[A](head, tail.take(n - 1))
  } // takes the first n elements out of this stream
}

object MyStream {
  def from[A](start: A)(generator: A => A): MyStream[A] =
    new NonEmptyStream[A](start, from(generator(start))(generator))
}

object MyStreamTest extends App {
  val naturals: MyStream[Int] = MyStream.from(1)(_ + 1)
  // evaluate only if its needed (lazy eval - prevent stack overflow) => we combined call by name and lazy val in correct form
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals // naturals.#::(0)
  println(startFrom0.head)

  startFrom0.take(10).foreach(println)

  // map, flatMap
  println(startFrom0.map(_ * 2).take(10).toList())
  println(startFrom0.flatMap(x => new NonEmptyStream(x, new NonEmptyStream(x + 1, EmptyStream))).take(10).toList())
  println(startFrom0.filter(_ < 10).take(10).toList())

  // Exercise on streams
  // 1 - stream of Fibonacci numbers
  // 2 - stream of prime numbers with Eratosthenes sieve

  /*
  * [2 3 4 ....]
  * filter out all numbers divisible by 2
  [2 3 5 7 9 11...]
  filter out all numbers divisible by 3
  [ 2  3 5 7 11 13 17 ...]
  filter out all numbers divisible by 5
  ...
   */

  /*
  [ first, [... Stream]]
  [first, fibon(second, first + second)]
  */

  def fibonacci(firstNumber: BigInt, secondNumber: BigInt): MyStream[BigInt] = new NonEmptyStream[BigInt](firstNumber, fibonacci(secondNumber, secondNumber + firstNumber))

  println(fibonacci(1, 1).take(100).toList())

  /*
    [ 2 3 4 5 6 7 8 9 10 11 12 ...]
    [ 2 3 5 7 9 11...]
    [ 2 erat. applied to (numbers filtered by n % 2 != 0)
    [ 2 3 erat. applied to [ 5 7 9 11 ...] filtered by n % 3 != 0)
    [ 2 3 5
  */
  def eratosthenes(numbers: MyStream[Int]): MyStream[Int] =
    if (numbers.isEmpty) numbers
    else new NonEmptyStream(numbers.head, eratosthenes(numbers.tail.filter(x => x % numbers.head != 0)))

  val numbers: MyStream[Int] = MyStream.from(2)(_ + 1)
  println(eratosthenes(numbers).take(100).toList())

}