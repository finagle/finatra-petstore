scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.10.5", "2.11.7")

lazy val finatraVersion = "2.0.0.M2"

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % finatraVersion,
  "com.twitter.finatra" %% "finatra-http" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-logback" % finatraVersion,
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test" classifier "tests",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test"
)

resolvers += "twitter-repo" at "https://maven.twttr.com"