package account

import account.Account.Transaction

import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter
import java.time.{Clock, LocalDate}
import scala.collection.mutable

class Account(private val clock: Clock) extends AccountContract:

  private val transactions = mutable.ListBuffer[Transaction]()
  private var balance = 0

  override def deposit(amount: Int): Unit =
    balance += amount
    val now = clock.instant().atZone(UTC).toLocalDate
    transactions.append(Transaction(now, amount, balance))

  override def withdraw(amount: Int): Unit =
    balance -= amount
    val now = clock.instant().atZone(UTC).toLocalDate
    transactions.append(Transaction(now, -amount, balance))

  override def printStatement(): String = transactions
    .map(_.toString)
    .mkString("Date       Amount Balance\n", "\n", "")

object Account:

  private val dateFormatter = DateTimeFormatter.ofPattern("d.M.y")

  case class Transaction(
      date: LocalDate,
      amount: Int,
      balance: Int
  ):

    override def toString: String =
      val formattedDate = date.format(dateFormatter)
      "%-10s %+5d %7d".format(formattedDate, amount, balance)
