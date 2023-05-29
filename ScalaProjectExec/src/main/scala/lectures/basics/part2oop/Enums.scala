package lectures.basics.part2oop

object Enums {

  enum Permissions { // sealed data type that cannot be extended
    case READ, WRITE, EXECUTE, NONE

    // add fields/methods
    def openDocument(): Unit =
      if (this == READ) println("opening document...")
      else println("reading not allowed.")
  }


  val somePermissions: Permissions = Permissions.READ

  //constructor args
  enum PermissionWithBits(bits: Int) {
    case READ extends PermissionWithBits(4) // 100
    case WRITE extends PermissionWithBits(2) // 010
    case EXECUTE extends PermissionWithBits(1) // 001
    case NONE extends PermissionWithBits(0) // 000
  }

  /* in java:
  * enum Thing {
      case A(2), B(3)...
  }
  * */

  object PermissionWithBits { // can use as factory method
    def fromBits(bits: Int): PermissionWithBits = // whatever
      PermissionWithBits.NONE
  }

  //standard API
  val somePermissionsOrdinal: Int = somePermissions.ordinal
  val allPermissions: Any = PermissionWithBits.values // array of all possible values of the enum
  val readPermission: Permissions = Permissions.valueOf("READ") // Permissions.READ

  def main(args: Array[String]): Unit = {
    somePermissions.openDocument()
    println(somePermissionsOrdinal)
    println(allPermissions)
  }
}
