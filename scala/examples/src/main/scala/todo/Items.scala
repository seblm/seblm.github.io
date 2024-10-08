package todo

import cats.effect.IO

import java.util.UUID
import scala.annotation.tailrec
import scala.collection.mutable

class Items:
  private val fakeId = UUID.fromString("fc03741d-1c27-4afd-88ab-03e085980a36")
  val items: mutable.ArrayBuffer[Item] = mutable.ArrayBuffer(Item(fakeId, "learn Scala", false))
  def list: List[Item] = items.toList
  def addOne(newItem: NewItem): IO[Item] =
    for
      nonEmptyDescription <- IO.fromEither(Item.validateDescription(newItem.description))
      id <- IO.randomUUID
      item = Item(id, nonEmptyDescription, false)
      _ <- IO(items.addOne(item))
    yield item
  def update(itemId: UUID, itemToBeUpdated: ItemToBeUpdated): IO[Item] =
    for
      (item, index) <- IO.fromOption(items.zipWithIndex.find(_._1.id == itemId))(new NoSuchElementException(s"item $itemId not found"))
      nonEmptyDescription <- itemToBeUpdated.description.fold(IO.pure(item.description)): newDescription =>
        IO.fromEither(Item.validateDescription(newDescription))
      updatedItem = item.copy(description = nonEmptyDescription, completed = itemToBeUpdated.completed.getOrElse(item.completed))
      _ <- IO(items.update(index, updatedItem))
    yield updatedItem
  def delete(itemId: UUID): Unit = items.zipWithIndex.find(_._1.id == itemId).map(_._2).foreach(items.remove)
  @tailrec final def allDelete(itemIds: List[UUID]): Unit = itemIds match
    case Nil => ()
    case head :: tail =>
      delete(head)
      allDelete(tail)
  def allCompleted(itemIds: List[UUID]): Unit = applyToAllItems(itemIds, _.copy(completed = true))
  def allUncompleted(itemIds: List[UUID]): Unit = applyToAllItems(itemIds, _.copy(completed = false))
  private def applyToAllItems(itemIds: List[UUID], updateItem: Item => Item): Unit = items.zipWithIndex
    .filter((item, _) => itemIds.contains(item.id))
    .foreach((item, index) => items.update(index, updateItem(item)))
