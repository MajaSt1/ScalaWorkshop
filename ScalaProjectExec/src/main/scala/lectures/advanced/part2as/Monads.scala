package lectures.advanced.part2as

object Monads extends App {

  // Monads are kind of types which have some fundamentals operations
  // unit (constract a monads) + flatMap (transform a monad)
  // Monads: List, Option, Try, Future, Stream, Set are all monads
  // Operations must satisfy the monad laws:
  // - left identity unit(x).flatMap(f) == f(x)
  // - right identity adMonadInstance.flatMap(unit) == aMonadInstance
  // - associativity m.flatMap(f).flatMap(g) == m.flatMap(x => f(x).flatMap(g))

  // Left identity:
  //  List(x).flatMap(f) =
  //    f(x) ++ Nil.flatMap(f) = f(x)

  // Right identity:
  // list.flatMap(x => List(x)) = list

  // associativity:
  //   [a b c].flatMap(f).flatMap(g)
  //    = (f(a) ++ f(b) ++ f(c)).flatMap(g) =
  //    f(a).flatMap(g) ++ f(b).flatMap(g) ++ f(c).flatMap(g) =
  //    [a b c].flatMap(f(_).flatMap(g)) =
  //    [a b c].flatMap(x => f(x).flatMap(g))


  // our own Try Monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    // call by name parameter
    def apply[A](a: => A): Attempt[A] =
      try {
        Success(a)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B] =
      try {
        f(value)
      } catch {
        case e: Throwable => Fail(e)
      }
  }

  case class Fail(e: Throwable) extends Attempt[Nothing] {
    def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  /*
  left-identity
  unit.flatMap(f) = f(x)
  Attempt(x).flatMap(f) = f(x)  // Success case!
  Success(x).flatMap(f) = f(x) // proved.

  right-identity
  attempt.flatMap(unit) = attempt
  Success(x).flatMap(x => Attempt(x)) = Attempt(x) = Success(x)
  Fail(_).flatMap(...) = Fail(e)

  associativity
  attempt.flatMap(f).flatMap(g) == attempt.flatMap(x => f(x).flatMap(g))

  Fail(e).flatMap(f).flatMap(g) = Fail(e)
  Fail(e).flatMap(x => f(x).flatMap(g)) = Fail(e)

  Success(v).flatMap(f).flatMap(g) =
    f(v).flatMap(g) OR Fail(e)

  Success.flatMap(v).flatMap(x => f(x).flatMap(g)) =
    f(v).flatMap(g) OR Fail(e)
  */

  val attempt = Attempt {
    throw new RuntimeException("My own monad!")
  }

  println(attempt)

  /*
  * EXERCISE:
  1) implement a Lazy[T] monad = computation which will be only executed when it's needed
  unit/apply
  flatMap

  2) Monads = unit + flatMap
     Monads = unit + map + flatten

    Monad[T] {
      def flatMap[B](f: T => Monad[B]): Monad[B] = ... (implemented)
      def map[B](f: T => B): Monad[B] = flatMap(x => Monad(f(x))) // Monad[B]
      def flatten(m: Monad[Monad[T]]): Monad[T] = m.flatMap((x: Monad[T]) => x)

      List(1,2,3).map(_ * 2) = List(1,2,3).flatMap(x => List(x * 2))
      List(List(1,2), List(3,4)).flatten = List(List(1,2), List(3,4)).flatMap(x => x) = List(1,2,3,4)
      (have List in mind)
  }
  * */

  class Lazy[+A](value: => A) {
    // call by need
    private lazy val internalValue = value
    def use: A = internalValue

    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)
  }

  object Lazy {
    // call by name parameter
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInstance = Lazy {
    println("Today I feel quite fine...")
    42
  }

  val flatMapInstance = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  val flatMapInstance2 = lazyInstance.flatMap(x => Lazy {
    10 * x
  })

  flatMapInstance.use
  flatMapInstance2.use

  /*
  left-identity
  unit.flatMap(f) = f(v)
  Lazy(v).flatMap(f) = f(v)

  right-identity
  l.flatMap(unit) = l
  Lazy(v).flatMap(x => Lazy(x)) = Lazy(v)

  associativity
  Lazy(v).flatMap(f).flatMap(g) = f(v).flatMap(g)
  Lazy(v).flatMap(x => f(x).flatMap(g)) = f(v).flatMap(g)
  */
}
