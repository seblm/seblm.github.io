package monoid

import cats.Monoid
import monoid.Result.{Error, Success}

enum Result:
  case Success(count: Int)
  case Error(message: String)

case class Report(ok: List[Success], ko: List[Error])

object Report:
  def fromResult(result: Result): Report = result match
    case success: Success =>
      Report(ok = List(success), ko = List.empty)
    case error: Error => Report(ok = List.empty, ko = List(error))

  given Monoid[Report] with
    def combine(a: Report, b: Report): Report =
      Report(ok = a.ok ++ b.ok, ko = a.ko ++ b.ko)
    def empty: Report = Report(ok = List.empty, ko = List.empty)
