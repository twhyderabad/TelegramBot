name := "TwHydTelegramBot"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "jitpack.io" at "https://jitpack.io"

libraryDependencies ++= Seq(
  // TelegramBotApi
  "com.github.rubenlagus" %% "TelegramBots" % "v2.3.5",
  // config properties library
  "com.typesafe" % "config" % "1.3.1",
  // SQLlite
  "org.xerial" % "sqlite-jdbc" % "3.8.11.2",
  //ARM
  "com.jsuereth" %% "scala-arm" % "1.4",
  //Slick
  "com.typesafe.slick" %% "slick" % "3.1.1",
  // https://mvnrepository.com/artifact/com.zaxxer/HikariCP
  "com.typesafe.slick" %% "slick-hikaricp" % "3.1.0",
  "com.zaxxer" % "HikariCP" % "2.5.1"


  // Scalatest
  , "org.scalactic" %% "scalactic" % "3.0.0"
  , "org.scalatest" %% "scalatest" % "3.0.0" % "test"
  // Mock
  , "org.scalamock" %% "scalamock-scalatest-support" % "3.2.2" % "test"
)

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

mainClass in Compile := Some("com.twhyd.Main")
