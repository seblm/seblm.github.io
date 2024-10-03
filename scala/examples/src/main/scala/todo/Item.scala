package todo

import java.util.UUID
import scala.util.{Failure, Success, Try}

case class Item(id: UUID, description: String, completed: Boolean)

object Item:
  def validateDescriptionImperative(description: String): String =
    if description.trim.isBlank then throw new IllegalArgumentException("description can’t be empty") else description.trim
  def validateDescriptionUnsafe(description: String): String = description.trim match
    case ""                         => throw new IllegalArgumentException("description can’t be empty")
    case nonEmptyTrimmedDescription => nonEmptyTrimmedDescription
  def validateDescriptionTry(description: String): Try[String] = description.trim match
    case ""                         => Failure(new IllegalArgumentException("description can’t be empty"))
    case nonEmptyTrimmedDescription => Success(nonEmptyTrimmedDescription)
  enum DescriptionValidationResult:
    case Empty
    case Value(description: String)
  def validateDescriptionResult(description: String): DescriptionValidationResult = description.trim match
    case ""                         => DescriptionValidationResult.Empty
    case nonEmptyTrimmedDescription => DescriptionValidationResult.Value(nonEmptyTrimmedDescription)
  def validateDescription(description: String): Either[Throwable, String] = description.trim match
    case ""                         => Left(new IllegalArgumentException("description can’t be empty"))
    case nonEmptyTrimmedDescription => Right(nonEmptyTrimmedDescription)
  enum DescriptionError:
    case Empty
  def validateDescriptionErrors(description: String): Either[DescriptionError, String] = description.trim match
    case ""                         => Left(DescriptionError.Empty)
    case nonEmptyTrimmedDescription => Right(nonEmptyTrimmedDescription)
