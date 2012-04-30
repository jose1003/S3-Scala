scalaVersion := "2.9.1"

resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"

libraryDependencies += "com.typesafe.akka" % "akka-actor" % "2.0.1"

libraryDependencies += "net.java.dev.jets3t" % "jets3t" % "0.9.0"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.3.0"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.3.0"

libraryDependencies += "org.clapper" %% "grizzled-slf4j" % "0.6.8"

resolvers += "twitter-repo" at "http://maven.twttr.com/"

resolvers += "jets3t" at "http://www.jets3t.org/maven2"


otherResolvers := Seq(Resolver.file("dotM2",file(Path.userHome + "/.m2/repository")), Resolver.file("dotIVY",file(Path.userHome + "/.ivy2/repository")))
