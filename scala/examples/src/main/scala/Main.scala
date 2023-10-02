import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.UUID
import scala.util.Random

case class Author(name: String, email: String)
abstract class Database:
  def executeQuery(
      sql: String,
      parameters: (String, Any)*
  ): Boolean

def saveArticle(id: UUID, content: String, author: Author)(
    database: Database
): Boolean =
  database.executeQuery(
    """INSERT INTO Article(id, content, author_name, author_email)
      |VALUES (:id, :content, :author_name, :author_email);
      |""".stripMargin,
    "id" -> id,
    "content" -> content,
    "author_name" -> author.name,
    "author_email" -> author.email
  )

val postgresql: Database = new Database:
  override def executeQuery(
      sql: String,
      parameters: (String, Any)*
  ): Boolean = true

@main def main(): Unit =
  saveArticle(
    UUID.fromString("0d975fff-44dc-4110-b21a-1f31148969b8"),
    "<p>Great content</p>",
    Author("Sébastian", "seb@example.com")
  )(postgresql)

def sayHello(niceMessage: String): Unit =
  println(s"Hello $niceMessage")

def dice(): Int =
  Random.nextInt(6) + 1

def nowPlus(days: Long): Instant =
  Instant.now().plus(days, ChronoUnit.DAYS)
