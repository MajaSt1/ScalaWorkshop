package lectures.basics.part2oop

object OOBasics extends App {
  val person = new Person("David", 26)
  println(person.x)
  person.greet("Daniel")
  person.greet()

  val author = new Writer("Charles", "Dickens", 1812)
  val imposter = new Writer("Charles", "Dickens", 1812)
  val novel = new Novel("Great Exp", 1861, author)

  println(novel.authorAge)
  println(novel.isWrittenBy(imposter))

  val counter = new Counter
  counter.inc.inc.inc.print
  counter.inc(5).print
}

// class parameters are NOT FIELDS
class Person(name: String, val age: Int = 0) {
  val x = 2

  println(1 + 3)

  def greet(name: String): Unit = println(s"${this.name} says: Hi, $name")

  // overloading
  def greet(): Unit = println(s"Hi, I am $name")

  // multiple constructors (overloading constructors)
  def this(name: String) = this(name, 0) // the other constructor can have only primary/secondary constructor

  def this() = this("John Doe")
}

class Writer(firstName: String, surname: String, val year: Int) {
  def fullName: String = firstName + " " + surname
}

class Novel(name: String, year: Int, author: Writer) {
  def authorAge = year - author.year

  def isWrittenBy(author: Writer) = author == this.author

  def copy(newYear: Int): Novel = new Novel(name, newYear, author)
}

class Counter(val count: Int = 0) {
  def inc = {
    println("incrementing")
    new Counter(count + 1)
  } // immutability

  def dec = new Counter(count - 1)

  // TODO - important !!!
  def inc(n: Int): Counter = {
    if (n <= 0) this
    else inc.inc(n - 1)
  }

  def print = println(count)
}