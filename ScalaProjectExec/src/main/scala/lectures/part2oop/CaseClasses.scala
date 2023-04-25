package lectures.part2oop

object CaseClasses extends App {
  // boilerplate for structure data
  // equals, hashCode, toString

  case class Person(name: String, age: Int)

  // 1. class parameters are fields (promote all parameters to fields)
  val jim = new Person("jim", 34)
  println(jim.name)

  // 2. sensible toString
  // println(instance) = println(instance.toString) // syntactic sugar
  println(jim)

  // 3. equals and hashcode implemented OOTB
  val jim2 = new Person("Jim", 34)
  println(jim == jim2)

  // 4. CCs have handy copy method
  val jim3 = jim.copy(age = 45)
  println(jim3)

  // 5. CCs have companion objects already implemented
  val thePerson = Person
  val mary = Person("Mary", 23) //delegates apply method = the same thing as constructor

  // 6. CCs are serializable
  // you can send case classes through the network and in between jvm's -> Akka framework = dealing with sending serializable messages through the network

  // 7. CCs have extractor patterns = CCs can be used in PATTERN MATCHING

  // companion object:
  case object UnitedKingdom {
    def name: String = "The UK of GB and NI"
  }

  /*
  * Expand MyList - use case classes and case objects
  * */
}
