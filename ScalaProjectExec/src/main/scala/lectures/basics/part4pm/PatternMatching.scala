package lectures.basics.part4pm

import com.sun.source.tree.ExportsTree

import scala.util.Random

object PatternMatching extends App {

  // switch on steroids
  val random = new Random
  val x = random.nextInt(10)

  val description = x match
    case 1 => "the ONE"
    case 2 => "double or nothing"
    case 3 => "third time is the charm"
    case _ => "something else" // _ = WILDCARD

  println(x)
  println(description)

  //1. Decompose values
  case class Person(name: String, age: Int)

  val bob = Person("Bob", 20)

  val greeting = bob match
    case Person(n, a) if a < 21 => s"Hi, my name is $n and I can't drink in the U.S."
    case Person(n, a) => s"Hi, my name is $n and I am $a years old."
    case null => "I don't know who I am"

  println(greeting)

  /*
  1. cases are matched in order
  2. what if no cases match? => scala.MatchError
  3. type of pattern matching expression? unified type of all types in all the cases
  4. PM works really well with case classes*
  */

  // PM on sealed hierarchies
  sealed class Animal // sealed classes are covering your ass ! ;)

  case class Dog(breed: String) extends Animal

  case class Parrot(greeting: String) extends Animal

  val animal: Animal = Dog("Terra Nova")
  animal match
    case Dog(someBreeed) => println(s"Matched a dog of the $someBreeed breed")
    case Parrot(greeting) => ""
    case _ =>

  // match everything
  val isEven = x match
    case n if n % 2 == 0 => true
    case _ => false
  // WHY?
  val isEvenCond = if (x % 2 == 0) true else false
  val isEvenNormal = x % 2 == 0

  /*
  Exercise
  simple function uses PM
  takes an Expr => human readable form

  Sum(Number(2), Number(3)) => 2 + 3
  Sum(Number(2), Number(3), Number(4)) => 2 + 3 + 4
  Prod(Sum(Number(2), Number(1)), Number(3)) = (2 + 1) * 3
  Sum(Prod(Number(2), Number(1)), Number(3)) = 2 * 1 + 3
  */

  trait Expr

  case class Number(n: Int) extends Expr

  case class Sum(e1: Expr, e2: Expr) extends Expr

  case class Prod(e1: Expr, e2: Expr) extends Expr

  val y: Sum = Sum(Number(2), Number(3))

  // !!!!!!! IMPORTANT
  def matchExpression(expr: Expr): String = {
    expr match
      case Number(n) => s"$n"
      case Sum(e1, e2) => s"${matchExpression(e1)} + ${matchExpression(e2)}"
      case Prod(e1, e2) => {
        def insideProd(expr: Expr): String = {
          expr match
            case Prod(_, _) => matchExpression(expr)
            case Number(_) => matchExpression(expr)
            case _ => s"(${matchExpression(expr)})"
          /*      HOW IT WORKS:
                  case Number(n) => s"$n"
                  case Prod(e1, e2) => s"(${matchExpression(e1)} * ${matchExpression(e2)})"
                  case Sum(e1, e2) => s"(${matchExpression(e1)} + ${matchExpression(e2)})"
                  case _ => ""*/
        }

        s"${insideProd(e1)} * ${insideProd(e2)}"
      }
      case _ => ""
  }

  val expression1 = Prod(Sum(Number(2), Number(1)), Number(3))
  val expression2 = Sum(Number(2), Number(3))
  val expression3 = Sum(Number(2), Sum(Number(3), Number(4)))
  val expression4 = Sum(Prod(Number(2), Number(3)), Prod(Number(1), Number(3)))
  println(matchExpression(expression1))
  println(matchExpression(expression2))
  println(matchExpression(expression3))
  println(matchExpression(expression4))

}
