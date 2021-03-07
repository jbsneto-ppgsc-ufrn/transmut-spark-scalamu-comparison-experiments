import ScalamuPlugin.autoImport.ScalamuKeys._

val sparkVersion = "2.4.3"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.apache.spark" %% "spark-core" % sparkVersion ,
  "com.holdenkarau" %% "spark-testing-base" % "2.4.3_0.12.0" % "test"
)

fork in Test := true

resolvers += Resolver.bintrayRepo("sugakandrey", "sbt-plugins")

lazy val root = (project in file("."))
  .enablePlugins(ScalamuPlugin)
  .settings(
    name := "scalamu-experiments",
    version := "0.1",
    scalaVersion := "2.12.3",
    timeoutFactor := 4,
    timeoutConst  := 6000
)