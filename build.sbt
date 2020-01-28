ThisBuild / scalaVersion := "2.13.1"
ThisBuild / turbo := true

lazy val core = (project in file("core")).settings(
  organization := "tf.bug",
  name := "cubicmetre",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "squants" % "1.6.0",
    "org.typelevel" %% "spire" % "0.17.0-M1",
  ),
)

lazy val nbt = (project in file("nbt")).settings(
  organization := "tf.bug",
  name := "cubicmetre-nbt",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.1.0",
    "org.scodec" %% "scodec-core" % "1.11.4",
    "org.scodec" %% "scodec-cats" % "1.0.0",
  ),
)

lazy val event = (project in file("event")).settings(
  organization := "tf.bug",
  name := "cubicmetre-event",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "org.typelevel" %% "cats-core" % "2.1.0",
    "org.typelevel" %% "cats-effect" % "2.0.0",
    "co.fs2" %% "fs2-core" % "2.2.1",
  ),
).dependsOn(core)

lazy val region = (project in file("region")).settings(
  organization := "tf.bug",
  name := "cubicmetre-region",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "com.github.fd4s" %% "fs2-kafka" % "1.0.0",
  ),
).dependsOn(event)

lazy val node = (project in file("node")).settings(
  organization := "tf.bug",
  name := "cubicmetre-node",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "com.github.fd4s" %% "fs2-kafka" % "1.0.0",
    "co.fs2" %% "fs2-io" % "2.2.1",
  ),
).dependsOn(event)

lazy val protocol = (project in file("protocol")).settings(
  organization := "tf.bug",
  name := "cubicmetre-protocol",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.13.0-RC1",
    "io.circe" %% "circe-generic" % "0.13.0-RC1",
    "org.scodec" %% "scodec-stream" % "2.0.0",
  ),
).dependsOn(event, nbt)

lazy val bungee = (project in file("bungee")).settings(
  organization := "tf.bug",
  name := "cubicmetre-bungee",
  version := "0.1.0",
  scalaVersion := "2.13.1",
  resolvers += Resolver.bintrayRepo("alexknvl", "maven"),
  libraryDependencies ++= Seq(
    "io.circe" %% "circe-core" % "0.13.0-RC1",
    "co.fs2" %% "fs2-io" % "2.2.1",
    "com.alexknvl" %% "polymorphic" % "0.5.0",
  ),
  addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full),
).dependsOn(protocol)
