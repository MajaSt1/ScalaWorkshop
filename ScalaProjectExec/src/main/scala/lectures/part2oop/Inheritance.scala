package lectures.part2oop

object Inheritance extends App {

  //access modifiers in Scala: private, protected, default (none = public)
  // single class inheritance
  class Animal {
    // is only accessible through the subclass and this class
    //    protected def eat = println("nomnomnom")
    def eat = println("nomnomnom")

    val creatureType = "wild"
  }

  class Cat extends Animal {
    def crunch = {
      eat
      println("crunch crunch")
    }
  }

  val cat = new Cat
  cat.crunch


  // constructors
  class Person(name: String, age: Int) {
    def this(name: String) = this(name, 0)
  }

  class Adult(name: String, age: Int, idCard: String) extends Person(name)

  //overriding members or methods
  class Dog(override val creatureType: String) extends Animal {
    override def eat: Unit = {
      super.eat
      println("crunch, crunch")
    }
  }

  val dog = new Dog("K9")
  dog.eat
  println(dog.creatureType)

  // type substitution (broad: polymorphism)
  val unknownAnimal: Animal = new Dog("K9")
  unknownAnimal.eat

  // overRIDING vs overLOADING

  // super - reuse parent fields/methods
  unknownAnimal.eat

  // preventing overrides (final and sealed)
  // 1 - use final on member ->  final def eat = println("nomnomnom")
  // 2 - use final on the entire class -> final class Animal
  // 3 - seal the class = extend classes in THIS FILE, prevent extension in other files (for example if you know that its reserved to only particular classes to inherit) -> sealed class Animal
}
