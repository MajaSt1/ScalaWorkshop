package lectures.advanced.part2as.concurrency

import java.util.concurrent.Executors

object IntroToConcurrency extends App {

  // JVM threads
  val runnable = new Runnable {
    override def run(): Unit = println("Running in parallel")
  }

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("Running in parallel")
  })

  // JVM thread vs instance thread
  aThread.start() // gives the signal to the JVM to start a JVM thread
  // create a JVM thread => OS thread

  //  runnable.run() // doesn't do anything in parallel

  aThread.join() // blocks until aThread finishes running - call to make sure that thread is already run before we do some computation

  val threadHello = new Thread(() => (1 to 5).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() => (1 to 5).foreach(_ => println("goodbye")))
  //  threadHello.start()
  //  threadGoodbye.start()
  // different runs produce different results !

  // threads are expensive to: start and kill. Solution: executors = reuse threads
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(() => println("something in the thread pool")) // we don't need to care about start and stop threads
  pool.execute(() => {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(() => {
    Thread.sleep(1000)
    println("almost done")
    Thread.sleep(1000)
    println("done after 2 seconds")
  })

  //  pool.shutdown() // none action can be executed any more - this throws an exception in the calling thread
  //  pool.execute(() => println("should not appear"))

  //  pool.shutdownNow()
  //  println(pool.isShutdown) // - this throws an exception in the calling thread


  // thread can acts in predictical way:

}
