val scala3Version = "3.4.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "examples",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "org.typelevel" %% "cats-core" % "2.10.0",

    libraryDependencies += "org.scalameta" %% "munit" % "0.7.29" % Test
  )
