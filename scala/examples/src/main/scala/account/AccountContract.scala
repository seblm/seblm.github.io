package account

trait AccountContract:
  def deposit(amount: Int): Unit
  def withdraw(amount: Int): Unit
  def printStatement(): String
