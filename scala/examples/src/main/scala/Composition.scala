object Composition:

  def times2(value: Int): Int = value * 2

  def addOne(value: Int): Int = value + 1

  def times2PlusOne(value: Int): Int = addOne(times2(value))
  def withCompose(value: Int): Int   = addOne.compose(times2)(value)
  def withAndThen(value: Int): Int   = times2.andThen(addOne)(value)

  @main def compositionMain(): Unit =
    println(times2PlusOne(5)) // 11
    println(withCompose(5))   // 11
    println(withAndThen(5))   // 11
