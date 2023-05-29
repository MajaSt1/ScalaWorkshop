package lectures.basics.part3fp

import scala.util.Random

object Sequences extends App {

  // Sequences
  // trait Seq[+A] {
  // def head: A
  // def tail: Seq[A]
  // }
  // very general interface for data structures that:
  // - have a well defined order
  // - can be indexed
  // Supports various operations:
  // - apply, iterator, length, reverse for indexing and iterating
  // - concatenation, appending, prepending

  val aSequence = Seq(2, 3, 4, 1)
  println(aSequence) // List
  println(aSequence.reverse)
  println(aSequence(2)) // 2
  println(aSequence ++ Seq(5, 6, 7))
  println(aSequence.sorted)

  // Ranges
  val aRange: Seq[Int] = 1 until 10
  aRange.foreach(println)

  (1 to 10).foreach(x => println("Hello"))

  // List
  // A LinearSeq immutable linked list
  // - head, tail, isEmpty methods are fast: O(1) = constant time
  // - most operations are O(n): length, reverse
  // Sealed - has two subtypes:
  // - object Nill (empty)
  // - class ::

  val aList = List(1, 2, 3)
  val prepended = 42 :: aList // apply
  val prepended1 = 42 +: aList // prepending
  val uppending = aList :+ 80
  println(aList)

  val apples5 = List.fill(5)("apple") //curried function = constructs 5 x value
  println(apples5)
  println(aList.mkString("-")) // concatenate with separator

  // Array
  // The equivalent of simple Java arrays
  // - can be manually constructed with predefined length
  // - can be mutated (updated in place)
  // - are interoperable with Java's T[] arrays
  // - indexing fast
  // Where is the Seq?!

  val numbers = Array(1, 2, 3, 4)
  val threeElements = Array.ofDim[Int](3) // allocates the space for three elements withour supplying those elements
  val threeElementsString = Array.ofDim[String](3) // allocates the space for three elements withour supplying those elements
  threeElements.foreach(println)
  threeElementsString.foreach(println)

  // mutation
  // syntax sugar for numbers.update(2,0) !!!
  numbers(2) = 0 // update the value at index 2 with 0
  println(numbers.mkString(" "))

  // arrays and seq
  val numbersSeq: Seq[Int] = numbers // implicit conversion
  println(numbersSeq)

  // Vector
  // The dafault implementation for immutable sequences
  // - effectively constant indexed read and write: O(log32(n))
  // - fast element addition: append/prepend
  // - implemented as a fixed-branched trie (branch factor 32) / memory/cache optimization
  // - good performance for large sizes

  val vector: Vector[Int] = Vector(1, 2, 3)
  println(vector)

  // vectors vs lists
  val maxRuns = 1000
  val maxCapacity = 1000000
  def getWriteTime(collection: Seq[Int]): Double = {
    val r = new Random
    val times = for {
      it <- 1 to maxRuns
    } yield {
      val currentTime = System.nanoTime()
      collection.updated(r.nextInt(maxCapacity), r.nextInt()) // update at random index and put value and returns new collection
      System.nanoTime() - currentTime
    }

    times.sum * 1.0 / maxRuns // avarage
  }

  val numbersList = (1 to maxCapacity).toList // creates a List with capacity
  val numbersVector = (1 to maxCapacity).toVector // creates a Vector with capacity

  // advantage: keeps reference to tail
  // disadvantage: updating an element in the middle takes long
  println(getWriteTime(numbersList))
  // advantage: depth of the tree is small
  // disadvantage: needs to replace an entire 32-element chunk
  println(getWriteTime(numbersVector))

}
