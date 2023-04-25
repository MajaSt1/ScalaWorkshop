package lectures.part2oop

import lectures.part2oop.Objects.Person.things

object Objects {
  // SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY ("static")
  object Person { // Type + it's only instance
    // "static"/"class" - level functionality
    val N_EYES = 2

    def canFly: Boolean = false

    private def things : String = "PRIVATE THINGS"

    // factory method
    def apply(mother: Person, father: Person): Person = new Person("Bobby")
  }

  class Person(val name: String) {
    // instance-level functionality
    def printPrivateThings(): Unit = println(things) // can access each other private members
  }
  // COMPANIONS - class + object in the same scope and the same name

  def main(args: Array[String]): Unit = {
    println(Person.N_EYES)
    println(Person.canFly)

    //Scala object = SINGLETON INSTANCE
    val person1 = Person
    val person2 = Person
    println(person1 == person2)

    val mary = new Person("Mary")
    val john = new Person("John")
    println(mary == john)

    val bobby = Person(mary, john)

    mary.printPrivateThings()
  }
  // Scala application = Scala object with
  // def main(args: Array[String]) : Unit

}
