package todo

import munit.{FailException, FunSuite, Location}
import todo.Item.*

import scala.util.Success

class ItemSuite extends FunSuite:

  test("should validate imperative"):
    assertEquals(validateDescriptionImperative("new item"), "new item")
    assertEquals(validateDescriptionImperative(" to trim  "), "to trim")
    interceptMessage[IllegalArgumentException]("description can’t be empty")(validateDescriptionImperative(" "))

  test("should validate unsafe"):
    assertEquals(validateDescriptionUnsafe("new item"), "new item")
    assertEquals(validateDescriptionUnsafe(" to trim  "), "to trim")
    interceptMessage[IllegalArgumentException]("description can’t be empty")(validateDescriptionUnsafe(" "))

  test("should validate try"):
    assertEquals(validateDescriptionTry("new item"), Success("new item"))
    assertEquals(validateDescriptionTry(" to trim  "), Success("to trim"))
    assert(validateDescriptionTry(" ").isFailure)
    assert(validateDescriptionTry(" ").failed.get.isInstanceOf[IllegalArgumentException])
    assertEquals(validateDescriptionTry(" ").failed.get.getMessage, "description can’t be empty")

  test("should validate result"):
    assertEquals(validateDescriptionResult("new item"), DescriptionValidationResult.Value("new item"))
    assertEquals(validateDescriptionResult(" to trim  "), DescriptionValidationResult.Value("to trim"))
    assertEquals(validateDescriptionResult(" "), DescriptionValidationResult.Empty)

  test("should validate"):
    assertEquals(validateDescription("new item"), Right("new item"))
    assertEquals(validateDescription(" to trim  "), Right("to trim"))
    assert:
      validateDescription(" ").left.getOrElse(new FailException("not a left", summon[Location])).isInstanceOf[IllegalArgumentException]
    assertEquals(
      validateDescription(" ").left.getOrElse(new FailException("not a left", summon[Location])).getMessage,
      "description can’t be empty"
    )

  test("should validate errors"):
    assertEquals(validateDescriptionErrors("new item"), Right("new item"))
    assertEquals(validateDescriptionErrors(" to trim  "), Right("to trim"))
    assertEquals(validateDescriptionErrors(" "), Left(DescriptionError.Empty))
