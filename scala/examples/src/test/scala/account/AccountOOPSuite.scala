package account

import account.AccountOOPSuite.clock
import munit.FunSuite

import java.time.{Clock, Instant, ZoneId}

class AccountOOPSuite extends FunSuite:

  test("should use account"):
    val account = Account(clock)
    account.deposit(500)
    account.withdraw(100)
    assertEquals(
      account.printStatement(),
      """Date       Amount Balance
        |24.12.2015  +500     500
        |23.8.2016   -100     400""".stripMargin
    )

object AccountOOPSuite:

  private val clock = new Clock:
    private var first = true
    override def getZone: ZoneId = null
    override def withZone(zone: ZoneId): Clock = null
    override def millis: Long = 0
    override def instant: Instant =
      if first then
        first = false
        Instant.parse("2015-12-24T00:00:00Z")
      else Instant.parse("2016-08-23T00:00:00Z")
