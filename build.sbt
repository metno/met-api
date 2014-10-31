name := """bora-prototype"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.2"

instrumentSettings

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  ws
)
