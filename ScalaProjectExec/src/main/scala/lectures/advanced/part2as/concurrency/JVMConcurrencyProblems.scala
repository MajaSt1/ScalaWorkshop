package lectures.advanced.part2as.concurrency

object JVMConcurrencyProblems {

  // variables are root of almost all evil in parallel and distributed impl

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()

    println(x) // race condition -> mutable variables
  }

  case class BankAccount(var amount: Int)

  def buy(account: BankAccount, thing: String, price: Int): Unit = {
    /*
    * involves 3 steps:
     - read old value
     - compute result
     - write new value
    * */
    account.amount -= price
  }

  // solutions : make steps atomic
  def buySafe(account: BankAccount, thing: String, price: Int): Unit = {
    account.synchronized { // does not allow multiple threads to run the critical section AT THE SAME TIME
      account.amount -= price // critical section
    }
  }

  /*
    Example race condition:
    thread1 (shoes)
      - reads amount 50000
      - compute result 50000 - 3000 = 47000
    thread2 (iPhone)
      - reads amount 50000
      - compute result 50000 - 4000 = 46000
    thread1 (shoes)
      - write amount 47000
    thread (iPhone)
      - write amount 46000
  */
  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buy(account, "shoes", 3000))
      val thread2 = new Thread(() => buy(account, "iPhone", 4000))
      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()
      if (account.amount != 43000) println(s"AHA! Ive just broken the bank: ${account.amount}")
    }
  }

  /* EXERCISE:
  1 - create "inception threads"
    thread1
      -> thread 2
        -> thread 3
            .....
  each thread prints "hello from thread $i"
  Print all messages IN REVERSE ORDER

  2 - whats the max/min value of x

  3 - "sleep fallacy": whats the value of message?
  */
  // inception threads
  def inceptionThreads(maxThreads: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThreads) {
        val newThread = inceptionThreads(maxThreads, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"hello from thread $i")
    })

  // 2
  // we have steps : initiate
  /*
    max value = 100 - each thread increases x by 1
    min value = 1
      all threads read x = 0 at the same time
      all threads (in parallel) compute 0 + 1
      all threads try to write x = 1
  */
  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  // 3
  /*
  almost always, message = "Scala is awesome"
  it guaranteed ? NO
  Obnoxious situation (possible):

  main thread:
    message = "Scala sucks"
    awesomeThread.start()
    sleep(1001) - yields execution = processor (especially single core) will put the thread on hold , it will schedule some other thread for execution
  awesome thread:
    sleep(1000) - yields execution = so processor will take some other thread from OS
  OS gives the CPU to some important thread, takes > 2s
  OS gives the CPU back to the main thread
    main thread:
      println(message) // "Scala sucks"
    awesome thread:
      message = "Scala is awesome"
  */
  def demoSleepFallacy(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = " Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    // solution : join the worker thread
    awesomeThread.join()
    println(message)
  }

  def main(args: Array[String]): Unit = {
    //    runInParallel()
    //    demoBankingProblem()
    //    inceptionThreads(50).start()
    demoSleepFallacy()
  }
}
