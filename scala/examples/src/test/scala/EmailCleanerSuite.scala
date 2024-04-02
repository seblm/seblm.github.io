import EmailCleaner.clean
import munit.FunSuite

class EmailCleanerSuite extends FunSuite:

  test("remove starting quote"):
    assertEquals(clean(""""kai@example.com"""),
                         "kai@example.com")
  test("remove trailing quote"):
    assertEquals(clean("""sacha@example.net""""),
                         "sacha@example.net")
  test("remove starting and trailing quote"):
    assertEquals(clean(""""cruz@example.org""""),
                         "cruz@example.org")
  test("remove starting and trailing spaces"):
    assertEquals(clean("""  noam@example.com    """),
                         "noam@example.com")
