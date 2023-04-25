package lectures.part2oop

object AbstractDataTypes extends App {
  // abstract
  abstract class Animal {
    val creatureType: String = "wild"

    // this value is abstract because we're not supply values for them
    def eat: Unit
  }

  class Dog extends Animal {
    override val creatureType: String = "Canine"

    def eat: Unit = println("crunch crunch")
  }

  // traits
  trait Carnivore {
    def eat(animal: Animal): Unit // this method is abstract
    val preferredMeal: String = "fresh meat"
  }

  trait ColdBlooded

  class Crocodile extends Animal with Carnivore with ColdBlooded {
    override val creatureType: String = "croc"

    def eat: Unit = println("nomnomnom")

    def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")
  }

  val dog = new Dog
  val croc = new Crocodile
  croc.eat(dog)


  //traits vs abstract classes
  // 1 - traits do not have constructor parameters
  // 2 - multiple traits may be inherited by the same class
  // 3 - traits = behavior, abstract class = "thing"

  // type hierarchy
  // Any -> AnyRef = java.lang.Object -> scala.Null (no reference) -> scala.Nothing (no instance)
  // AnyVal(Int,Unit,Boolean,Float...) -> scala.Nothing (no instance)
}
