package lectures.basics.part1basics

object ValuesVariablesTypes extends App {

  val x = 42
  println(x)
  // VALS are immutable
  // TYPES OF VAL's are optional (compiler can infer types)
  val aString: String = "hello!"

  val aBoolean: Boolean = true
  val aChar: Char = 'a'
  val aInt: Int = x
  val aShort: Short = 211 // represantation on 2 bytes instead of 4 bytes
  val aLong: Long = 2111111111L// 8 bytes instead of (Int) 4 bytes
  val aFloat: Float = 2.0f
  val aDouble: Double = 3.14

  // variables
  var aVariable: Int = 4
  aVariable = 5 // side effects
  // prefer vals over vars
}
