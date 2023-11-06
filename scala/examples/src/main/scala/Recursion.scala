import scala.annotation.tailrec

object Recursion:
  def main(args: Array[String]): Unit =
    val input = """34
                  |84
                  |12
                  |
                  |32
                  |28
                  |9
                  |7""".stripMargin
    println(sums(input.linesIterator.toList))

@tailrec
def sums(
    input: List[String],
    acc: Seq[Long] = Vector.empty,
    sum: Long = 0
): Seq[Long] =
  input match
    case Nil        => acc :+ sum
    case "" :: tail => sums(tail, acc :+ sum)
    case n :: tail  => sums(tail, acc, sum + n.toLong)
