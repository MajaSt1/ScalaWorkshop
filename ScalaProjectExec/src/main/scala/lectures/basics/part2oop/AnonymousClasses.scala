package lectures.basics.part2oop

object AnonymousClasses extends App {

  abstract class Animal {
    def eat: Unit
  }

  // anonymous class
  val funnyAnimal: Animal = new Animal :
    override def eat: Unit = println("ahahhaha")

  /*
    equivalent with:

    class AnonymousClasses$$anon$1 extends Animal {
      override def eat: Unit = println("ahahhaha")
    }

    val funnyAnimal: Animal = new AnonymousClasses$$anon$1 -> non abstract/trait class
  */

  println(funnyAnimal.getClass)

  class Person(name: String) {
    def sayHi: Unit = println(s"Hi my name is $name how can I help?")
  }

  val jim = new Person("Jim") {
    override def sayHi: Unit = println("Hi my name is Jim how can I be of service?")
  }

  println(jim.getClass)

  /*  => here what is actually happen behind the scenes for instantiate when overriding (method or fields) abstract and non-abstract class
    RULES:
    1. pass in required constructor arguments if needed
    2. implement all abstract fields/methods
  * */

  /*
  Exercise - expand MyList with new features

  * */
}
