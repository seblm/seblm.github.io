import scala.util.Try
import scala.jdk.StreamConverters.*

object SumAll:

  private def firstNumber(value: String): String = value match
    case ""                                  => ""
    case s if Try(s.take(1).toInt).isSuccess => s.take(1)
    case s                                   => firstNumber(s.drop(1))

  private def lastNumber(value: String): String = firstNumber(value.reverse)

  def firstAndLastNumber(value: String): String = firstNumber(value) + lastNumber(value)

  def toNumber(value: String): String = value match
    case s if s.startsWith("one")   => "1" + toNumber(s.drop(3))
    case s if s.startsWith("two")   => "2" + toNumber(s.drop(3))
    case s if s.startsWith("three") => "3" + toNumber(s.drop(5))
    case s if s.startsWith("four")  => "4" + toNumber(s.drop(4))
    case s if s.startsWith("five")  => "5" + toNumber(s.drop(4))
    case s if s.startsWith("six")   => "6" + toNumber(s.drop(3))
    case s if s.startsWith("seven") => "7" + toNumber(s.drop(5))
    case s if s.startsWith("eight") => "8" + toNumber(s.drop(5))
    case s if s.startsWith("nine")  => "9" + toNumber(s.drop(4))
    case ""                         => ""
    case s                          => s.take(1) + toNumber(s.drop(1))

  def sumAll(values: String): Int =
    values
      .lines()
      .toScala(Vector)
      .map(toNumber)
      .map(firstAndLastNumber)
      .map(_.toInt)
      .sum
