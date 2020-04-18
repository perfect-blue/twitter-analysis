
name := "twitter-stream-analysis"

version := "0.1"

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "2.4.3",
  "org.apache.spark" %% "spark-streaming" % "2.4.3",
  "org.twitter4j" % "twitter4j-core" % "4.0.6",
  "org.twitter4j" % "twitter4j-stream" % "4.0.6",
  "org.apache.bahir" %% "spark-streaming-twitter" % "2.3.4",
  "org.apache.spark" %% "spark-tags" % "2.4.3",
  "org.apache.spark" %% "spark-sql" % "2.4.3"
)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}