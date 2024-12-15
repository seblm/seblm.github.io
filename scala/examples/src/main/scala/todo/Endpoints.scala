package todo

import cats.effect.IO
import io.circe.generic.auto.*
import sttp.model.StatusCode.{Created, NoContent}
import sttp.tapir.*
import sttp.tapir.generic.auto.*
import sttp.tapir.json.circe.*
import sttp.tapir.server.ServerEndpoint
import sttp.tapir.swagger.bundle.SwaggerInterpreter

import java.util.UUID
import scala.collection.mutable

object Endpoints:
  private val items: Items = new Items()

  private val itemsGet: PublicEndpoint[Unit, Unit, List[Item], Any] = endpoint.get.in("api" / "items").out(jsonBody[List[Item]])
  val itemsGetServerEndpoint: ServerEndpoint[Any, IO] = itemsGet.serverLogicSuccess(_ => IO.pure(items.list))
  private val itemsPost: PublicEndpoint[NewItem, Unit, Item, Any] =
    endpoint.post.in("api" / "items").in(jsonBody[NewItem]).out(jsonBody[Item]).out(statusCode(Created))
  val itemsPostServerEndpoint: ServerEndpoint[Any, IO] = itemsPost.serverLogicSuccess(items.addOne)
  private val itemsPatch: PublicEndpoint[(UUID, ItemToBeUpdated), Unit, Item, Any] =
    endpoint.patch.in("api" / "items" / path[UUID]("item-id")).in(jsonBody[ItemToBeUpdated]).out(jsonBody[Item])
  val itemsPatchServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatch.serverLogicSuccess((itemId, itemToBeUpdated) => items.update(itemId, itemToBeUpdated))
  private val itemsPatchAllCompleted: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.patch.in("api" / "items" / "all" / "completed").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsPatchAllCompletedServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatchAllCompleted.serverLogicSuccess(itemIds => IO.pure(items.allCompleted(itemIds)))
  private val itemsPatchAllUncompleted: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.patch.in("api" / "items" / "all" / "uncompleted").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsPatchAllUncompletedServerEndpoint: ServerEndpoint[Any, IO] =
    itemsPatchAllUncompleted.serverLogicSuccess(itemIds => IO.pure(items.allUncompleted(itemIds)))
  private val itemsDelete: PublicEndpoint[UUID, Unit, Unit, Any] =
    endpoint.delete.in("api" / "items" / path[UUID]("item-id")).out(statusCode(NoContent))
  val itemsDeleteServerEndpoint: ServerEndpoint[Any, IO] = itemsDelete.serverLogicSuccess(itemId => IO.pure(items.delete(itemId)))
  private val itemsDeleteAll: PublicEndpoint[List[UUID], Unit, Unit, Any] =
    endpoint.delete.in("api" / "items" / "all" / "deleted").in(jsonBody[List[UUID]]).out(statusCode(NoContent))
  private val itemsDeleteAllServerEndpoint: ServerEndpoint[Any, IO] =
    itemsDeleteAll.serverLogicSuccess(itemIds => IO.pure(items.allDelete(itemIds)))
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
