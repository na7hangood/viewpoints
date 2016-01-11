addCommandAlias("dist", ";riffRaffArtifact")

import play.PlayImport.PlayKeys._

name := "viewpoints"

version := "1.0"

lazy val dependencies = Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.10.33",
  "com.amazonaws" % "amazon-kinesis-client" % "1.6.1",
  "com.gu" %% "pan-domain-auth-play_2-4-0" % "0.2.8",
  ws, // for panda
  "com.squareup.okhttp" % "okhttp" % "2.4.0",
  "org.apache.thrift" % "libthrift" % "0.8.0",
  "com.twitter" %% "scrooge-core" % "4.1.0",
  "com.google.guava" % "guava" % "18.0",
  "net.logstash.logback" % "logstash-logback-encoder" % "4.2",
  "com.gu" % "kinesis-logback-appender" % "1.0.5",
  "org.slf4j" % "slf4j-api" % "1.7.12",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.12"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala, RiffRaffArtifact, SbtWeb)
  .settings(Defaults.coreDefaultSettings: _*)
  .settings(
    playDefaultPort := 9258,
    packageName in Universal := normalizedName.value,
    riffRaffPackageType := (packageZipTarball in config("universal")).value,
    riffRaffPackageName := s"editorial-tools:${name.value}",
    riffRaffManifestProjectName := riffRaffPackageName.value,
    riffRaffBuildIdentifier := Option(System.getenv("CIRCLE_BUILD_NUM")).getOrElse("DEV"),
    riffRaffUploadArtifactBucket := Option("riffraff-artifact"),
    riffRaffUploadManifestBucket := Option("riffraff-builds"),
    riffRaffArtifactResources := Seq(
      riffRaffPackageType.value -> s"packages/${name.value}/${riffRaffPackageType.value.getName}",
      baseDirectory.value / "deploy.json" -> "deploy.json",
      baseDirectory.value / "cloudformation" / "viewpoints.json" ->
        "packages/cloudformation/viewpoints.json"
    ),
    doc in Compile <<= target.map(_ / "none"),
    scalaVersion := "2.11.7",
    scalaVersion in ThisBuild := "2.11.7",
    libraryDependencies ++= dependencies
  )
  .settings(ViewpointsBuild.settings: _*)
