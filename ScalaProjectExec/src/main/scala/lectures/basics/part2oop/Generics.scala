package lectures.basics.part2oop

object Generics extends App {

  class MyList[+A] {
    // use the type A
    //    def add(element: A): MyList[A] = ??? // compiler throws error
    def add[B >: A](element: B): MyList[B] = ??? // B is supertype of element A
    /*
    A = Cat
    B = Dog = Animal
  */
  }

  class MyMap[Key, Value]
  // traits can also be type parametrized

  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  // generic methods
  // companion object
  object MyList {
    def empty[A]: MyList[A] = ???
  }

  val emptyListOfIntegers = MyList.empty[Int]


  // variance problem
  class Animal

  class Cat extends Animal

  class Dog extends Animal

  // 1. yes - List[Cat] extends List[Animal] = COVARIANCE
  class CovariantList[+A]

  val animal: Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]

  // animalList.add(new Dog) ??? HARD QUESTION. -> adding a Dog to a list of cats turns it into list of animals => we return a list of Animals


  // 2. no - List[Cat] and List[Animal] are two different things = INVARIANCE
  class InvariantList[A]

  val invariantAnimalList: InvariantList[Animal] = new InvariantList[Animal]

  // 3. Hell, no! = CONTRAVARIANT
  //  class ContravariantList[-A]
  //  val contravariantList: ContravariantList[Cat] = new ContravariantList[Animal]

  class Trainer[-A]

  val contravariantList: Trainer[Cat] = new Trainer[Animal]

  //bounded types
  class Cage[A <: Animal](animal: A) //read: type A accepts only subtypes of Animal

  class Cage1[A >: Animal](animal: A) //read: type A accepts only supertype of Animal

  val cage = new Cage(new Dog)

  //  class Car
  //  val newCage = new Cage(new Car) // not allowed!
 

  // EXERCISE : expend MyList to be generic

}
