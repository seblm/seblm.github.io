package account

import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class AccountFP(private val transactions: Seq[TransactionFP])

case class TransactionFP(date: LocalDate, amount: Int)

object AccountFP:
  def empty: AccountFP = AccountFP(Seq.empty)
  def deposit(account: AccountFP, date: LocalDate, amount: Int): AccountFP =
    AccountFP(account.transactions :+ TransactionFP(date, amount))
  def withdraw(account: AccountFP, date: LocalDate, amount: Int): AccountFP =
    AccountFP(account.transactions :+ TransactionFP(date, -amount))
  def printStatement(account: AccountFP): String =
    "Date       Amount Balance" + printStatement(account.transactions, 0)
  private def printStatement(transactions: Seq[TransactionFP], balance: Int): String = transactions match
    case Nil => ""
    case TransactionFP(date, amount) :: tail =>
      "\n" + printTransaction(date, amount, balance + amount) + printStatement(tail, balance + amount)
  private val dateFormatter = DateTimeFormatter.ofPattern("d.M.y")
  private def printTransaction(date: LocalDate, amount: Int, balance: Int): String =
    val formattedDate = date.format(dateFormatter)
    "%-10s %+5d %7d".format(formattedDate, amount, balance)
