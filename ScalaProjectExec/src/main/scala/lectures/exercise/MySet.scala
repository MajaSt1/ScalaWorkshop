package lectures.exercise

import scala.annotation.tailrec

trait MySet[A] extends (A => Boolean) {

  /*
  * Exercise. implement a functional set
  * */
  def apply(elem: A): Boolean = contains(elem)

  def contains(elem: A): Boolean

  def +(elem: A): MySet[A]

  def ++(anotherSet: MySet[A]): MySet[A] // union

  def map[B](f: A => B): MySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B]

  def filter(predicate: A => Boolean): MySet[A]

  def foreach(f: A => Unit): Unit

  def -(elem: A): MySet[A]

  def --(anotherSet: MySet[A]): MySet[A] // difference

  def &(anotherSet: MySet[A]): MySet[A] // intersection

  def unary_! : MySet[A]
}

// is invarient so it can't be MySet[Nothing]
class EmptySet[A] extends MySet[A] {

  def contains(elem: A): Boolean = false

  def +(elem: A): MySet[A] = new NonEmptySet[A](elem, this)

  def ++(anotherSet: MySet[A]): MySet[A] = anotherSet

  def map[B](f: A => B): MySet[B] = new EmptySet[B]

  def flatMap[B](f: A => MySet[B]): MySet[B] = new EmptySet[B]

  def filter(predicate: A => Boolean): MySet[A] = this

  def foreach(f: A => Unit): Unit = ()

  def -(elem: A): MySet[A] = this

  def --(anotherSet: MySet[A]): MySet[A] = this

  def &(anotherSet: MySet[A]): MySet[A] = this

  def unary_! : MySet[A] = new PropertyBaseSet[A](_ => true)
}

// STEP #2 for unary_! = infinite => we use PropertyBaseSet
// infinite set
//class AllInclusiveSet[A] extends MySet[A] {
//  override def contains(elem: A): Boolean = true
//
//  override def +(elem: A): MySet[A] = this
//
//  override def ++(anotherSet: MySet[A]): MySet[A] = this
//
//  // naturals = allinclusiveSet[Int] = all the natural numbers
//  // naturals.map(x => x % 3) => ???
//  // [0 1 2]
//  override def map[B](f: A => B): MySet[B] = ???
//
//  override def flatMap[B](f: A => MySet[B]): MySet[B] = ???
//
//  override def foreach(f: A => Unit): Unit = ???
//
//
//  override def filter(predicate: A => Boolean): MySet[A] = ??? // property-base set
//
//  override def -(elem: A): MySet[A] = ???
//
//  override def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet)
//
//  override def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet)
//
//  override def unary_! : MySet[A] = new EmptySet[A]
//}

// all elements of type A which satisfy a property
// {x in A | property(x) }
class PropertyBaseSet[A](property: A => Boolean) extends MySet[A] {
  def contains(elem: A): Boolean = property(elem)

  def +(elem: A): MySet[A] =
  // { x in A | property(x) } + element = { x in A | property(x) || x == element}
    new PropertyBaseSet[A](x => property(x) || x == elem)

  // { x in A | property(x) } ++ set = { x in A | property(x) || set.contains(x)}
  def ++(anotherSet: MySet[A]): MySet[A] = // union
    new PropertyBaseSet[A](x => property(x) || anotherSet(x))

  // all integers => (_ % 3) => [0 1 2] we wont know if a element is in the set
  def map[B](f: A => B): MySet[B] = politelyFail

  def flatMap[B](f: A => MySet[B]): MySet[B] = politelyFail

  def filter(predicate: A => Boolean): MySet[A] = new PropertyBaseSet[A](x => property(x) && predicate(x))

  def foreach(f: A => Unit): Unit = politelyFail

  def -(elem: A): MySet[A] = filter(x => x != elem)

  def --(anotherSet: MySet[A]): MySet[A] = filter(!anotherSet) // difference

  def &(anotherSet: MySet[A]): MySet[A] = filter(anotherSet) // intersection

  def unary_! : MySet[A] = new PropertyBaseSet[A](x => !property(x))

  def politelyFail = throw new IllegalArgumentException("Really drop rabbot hole!")
}

class NonEmptySet[A](head: A, tail: MySet[A]) extends MySet[A] {

  // similar to apply
  def contains(elem: A): Boolean =
    head == elem || tail.contains(elem)

  def +(elem: A): MySet[A] =
    if (this contains elem) this
    else new NonEmptySet[A](elem, this)

  /*
  * [1,2,3] ++ [4,5] =
    [2,3] ++ [4,5] + 1 =
    [3] ++ [4,5] + 1 + 2 =
    [] ++ [4,5] + 1 + 2 + 3 =
    [4,5] + 1 + 2 + 3 = [4,5,1,2,3]
  * */
  def ++(anotherSet: MySet[A]): MySet[A] =
    tail ++ anotherSet + head

  def map[B](f: A => B): MySet[B] =
    (tail map f) + f(head)

  def flatMap[B](f: A => MySet[B]): MySet[B] =
    f(head) ++ (tail flatMap f)

  def filter(predicate: A => Boolean): MySet[A] = {
    val filteredTail = tail filter predicate
    if (predicate(head)) filteredTail + head
    else filteredTail
  }

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail foreach f
  }

  def -(elem: A): MySet[A] = {
    if (elem == head) tail
    else tail - elem + head
  }

  def --(anotherSet: MySet[A]): MySet[A] = {
    filter(!anotherSet)
    // refactor =>
    //filter(x => !(anotherSet contains x))
    // filter(!anotherSet) + unary operator
  }

  def &(anotherSet: MySet[A]): MySet[A] = { // intersection = filtering!
    filter(x => anotherSet contains x)
    //refactor =>
    // filter(x => anotherSet(x)) =
    // filter(anotherSet)
  }

  // Exercise - implement a unary_! = NEGATION of a set
  // set[1,2,3] => x.contains _
  def unary_! : MySet[A] = new PropertyBaseSet[A](x => !this.contains(x))
}

/* EXERCISE
- removing an element
- intersection with another set
- difference with another set
*/

object MySet {
  /*
  val s = MySet(1,2,3) = buildSet(seq(1,2,3), [])
  = buildSet(seq(2,3), [] + 1)
  = buildSet(seq(3), [1] + 2)
  = buildSet(seq(), [1,2] + 3)
  = [1,2,3] (acc)
  */
  def apply[A](values: A*): MySet[A] = {
    @tailrec
    def buildSet(valSeq: Seq[A], acc: MySet[A]): MySet[A] =
      if (valSeq.isEmpty) acc
      else buildSet(valSeq.tail, acc + valSeq.head)

    buildSet(values.toSeq, new EmptySet[A])
  }
}

object MySetPlayground extends App {
  val s = MySet(1, 2, 3, 4)
  //  s + 5 ++ MySet(-1, -2) + 3 flatMap (x => MySet(x, x * 10)) filter (_ % 2 == 0) foreach println
  //  s - 3 - 1 - 5 foreach println

  val d = MySet(1, 2)
  //  s & d foreach println
  s -- d foreach println

  val negative = !s // s.unary_! = all the naturals not equal to 1,2,3,4
  println(negative(2))
  println(negative(5))

  val negativeEven = negative.filter(_ % 2 == 0)
  println(negativeEven(5))

  val negativeEven5 = negativeEven + 5 // all the even numbers > 4 + 5
  println(negativeEven5(5))
}