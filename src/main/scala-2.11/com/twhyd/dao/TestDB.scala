package com.twhyd.dao


import slick.driver.SQLiteDriver.api._
import slick.lifted.TableQuery

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

/**
  * Created by raj on 10/8/16.
  */
object TestDB {



  def main(args: Array[String]): Unit = {
    val db = Database.forConfig("sqllite")
    val vehicles = TableQuery[Vehicles]
    try {
      setupDB(db, vehicles)

      addVehicle(db, vehicles)

      updateVehicle(db, vehicles)

      getVehicle(db, vehicles)
      for (i <- 1 to 3) {
        println(s"finishing $i")
        Thread.sleep(1000)
      }
      println("End of line")
    } finally db.close
  }

  def getVehicle(db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef, vehicles: TableQuery[Vehicles]) = {
    val getAction = (for {v <- vehicles if v.registration === "Raj's car"} yield v)result
//    val getAction = vehicles.filter(_.registration === "Raj's car").result.head
    val getFuture = db.run(getAction)
    getFuture onComplete {
      case Success(o) => println("fetched successfully " + o)
      case Failure(t) => printFailure(t)
    }
  }

  def updateVehicle(db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef, vehicles: TableQuery[Vehicles]): Unit = {
    val vehicleToBeUpdated = Vehicle("Raj'sas car", "Raj night")
    //    val updateAction = vehicles.filter(_.registration === vehicleToBeUpdated.registration).map(v => v.owner).update(vehicleToBeUpdated.owner)
    val updateAction = (for {v <- vehicles if v.registration === vehicleToBeUpdated.registration} yield v.owner).update(vehicleToBeUpdated.owner)

    val updateFuture = db.run(updateAction)
    updateFuture onComplete {
      //Doesn't go here for some reason.
      case Success(o) => println("Updated successfully " + o)
      case Failure(t) => printFailure(t)
    }
  }

  def addVehicle(db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef, vehicles: TableQuery[Vehicles]): Unit = {
    val addFuture = db.run((vehicles returning vehicles.map(_.id)) += Vehicle("Raj'sas car", "Raj"))
    addFuture onComplete {
      case Success(id) => println(s"Vehicle added successfully at => " + id)
      case Failure(t) => printFailure(t)
    }
  }

  def setupDB(db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef, vehicles: TableQuery[Vehicles]): Unit = {
    val setup = DBIO.seq(
      vehicles.schema.create
      //        , vehicles += Vehicle("Raj's bike", "Raj"),
      //        vehicles += Vehicle("Mom's car  ", "Mom"),
      //        vehicles += Vehicle("Meggie's car ", "Meggie")
    )
    val setupFuture = db.run(setup)

    setupFuture onComplete {
      case Success(o) => println("Setup done successfully=>" + o)
      case Failure(t) => printFailure(t)
    }
  }

  def printFailure(t: Throwable): Unit = {
    println("Oh crap!" + t)
  }
}
