name := "TwHydTelegramBot"

addCommandAlias("mg", "migrations/run")

lazy val commonSettings = Seq(
  version := "1.0",
  scalaVersion := "2.11.8",
  resolvers += "jitpack.io" at "https://jitpack.io" // For TelegramBot Api jar.
)

lazy val slickVersion = "3.1.1"
lazy val forkliftVersion = "0.2.3"


lazy val commonDependencies = Seq(
  // HikariCP - used by Slick
    "com.typesafe.slick" %% "slick-hikaricp" % slickVersion
  , "com.zaxxer" % "HikariCP" % "2.5.1"

  // SQLlite
  , "org.xerial" % "sqlite-jdbc" % "3.8.11.2"
)


lazy val appDependencies = Seq(
  // TelegramBotApi
    "com.github.rubenlagus" %% "TelegramBots" % "v2.3.5"
  // config properties library
  , "com.typesafe" % "config" % "1.3.1"

  //Slick
  , "com.typesafe.slick" %% "slick" % slickVersion

  // Scalatest
  , "org.scalactic" %% "scalactic" % "3.0.0"
  , "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  // Mock
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test"
)

lazy val migrationDependencies = Seq(
  "com.liyaos" %% "scala-forklift-slick" % forkliftVersion
  , "io.github.nafg" %% "slick-migration-api" % "0.3.0"
)

lazy val app = project.in(file("app")).dependsOn(generatedCode).settings(commonSettings: _*)
  .settings {
    libraryDependencies ++= commonDependencies ++ appDependencies
  }

lazy val migrations = project.in(file("migrations")).settings(
  commonSettings: _*).settings {
  libraryDependencies ++=commonDependencies ++ migrationDependencies
}

lazy val generatedCode = Project("generate_code",
  file("generated_code")).settings(commonSettings: _*).settings {
  libraryDependencies ++= appDependencies
}

enablePlugins(sbtdocker.DockerPlugin, JavaAppPackaging)

dockerfile in docker := {
  val appDir: File = stage.value
  val targetDir = "/app"

  new Dockerfile {
    from("java")
    entryPoint(s"$targetDir/bin/${executableScriptName.value}")
    copy(appDir, targetDir)
  }
}