package lectures.exercise

import lectures.basics.part2oop.Generics.MyList

import scala.annotation.targetName
import scala.language.postfixOps
import scala.runtime.Nothing$

// IMPORTANT !!!!!!!!!!!!!!!!!
abstract class MyList[+A] {

  def head: A

  def tail: MyList[A]

  def isEmpty: Boolean

  def add[B >: A](element: B): MyList[B]

  //HIGHER ORDER FUNCTIONS - receive/returns other functions
  def map[B](transformer: A => B): MyList[B]

  def filter(predicate: A => Boolean): MyList[A]

  def flatMap[B](transformer: A => MyList[B]): MyList[B]

  def forEach(executor: A => Unit): Unit

  def sort(sorter: (A, A) => Int): MyList[A]

  def zipWith[B, C](list: MyList[B], zipper: (A, B) => C): MyList[C]

  def fold[B](start: B)(operator: (B, A) => B): B

  def printElements: String

  // polymorphic call
  override def toString: String = "[" + printElements + "]"

  // concatenation
  def ++[B >: A](list: MyList[B]): MyList[B]
}

// case -> 1. equals + hashCode = can use list and collection as well
// 2. serializable = can use in distributed network
case object Empty extends MyList[Nothing] {
  def head: Nothing = throw new NoSuchElementException

  def tail: MyList[Nothing] = throw new NoSuchElementException

  def isEmpty: Boolean = true

  def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)

  def map[B](transformer: Nothing => B): MyList[B] = Empty

  def filter(predicate: Nothing => Boolean): MyList[Nothing] = Empty

  def flatMap[B](transformer: Nothing => MyList[B]): MyList[B] = Empty

  def forEach(executor: Nothing => Unit): Unit = ()

  def sort(sorter: (Nothing, Nothing) => Int): MyList[Nothing] = Empty

  def zipWith[B, C](list: MyList[B], zipper: (Nothing, B) => C): MyList[C] =
    if (!list.isEmpty) throw new RuntimeException("Lists do not have the same length!")
    else Empty

  def fold[B](start: B)(operator: (B, Nothing) => B): B = start

  def printElements: String = ""

  def ++[B >: Nothing](list: MyList[B]): MyList[B] = list
}

// MyList is covariant so Cons must be covariant too
case class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
  def head: A = h

  def tail: MyList[A] = t

  def isEmpty: Boolean = false

  def add[B >: A](element: B): MyList[B] = new Cons(element, this)

  /*
  * [1,2,3].filter(n % 2 == 0) =
    [2,3].filter(n % 2 == 0) =
    new Cons(2, [3].filter(n % 2 == 0) =
    new Cons(2, Empty.filter(n % 2 == 0) =
    new Cons(2, Empty)
  * */
  def filter(predicate: A => Boolean): MyList[A] =
    if (predicate(head)) new Cons(h, t.filter(predicate))
    else t.filter(predicate)

  // in this case we're not bothered by construction of result, because list will result with the same structure
  /*
  * [1,2,3].map(n * 2) = new Cons(2, [2,3].map(n * 2))
  = new Cons(2, new Cons(4, [3].map(n * 2)))
  = new Cons(2, new Cons(4, new Cons(6, Empty.map(n * 2))))
  = new Cons(2, new Cons(4, new Cons(6, Empty))))
  * */
  def map[B](transformer: A => B): MyList[B] =
  new Cons(transformer(h), t.map(transformer))

  /*
    [1,2].flatMap(n => [n, n+1])
    = [1,2] ++ [2].flatmap(n => [n, n+1])
    = [1,2] ++ [2,3] ++ Empty.flatmap(n => [n, n+1])
    = [1,2] ++ [2,3] ++ Empty
    = [1,2,2,3]
   */
  def flatMap[B](transformer: A => MyList[B]): MyList[B] =
    transformer(h) ++ t.flatMap(transformer)

  /*
    [1,2] ++ [3,4,5]
    = new Cons(1, [2] ++ [3,4,5])
    = new Cons(1, new Cons(2, Empty ++ [3,4,5]))
    = new Cons(1, new Cons(2, new Cons(3, new Cons(4, new Cons(5)))))
   */
  def ++[B >: A](list: MyList[B]): MyList[B] = new Cons[B](h, t ++ list)

  def forEach(executor: A => Unit): Unit = {
    executor(h)
    t.forEach(executor)
  }

  def sort(sorter: (A, A) => Int): MyList[A] = {
    def insert(x: A, sortedTail: MyList[A]): MyList[A] =
      if (sortedTail.isEmpty) {
        new Cons(x, Empty)
      }
      else if (sorter(x, sortedTail.head) < 0) new Cons(x, sortedTail)
      else new Cons(sortedTail.head, insert(x, sortedTail.tail))

    val sortedTail = t.sort(sorter)
    // insert a head inside a already inserted list
    insert(h, sortedTail)
  }

  def zipWith[B, C](list: MyList[B], zipper: (A, B) => C): MyList[C] = {
    if (list.isEmpty) Empty.zipWith(list, zipper)
    else new Cons[C](zipper(head, list.head), tail.zipWith(list.tail, zipper))
  }

  def fold[B](start: B)(operator: (B, A) => B): B = t.fold(operator(start, h))(operator)


  def printElements: String =
    if (tail.isEmpty) s"$h"
    else s"$h ${t.printElements}"
  // printElements can't be protected because when working on external object the compilator will yiell that is not on this!
}

//trait MyPredicate[-T] { // T => Boolean
//  def test(elem: T): Boolean
//}
//
//trait MyTransformer[-A, B] { // A => B
//  def transform(elem: A): B
//}

object ListTest extends App {
  val list = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val cloneList = new Cons(1, new Cons(2, new Cons(3, Empty)))
  val list1 = new Cons(4, new Cons(5, Empty))
  println(list.tail.head)
  println(list.add(4).head)
  println(list.isEmpty)

  println(list.toString)

  val listOfIntegers: MyList[Int] = Empty
  val listOfStrings: MyList[String] = Empty

  println(list.map(_ * 2).toString)
  println(list.filter(_ % 2 == 0).toString)

  println(list ++ list1)
  println(list.flatMap(elem => new Cons(elem, new Cons(elem + 1, Empty))).toString)

  list.forEach(x => println(x))
  println(list.sort((x, y) => y - x))

  // `case` feature - if didn't use this then will need to use recursive equals method
  println(cloneList == list)

  val shorterList = Cons(2, Cons(2, Empty))
  println(list.zipWith(shorterList, (x, y) => x * y))
  println(list.zipWith(cloneList, _ * _))

  println(list.fold(0)(_ + _))

  // for comprehensions - will work only for this exact implementation of functions for: flatMap, map and filter (see: MapFlatmapFilterFor.scala)
  val combinations = for {
    n <- listOfIntegers
    s <- listOfStrings
  } yield s"$n - $s"
  
  println(combinations)
}
