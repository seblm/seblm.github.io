package todo

import cats.effect.IO
import cats.effect.unsafe.implicits.global
import io.circe.generic.auto.*
import munit.FunSuite
import sttp.client3.circe.*
import sttp.client3.testing.SttpBackendStub
import sttp.client3.{UriContext, basicRequest}
import sttp.model.StatusCode.NoContent
import sttp.tapir.integ.cats.effect.CatsMonadError
import sttp.tapir.server.stub.TapirStubInterpreter
import todo.Endpoints.*

import java.util.UUID

class EndpointsSuite extends FunSuite:

  private val learnScala = Item(UUID.fromString("fc03741d-1c27-4afd-88ab-03e085980a36"), "learn Scala", false)

  test("it should return items"):
    // given
    val backendStub =
      TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]())).whenServerEndpointRunLogic(itemsGetServerEndpoint).backend()

    // when
    val response = basicRequest.get(uri"https://example.com/api/items").response(asJson[List[Item]]).send(backendStub)

    // then
    response.map(response => assertEquals(response.body, Right(List(learnScala)))).unwrap

  test("it should add new items"):
    val backendStub =
      TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]())).whenServerEndpointRunLogic(itemsPostServerEndpoint).backend()

    val response = basicRequest.post(uri"https://example.com/api/items").body(NewItem("new item")).response(asJson[Item]).send(backendStub)
    response.map(response => assertEquals(response.body.map(_.description), Right("new item"))).unwrap

    val trimmed = basicRequest.post(uri"https://example.com/api/items").body(NewItem(" to trim  ")).response(asJson[Item]).send(backendStub)
    trimmed.map(trimmed => assertEquals(trimmed.body.map(_.description), Right("to trim"))).unwrap

    val empty = basicRequest.post(uri"https://example.com/api/items").body(NewItem(" ")).send(backendStub)
    empty.map(empty => assertEquals(empty.code.isSuccess, false)).unwrap

  test("it should update description"):
    // given
    val backendStub =
      TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]())).whenServerEndpointRunLogic(itemsPatchServerEndpoint).backend()

    // when
    val response = basicRequest
      .patch(uri"https://example.com/api/items/${learnScala.id}")
      .body(ItemToBeUpdated(description = Some("Learn Scala 3"), completed = None))
      .response(asJson[Item])
      .send(backendStub)

    // then
    response.map(response => assertEquals(response.body.map(_.description), Right("Learn Scala 3"))).unwrap

  test("it should update completed"):
    // given
    val backendStub =
      TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]())).whenServerEndpointRunLogic(itemsPatchServerEndpoint).backend()

    // when
    val response = basicRequest
      .patch(uri"https://example.com/api/items/${learnScala.id}")
      .body(ItemToBeUpdated(description = None, completed = Some(true)))
      .response(asJson[Item])
      .send(backendStub)

    // then
    response.map(response => assertEquals(response.body.map(_.completed), Right(true))).unwrap

  test("it should delete item"):
    // given
    val backendStub =
      TapirStubInterpreter(SttpBackendStub(new CatsMonadError[IO]())).whenServerEndpointRunLogic(itemsDeleteServerEndpoint).backend()

    // when
    val response = basicRequest.delete(uri"https://example.com/api/items/${learnScala.id}").send(backendStub)

    // then
    response.map(response => assertEquals(response.code, NoContent)).unwrap

  extension [T](t: IO[T]) def unwrap: T = t.unsafeRunSync()
