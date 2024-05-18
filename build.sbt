scalaVersion := "2.13.14"
scalacOptions += "-language:higherKinds"
addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.13.3" cross CrossVersion.full)

scalacOptions += "-Ydelambdafy:inline"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio" % "2.0.22",
  "dev.zio" %% "zio-prelude" % "1.0.0-RC26",
)
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-feature",
  "-unchecked"
)