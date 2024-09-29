object EmailCleaner:
  private val startWithQuote = """^"(.+)""".r
  private val endsWithQuote = """(.+)"$""".r
  private val startAndEndWithQuote = """^"(.+)"$""".r
  private val startAndEndWithSpaces = """^\s+(.*?)\s+$""".r

  def clean(notCleanedEmail: String): String = notCleanedEmail match
    case startAndEndWithQuote(email)  => email
    case startWithQuote(email)        => email
    case endsWithQuote(email)         => email
    case startAndEndWithSpaces(email) => email
    case _                            => notCleanedEmail
