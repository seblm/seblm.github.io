package todo

import cats.effect.{ExitCode, IO, IOApp}
import sttp.tapir.server.netty.cats.NettyCatsServer

object Main extends IOApp:
  private val port = sys.env.get("HTTP_PORT").flatMap(_.toIntOption).getOrElse(8080)

  override def run(args: List[String]): IO[ExitCode] =
    NettyCatsServer
      .io()
      .use: server =>
        for
          bind <- server.port(port).host("localhost").addEndpoints(Endpoints.all).start()
          _ <- IO.println(s"Go to http://localhost:${bind.port}/docs to open SwaggerUI. Press ENTER key to exit.")
          _ <- IO.readLine
          _ <- bind.stop()
        yield bind
      .as(ExitCode.Success)
