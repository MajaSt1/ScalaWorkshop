package lectures.part1basics

import scala.annotation.tailrec

object DefaultArgs extends App {

  // its useful when you called function with the same parameter many times
  @tailrec
  def trFact(n: Int, acc: Int = 1): Int =
    if (n <= 1) acc
    else trFact(n - 1, n * acc)

  val fact10 = trFact(10, 2)

  def savePicture(format: String = "jpg", width: Int = 1920, height: Int = 1080): Unit = println("saving picture")

  savePicture(width = 800)
  /*
    1. pass in every leading argument
    2. name the arguments
  */

  // bmp- bit map
  savePicture(height = 600, format = "bmp", width = 800)
}
