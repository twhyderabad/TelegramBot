# Telegram Bot for Hyd TW group.
    * Keeps a record of vehicles and owners
        * Supported methods 
            ```
            /vehicle_owner - Get the vehicle owner  
            /add_vehicle_owner - Add the vehicle owner 
            ```
            


_____________
## Setting up project 

* Install sbt and docker.
* cd into project directory and run `sbt`. This will install all the dependencies locally.
* Import the project in IntelliJ idea as a 'sbt' project.
* Provide the bot token key in `api_token = "<apiKey>"` in `application.conf`
* Make the changes and then build the docker image by running `sbt docker`.
* Run with `docker run -v /data:/data twhydtelegrambot/twhydtelegrambot`


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