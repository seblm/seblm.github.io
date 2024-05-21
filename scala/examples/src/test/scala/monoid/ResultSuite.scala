package monoid

import cats.kernel.Monoid
import monoid.Report.{fromResult, given}
import monoid.Result.{Error, Success}
import munit.FunSuite

class ResultSuite extends FunSuite:

  test("should aggregate successes and errors"):
    val success1: Success = Success(10)
    val error: Error = Error("Null reference encountered")
    val success2: Success = Success(42)
    val results: Seq[Result] = Vector(success1, error, success2)
    val reports: Seq[Report] = results.map(fromResult)

    val combinedReport = Monoid.combineAll(reports)

    assertEquals(
      combinedReport,
      Report(List(success1, success2), List(error))
    )
