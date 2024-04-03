import SumAll.{firstAndLastNumber, sumAll, toNumber}
import munit.FunSuite

class SumAllSuite extends FunSuite:

  test("toNumber"):
    assertEquals(toNumber("two1nine"), "219")
    assertEquals(toNumber("eightwothree"), "8wo3")
    assertEquals(toNumber("abcone2threexyz"), "abc123xyz")
    assertEquals(toNumber("xtwone3four"), "x2ne34")
    assertEquals(toNumber("4nineeightseven2"), "49872")
    assertEquals(toNumber("zoneight234"), "z1ight234")
    assertEquals(toNumber("7pqrstsixteen"), "7pqrst6teen")

  test("firstAndLastNumber"):
    assertEquals(firstAndLastNumber("219"),         "29")
    assertEquals(firstAndLastNumber("8wo3"),        "83")
    assertEquals(firstAndLastNumber("abc123xyz"),   "13")
    assertEquals(firstAndLastNumber("x2ne34"),      "24")
    assertEquals(firstAndLastNumber("49872"),       "42")
    assertEquals(firstAndLastNumber("z1ight234"),   "14")
    assertEquals(firstAndLastNumber("7pqrst6teen"), "76")

  test("sumAll"):
    assertEquals(sumAll("""two1nine
                          |eightwothree
                          |abcone2threexyz
                          |xtwone3four
                          |4nineeightseven2
                          |zoneight234
                          |7pqrstsixteen""".stripMargin), 281)
