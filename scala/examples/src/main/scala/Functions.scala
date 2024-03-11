object Functions:
  sealed trait TaskStatusCreated
  sealed trait TaskStatusInProgress
  sealed trait TaskStatusDone
  enum TaskStatus:
    case Created extends TaskStatus with TaskStatusCreated
    case Started extends TaskStatus with TaskStatusInProgress
    case InReview extends TaskStatus with TaskStatusInProgress
    case Done extends TaskStatus with TaskStatusDone
  def transition(status: TaskStatusCreated | TaskStatusInProgress): TaskStatusInProgress | TaskStatusDone = status match
    case TaskStatus.Created => TaskStatus.Started
    case TaskStatus.Started => TaskStatus.InReview
    case TaskStatus.InReview => TaskStatus.Done
  def main(args: Array[String]): Unit =
    println(
      computePrice(
        User("Emma", 16),
        categoryByMaxAges,
        1999,
        discountByCategories
      )
    )
    println(
      computePriceWithComposition(
        User("Emma", 16),
        categoryByMaxAges,
        1999,
        discountByCategories
      )
    )
    println(
      computePrice(
        User("Nicolas", 41),
        categoryByMaxAges,
        5500,
        discountByCategories
      )
    )
    println(
      computePriceWithComposition(
        User("Nicolas", 41),
        categoryByMaxAges,
        5500,
        discountByCategories
      )
    )
    println(
      computePrice(
        User("Martine", 67),
        categoryByMaxAges,
        4999,
        discountByCategories
      )
    )
    println(
      computePriceWithComposition(
        User("Martine", 67),
        categoryByMaxAges,
        4999,
        discountByCategories
      )
    )
    logBeforeAndAfter("hello", () => { println("Hello World!") })
    logBeforeAndAfterResult("hello", message => message.length)
    println(operationToCompute("add")(3, 2))
    println(operationToCompute("subtract")(3, 2))
    println(operationToCompute("multiply")(3, 2))
    println(operationToCompute("divide")(3, 2))

case class User(name: String, age: Int)
enum Category:
  case Young, Adult, Old
val categoryByMaxAges: Map[Int, Category] =
  Map(
    24 -> Category.Young,
    64 -> Category.Adult,
    Integer.MAX_VALUE -> Category.Old
  )
val discountByCategories: Map[Category, Int] =
  Map(
    Category.Young -> 25,
    Category.Adult -> 0,
    Category.Old -> 35
  )
def computePrice(
    user: User,
    categories: Map[Int, Category],
    price: Long,
    discounts: Map[Category, Int]
): Long = price - price * discounts(
  categories
    .filter { case (maxAge, category) => maxAge >= user.age }
    .minBy { case (maxAge, category) => maxAge }
    ._2
) / 100
def computePriceWithComposition(
    user: User,
    categories: Map[Int, Category],
    price: Long,
    discounts: Map[Category, Int]
): Long =
  computePrice(
    price = price,
    discount = computeDiscount(user, categories, discounts)
  )
def computePrice(price: Long, discount: Long): Long =
  price - price * discount / 100
def computeDiscount(
    user: User,
    categories: Map[Int, Category],
    discounts: Map[Category, Int]
): Long = discounts(findCategory(user, categories))
def findCategory(
    user: User,
    categories: Map[Int, Category]
): Category =
  val (_, category) = categories
    .filter { case (maxAge, category) => maxAge >= user.age }
    .minBy { case (maxAge, category) => maxAge }
  category

def logBeforeAndAfter(message: String, task: () => Unit): Unit =
  println(s"before $message")
  task()
  println(s" after $message")

def logBeforeAndAfterResult(
    message: String,
    computeResult: String => Long
): Unit =
  println(s"before $message")
  println(s"result ${computeResult(message)}")
  println(s" after $message")

def operationToCompute(operation: String): (Long, Long) => Long =
  operation match
    case "add"      => (left, right) => left + right
    case "subtract" => (left, right) => left - right
    case "multiply" => (left, right) => left * right
    case _          => (left, right) => Long.MinValue
