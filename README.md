

## Setting up project 

* Install sbt and cd into project directory and run `sbt`. This will install all the dependencies locally.
* Import the project in IntelliJ idea as a 'sbt' project.
* Make the changes and then build the docker image by running `sbt docker`.
* Run with `docker run <nameOfImage>`


* Add `Supersafe` scala compiler plugin to highlight errors in ScalaTest at compile time by adding the following to `~/.sbt/0.13/global.sbt`:
```
resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
```

* Then adding the following line to project/plugins.sbt:
```
addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.0")
```




#### Concepts used/learnt/revisioned
 * ScalaTest
 * Configuration
 * Slick as FRM (Functional Relational Mapping for DB).
 * Regex and case matching
 * Futures
 * Mocking