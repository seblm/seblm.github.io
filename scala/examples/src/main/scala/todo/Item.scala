package todo

import java.util.UUID
import scala.util.{Failure, Success, Try}

case class Item(id: UUID, description: String, completed: Boolean)

object Item:
  def validateDescription(description: String): Either[Throwable, String] = description.trim match
    case ""                         => Left(new IllegalArgumentException("description can’t be empty"))
    case nonEmptyTrimmedDescription => Right(nonEmptyTrimmedDescription)
  def validateDescriptionUnsafe(description: String): String = description.trim match
    case ""                         => throw new IllegalArgumentException("description can’t be empty")
    case nonEmptyTrimmedDescription => nonEmptyTrimmedDescription
  def validateDescriptionTry(description: String): Try[String] = description.trim match
    case ""                         => Failure(new IllegalArgumentException("description can’t be empty"))
    case nonEmptyTrimmedDescription => Success(nonEmptyTrimmedDescription)
