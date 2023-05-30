package lectures.advanced.part2as.concurrency

import scala.collection.mutable
import scala.util.Random

object JVMThreadCommunication extends App {

  /*
   the producer-consumer problem

  producer -> [ x ] -> consumer
  */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int) = value = newValue

    def get = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting ...")
      while (container.isEmpty) {
        println("[consumer] actively waiting ...")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing ...")
      Thread.sleep(500)
      val value = 42
      println("[producer] I have produced, after long work, the value " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //  naiveProdCons()

  //Synchronized
  // Entering a synchronized expression on an object locks the object
  val someObject = "hello"
  someObject.synchronized { // lock the object's monitor - data structure is internally used by the JVM to keep track of which object is locked by which tread
    // any other thread trying to run this will block
  } // release the lock
  // Only AnyRefs can have synchronized blocks.
  // General principles:
  // - make no assumptions about who gets the lock first
  // - keep locking to a minimum
  // - maintain thread safety at ALL times in parallel/multi-threaded applications

  // wait and notify
  /* wait()-ing on an object's monitor suspends you (the thread) indefinitely

  // thread 1
  val obj = "hello"
  obj.synchronized { // lock the object's monitor
    obj.wait()  // release the lock... and wait
  } // when allowed to proceed, lock the monitor again and continue

  //thread 2
    obj.synchronized { // lock the object's monitor
      obj.notify()  // signal ONE sleeping thread they may continue
    } // when allowed to proceed, lock the monitor again and continue
  */

  def smartProdCons(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("[consumer] waiting...")
      container.synchronized {
        container.wait()
      }

      // container must have some value
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] Hard at work...")
      Thread.sleep(2000)
      val value = 42
      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  //  smartProdCons()

  /*
    producer -> [ ? ? ? ] -> consumer
  */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]()
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          // hey consumer, new food for you
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)

          // hey producer, there's empty space available, are you lazy?
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(250))
      }
    })

    consumer.start()
    producer.start()
  }

  //  prodConsLargeBuffer()

  /*
  Prod-cons, level 3
    producer1 -> [ ? ? ? ] -> consumer1
    producer2 -----^     ^---- consumer2
  */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          /*
            producer produces value, two Cons are waiting
            notifies ONE consumer, notifies on buffer
            notifies the other consumer
          */
          while (buffer.isEmpty) {
            println(s"[consumer#$id] buffer empty, waiting...")
            buffer.wait()
          } // now i need to both be alive and buffer be not empty
          // if someone wakes me up i check if buffer is empty !!!!!

          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer#$id] consumed " + x)

          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer#$id] buffer is full, waiting...")
            buffer.wait()
          }

          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer#$id] producing " + i)
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(250))
      }
    }
  }

  def multiProdCons(nConsumer: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]()
    val capacity = 3

    (1 to nConsumer).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())
  }

  //  multiProdCons(3,3)

  /*
  Exercises.
  1) think of an example where notifyAll acts in a different way than notify?
  // The behavior doesnt change in terms of racing, but using notifyAll prevents
  a possible deadlock:

  EXAMPLE: 10 producers, 2 consumers, buffer size = 3
  1. One producer fills the buffer quickly. The other 9 go to sleep. Same with the open producer when it's done.
  2. One consumer consumes all, then goes to sleep. The others go to sleep once they see buffer empty.
  3. After 3 notifications (buffer size = 3), 3 producers wake up, fill the space. Notifications go to other producers.
  4. Every poor producer sees buffer full, goes to sleep.
  5. Deadlock.

  notifyAll fixes this.

  In racing this behavior doesnt change because of synchronized on buffer and that one thread may wake up the consumer

  http://www.pzielinski.com/?p=998
  2) create deadlock = one thread or multiple threads block each other and they cannot continue
  3) create livelock
  */

  // 1)
  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i => new Thread(() => {
      bell.synchronized {
        println(s"[thread $i] waiting...")
        bell.wait()
        println(s"[thread $i] hooray!")
      }
    }).start())

    new Thread(() => {
      Thread.sleep(2000)
      println("[announcer] Rock'n roll!")
      bell.synchronized {
        bell.notifyAll() // vs notify
      }
    }).start()
  }

  //  testNotifyAll()

  // 2) - deadlock = block threads

  case class Friend(name: String) {
    def bow(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I am bowing to my friend $other")
        other.rise(this)
        println(s"$this: my friend $other has risen")
      }
    }

    def rise(other: Friend): Unit = {
      this.synchronized {
        println(s"$this: I am rising to my friend $other")
      }
    }

    var side = "right"

    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"$this: Oh, but please, $other, feel free to pass..")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("Sam")
  val bob = Friend("Bob")

  //  new Thread(() => {
  //    sam.bow(bob)
  //  }).start() // sam's lock | bob's lock
  //
  //  new Thread(() => {
  //    bob.bow(sam)
  //  }).start() // bob's lock | sam's lock


  // 3) livelock = no threads are blocked (we don't have synchronized blocks), but no threads are free to run
  new Thread(() => sam.pass(bob)).start()
  new Thread(() => bob.pass(sam)).start()

}
