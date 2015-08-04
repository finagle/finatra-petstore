scalaVersion := "2.11.7"
crossScalaVersions := Seq("2.10.5", "2.11.7")

lazy val finatraVersion = "2.0.0.M2"
lazy val finagleVersion = "6.25.0"

resolvers += "twitter-repo" at "https://maven.twttr.com"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-language:existentials",
  "-language:higherKinds",
  "-unchecked",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Xfuture"
)

libraryDependencies ++= Seq(
  "com.twitter.finatra" %% "finatra-http" % finatraVersion,
  "com.twitter.finatra" %% "finatra-http" % finatraVersion % "test" classifier "tests",
  "com.twitter.finatra" %% "finatra-logback" % finatraVersion,
  "com.twitter.inject" %% "inject-app" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-app" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-core" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-modules" % finatraVersion % "test" classifier "tests",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test",
  "com.twitter.inject" %% "inject-server" % finatraVersion % "test" classifier "tests",
  "com.twitter" %% "finagle-stats" % finagleVersion,
  "junit" % "junit" % "4.10" % "test",
  "org.clapper" %% "grizzled-slf4j" % "1.0.2",
  "org.mockito" % "mockito-core" % "1.9.5" % "test",
  "org.scalatest" %% "scalatest" % "2.2.3" % "test",
  "org.scalacheck" %% "scalacheck" % "1.12.4" % "test",
  "org.slf4j" % "slf4j-simple" % "1.7.7"
)
