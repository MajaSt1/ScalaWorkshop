package lectures.basics.part2oop

import lectures.basics.playground.PrinceCharming as Prince
import java.util.Date
import java.sql.{Date => SqlDate}

// package = a group of definitions under the same name
object PackagingAndImports extends App {

  // package members are accessible by their simple name
  val writer = new Writer("Daniel", "JVM", 2018)

  // packages are in hierarchy
  // matching folder structure

  // package object = hold standalone methods/constants
  sayHello
  println(SPEED_OF_LIGHT)

  // imports
  val prince = new Prince

  // 1. use FQ names
  val date = new Date
  val sqlDate = new SqlDate(2018, 5, 4)
  // 2. use aliasing

  //default imports
  // java.lang - String, Object, Exception
  // scala - Int, Nothing, Function
  // scala.Predef - println
}
