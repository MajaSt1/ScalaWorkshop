package lectures.exercise

abstract class Maybe[+T] {
  def map[B](transformer: T => B): Maybe[B]

  def filter(predicate: T => Boolean): Maybe[T]

  def flatMap[B](transformer: T => Maybe[B]): Maybe[B]
}

case object MaybeNot extends Maybe[Nothing] {
  def map[B](transformer: Nothing => B): Maybe[B] = MaybeNot

  def filter(predicate: Nothing => Boolean): Maybe[Nothing] = MaybeNot

  def flatMap[B](transformer: Nothing => Maybe[B]): Maybe[B] = MaybeNot
}

// one element implementation !
case class Just[+T](value: T) extends Maybe[T] {
  def map[B](transformer: T => B): Maybe[B] = new Just[B](transformer(value))

  def filter(predicate: T => Boolean): Maybe[T] =
    if (predicate(value)) this
    else MaybeNot

  def flatMap[B](transformer: T => Maybe[B]): Maybe[B] = transformer(value)
}

object MaybeTest extends App {
  val just3 = Just(3)
  println(just3)
  println(just3.map(_ * 2))
  println(just3.flatMap(x => Just(x % 2 == 0)))
  println(just3.filter(_ % 2 == 0))
}

// Scala offers both mutable and immutable collections:
// mutable collections can be updated in place
// immutable collections never change
// we're using immutable collections by default

// package object scala {
//  type List[+A] = immutable.List[A]
// }

// object Predef {
//  type Map[A, +B] = immutable.Map[A, B]
//  type Set[A] = immutable.Set[A]
//

//Immutable Collections.
// Immutable collections are found in scala.collections.immutable package
// Traversable -> Iterable ->
// (Set(do not contain duplicates) -> (HashSet, SortedSet), Map -> (HashMap, SortedMap), Seq(can be traversed in set order) -> (IndexedSeq (accessed in constant time)-> (Vector, Range, String)), (LinearSeq (guarantee only the order of the elements) -> (List, Queue, Stack, Stream))


// Trevarsable
// Base trait for all collections. Offers a great variety of methods:
// - maps: map, flatMap, collect
// - conversions: toArray, toList, toSeq
// - size info: isEmpty, size, nonEmpty
// - tests: exists, forall
// - folds: fold, reduce
// - retrieval: head, find, tail
// - string ops: mkString