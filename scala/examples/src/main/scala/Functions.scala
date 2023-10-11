object Functions:
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
case class User(name: String, age: Int)
enum Category:
  case Young, Adult, Old
val categoryByMaxAges: Map[Int, Category] = Map(
  24 -> Category.Young,
  64 -> Category.Adult,
  Integer.MAX_VALUE -> Category.Old
)
val discountByCategories: Map[Category, Int] = Map(
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
