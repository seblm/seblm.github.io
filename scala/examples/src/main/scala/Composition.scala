import scala.util.chaining.given

object Composition:

  private def times2(value: Int): Int = value * 2

  private def addOne(value: Int): Int = value + 1

  private def times2PlusOne(value: Int): Int = addOne(times2(value))
  private def withCompose(value: Int): Int = addOne.compose(times2)(value)
  private def withAndThen(value: Int): Int = times2.andThen(addOne)(value)
  private def withPipe(value: Int): Int = value.pipe(times2).pipe(addOne)

  @main def compositionMain(): Unit =
    println(times2PlusOne(5)) // 11
    println(withCompose(5)) // 11
    println(withAndThen(5)) // 11
    println(withPipe(5)) // 11
