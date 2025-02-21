val tapirVersion = "1.11.10"
val scala3Version = "3.6.2"

lazy val root = project
  .in(file("."))
  .settings(
    name := "examples",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-cats-effect" % tapirVersion,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % tapirVersion,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-netty-server-cats" % tapirVersion,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % tapirVersion,
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.12.0",

    libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.5.12" % Runtime,

    libraryDependencies += "com.softwaremill.sttp.client3" %% "circe" % "3.10.1" % Test,
    libraryDependencies += "com.softwaremill.sttp.tapir" %% "tapir-sttp-stub-server" % tapirVersion % Test,
    libraryDependencies += "org.scalameta" %% "munit" % "1.0.3" % Test
  )
