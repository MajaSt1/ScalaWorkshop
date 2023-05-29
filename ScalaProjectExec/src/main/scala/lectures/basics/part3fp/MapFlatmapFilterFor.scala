package lectures.basics.part3fp

object MapFlatmapFilterFor extends App {
  val list = List(1, 2, 3) // apply
  println(list.head)
  println(list.tail)

  // map
  println(list.map(_ + 1))
  println(list.map(_ + " is a number"))

  // filter
  println(list.filter(_ % 2 == 0))

  // flatMap
  val toPair = (x: Int) => List(x, x + 1)
  println(list.flatMap(toPair))

  // print all combinations between two lists
  val numbers = List(1, 2, 3, 4)
  val chars = List('a', 'b', 'c', 'd')
  val colors = List("black", "white")

  // List("a1", "a2",...,"d4")
  // transform loops (two loops => flatMap + map, three loops => flatMap x 2 + map)
  // "iterating"
  val combinations = numbers.flatMap(n => chars.map(f => s"$n$f"))
  val combinations2 = numbers.flatMap(n => chars.flatMap(f => colors.map(c => s"$n$f-$c")))
  println(combinations2)
  println(combinations)

  // foreach
  list.foreach(println)

  // for-comprehensions
  // this is equivalent to map and flatMap combinations
  val forCombinations = for {
    n <- numbers if n % 2 == 0
    c <- chars
    color <- colors
  } yield s"$n$c-$color"
  println(forCombinations)

  // is equivalent to numbers.foraech(n=> println(n))
  for {
    n <- numbers
  } println(n)

  // syntax overload
  list.map { x =>
    x * 2
  }

  // EXERCISES
  // 1. MyList supports for comprehensions?
  // map(f: A => B) => MyList[B]
  // filter(p: A => Boolean) => MyList[A]
  // flatMap(t: A => MyList[B]) => MyList[B]

  // 2. A small collection of at most ONE element - Maybe[+T] Empty or one element T
  // - map, flatMap, filter

  // Optional values

}
