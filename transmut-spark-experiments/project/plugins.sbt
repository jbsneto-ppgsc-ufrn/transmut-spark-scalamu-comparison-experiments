addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.10")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4") 
addSbtPlugin("com.github.sbt" % "sbt-jacoco" % "3.0.3")
addSbtPlugin("com.orrsella" % "sbt-stats" % "1.0.7")
addSbtPlugin("br.ufrn.dimap.forall" % "sbt-transmut" % "0.1-SNAPSHOT")
addSbtPlugin("io.stryker-mutator" % "sbt-stryker4s" % "0.7.2")

libraryDependencies ++= Seq(
  "org.clapper" %% "grizzled-slf4j" % "1.3.4",
  "org.scalameta" %% "scalameta" % "4.2.0",
  "org.scalameta" %% "semanticdb" % "4.1.0",
  "com.github.pureconfig" %% "pureconfig" % "0.12.2",
  "io.circe" %% "circe-core" % "0.12.3",
  "io.circe" %% "circe-generic" % "0.12.3",
  "io.circe" %% "circe-parser" % "0.12.3"
)