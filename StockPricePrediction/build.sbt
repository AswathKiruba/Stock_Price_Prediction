name := "StockPricePrediction"

version := "0.1"

scalaVersion := "2.11.8"

val scalaTestVersion = "3.0.1"


libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.4",
  "org.scala-lang.modules" %% "scala-xml" % "1.0.2",
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1",
  "org.twitter4j" % "twitter4j-stream" % "3.0.5",
  "oauth.signpost" % "signpost-core" % "1.2",
  "oauth.signpost" % "signpost-commonshttp4" % "1.2",
  "org.apache.httpcomponents" % "httpclient" % "4.5",
  "org.apache.httpcomponents" % "httpcore" % "4.4.6",
  "org.apache.commons" % "commons-io" % "1.3.2",
  "org.apache.spark" %% "spark-core" % "2.1.0",
  "org.apache.spark" % "spark-streaming_2.11" % "2.1.0",
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.1.0").map(_.excludeAll(
  ExclusionRule(organization = "org.scalacheck"),
  ExclusionRule(organization = "org.scalactic"),
  ExclusionRule(organization = "org.scalatest")
))

val sprayGroup = "io.spray"
val sprayJsonVersion = "1.3.2"
libraryDependencies ++= List("spray-json") map { c => sprayGroup %% c % sprayJsonVersion }

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0"

libraryDependencies += "edu.stanford.nlp" % "stanford-corenlp" % "3.6.0" classifier "models"

libraryDependencies += "org.scalatest" %% "scalatest" % scalaTestVersion % "test"
libraryDependencies += "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % "test"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "2.16.0"

// https://mvnrepository.com/artifact/com.cloudera.sparkts/sparkts
libraryDependencies += "com.cloudera.sparkts" % "sparkts" % "0.4.0"

libraryDependencies ++= Seq("org.apache.spark" %% "spark-mllib" % "2.3.0")

// https://mvnrepository.com/artifact/org.elasticsearch/elasticsearch-spark-20
libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-20" % "5.0.0-beta1"


libraryDependencies += "com.github.fsw0422" %% "yahoofinancehistoryfetcher" % "0.1.1"
