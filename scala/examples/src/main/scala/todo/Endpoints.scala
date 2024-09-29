package todo

import cats.effect.IO
import io.circe.generic.auto.*
import sttp.model.StatusCode.{Created, NoContent}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter
import todo.Endpoints.{Item, ItemToBeUpdated, NewItem}

import java.util.UUID
import scala.annotation.tailrec
import scala.collection.mutable

object Endpoints:
  case class Item(id: UUID, description: String, completed: Boolean)
  case class NewItem(description: String)
  case class ItemToBeUpdated(description: Option[String], completed: Option[Boolean])

  private val itemsGet: PublicEndpoint[Unit, Unit, List[Item], Any] = endpoint.get.in("api" / "items").out(jsonBody[List[Item]])
  val itemsGetServerEndpoint: ServerEndpoint[Any, IO] = itemsGet.serverLogicSuccess(_ => IO.pure(Items.list))
  private val itemsPost: PublicEndpoint[NewItem, Unit, Item, Any] =
    endpoint.post.in("api" / "items").in(jsonBody[NewItem]).out(jsonBody[Item]).out(statusCode(Created))
  val itemsPostServerEndpoint: ServerEndpoint[Any, IO] = itemsPost.serverLogicSuccess(newItem => IO.pure(Items.append(newItem)))
  private val itemsPatch: PublicEndpoint[(UUID, ItemToBeUpdated), Unit, Item, Any] =
    endpoint.patch.in("api" / "items" / path[UUID]("item-id")).in(jsonBody[ItemToBeUpdated]).out(jsonBody[Item])
  val itemsPatchServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatch.serverLogicSuccess((itemId, itemToBeUpdated) => IO.pure(Items.update(itemId, itemToBeUpdated)))
  private val itemsPatchAllCompleted: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.patch.in("api" / "items" / "all" / "completed").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsPatchAllCompletedServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatchAllCompleted.serverLogicSuccess(itemIds => IO.pure(Items.allCompleted(itemIds)))
  private val itemsPatchAllUncompleted: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.patch.in("api" / "items" / "all" / "uncompleted").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsPatchAllUncompletedServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatchAllUncompleted.serverLogicSuccess(itemIds => IO.pure(Items.allUncompleted(itemIds)))
  private val itemsDelete: PublicEndpoint[UUID, Unit, Unit, Any] =
    endpoint.delete.in("api" / "items" / path[UUID]("item-id")).out(statusCode(NoContent))
  val itemsDeleteServerEndpoint: ServerEndpoint[Any, IO] = itemsDelete.serverLogicSuccess(itemId => IO.pure(Items.delete(itemId)))
  private val itemsDeleteAll: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.delete.in("api" / "items" / "all" / "deleted").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsDeleteAllServerEndpoint: ServerEndpoint[Any, IO] =
    itemsDeleteAll.serverLogicSuccess(itemIds => IO.pure(Items.allDelete(itemIds)))
  private val apiEndpoints: List[ServerEndpoint[Any, IO]] = List(
    itemsGetServerEndpoint,
    itemsPostServerEndpoint,
    itemsPatchServerEndpoint,
    itemsPatchAllCompletedServerEndpoint,
    itemsPatchAllUncompletedServerEndpoint,
    itemsDeleteServerEndpoint,
    itemsDeleteAllServerEndpoint
  )

  private val docEndpoints: List[ServerEndpoint[Any, IO]] = SwaggerInterpreter().fromServerEndpoints[IO](apiEndpoints, "todo", "1.0.0")

  val all: List[ServerEndpoint[Any, IO]] = apiEndpoints ++ docEndpoints

object Items:
  private val fakeId = UUID.fromString("fc03741d-1c27-4afd-88ab-03e085980a36")
  val items: mutable.ArrayBuffer[Item] = mutable.ArrayBuffer(Item(fakeId, "learn Scala", false))
  def list: List[Item] = items.toList
  def append(newItem: NewItem): Item =
    val item = Item(UUID.randomUUID(), newItem.description, false)
    items.addOne(item)
    item
  def update(itemId: UUID, itemToBeUpdated: ItemToBeUpdated): Item =
    items.zipWithIndex
      .find(_._1.id == itemId)
      .map: (item, index) =>
        val updatedItem = item.copy(
          description = itemToBeUpdated.description.getOrElse(item.description),
          completed = itemToBeUpdated.completed.getOrElse(item.completed)
        )
        items.update(index, updatedItem)
        updatedItem
      .getOrElse(items.head) // not safe
  def delete(itemId: UUID): Unit = items.zipWithIndex.find(_._1.id == itemId).map(_._2).foreach(items.remove)
  @tailrec def allDelete(itemIds: List[UUID]): Unit = itemIds match
    case Nil => ()
    case head :: tail =>
      delete(head)
      allDelete(tail)
  def allCompleted(itemIds: List[UUID]): Unit = applyToAllItems(itemIds, _.copy(completed = true))
  def allUncompleted(itemIds: List[UUID]): Unit = applyToAllItems(itemIds, _.copy(completed = false))
  private def applyToAllItems(itemIds: List[UUID], updateItem: Item => Item): Unit = items.zipWithIndex
    .filter((item, _) => itemIds.contains(item.id))
    .foreach((item, index) => items.update(index, updateItem(item)))
