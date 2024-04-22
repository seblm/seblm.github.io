package account

import munit.FunSuite

import java.time.LocalDate

class AccountFPSuite extends FunSuite:

  test("should use account"):
    val account = AccountFP.withdraw(
      AccountFP.deposit(AccountFP.empty, LocalDate.parse("2015-12-24"), 500),
      LocalDate.parse("2016-08-23"),
      100
    )
    assertEquals(
      AccountFP.printStatement(account),
      """Date       Amount Balance
        |24.12.2015  +500     500
        |23.8.2016   -100     400""".stripMargin
    )
