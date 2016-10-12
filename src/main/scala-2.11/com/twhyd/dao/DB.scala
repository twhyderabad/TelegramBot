package com.twhyd.dao

import java.sql.SQLException

import org.telegram.telegrambots.logging.BotLogger
import slick.driver.SQLiteDriver.api._
import slick.jdbc.meta.MTable

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

object DB {
  val vehicles = TableQuery[Vehicles]
  var LOGTAG = "DB"
  var db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef = _

  def addVehicleOwner(vehicle: Vehicle): Future[String] = {
    val addFuture = db.run((vehicles returning vehicles.map(_.id)) += vehicle)
    addFuture map (id => {
      BotLogger.info(LOGTAG, s"Vehicle registered at $id ")
      val status = String.format("Registration number - '%s' mapped to owner '%s' successfully", vehicle.registration,
        vehicle.owner)
      status
    }) recover {
      case e: SQLException if e.getMessage.contains("SQLITE_CONSTRAINT") => {
        updateVehicleOwner(vehicle)
      }
      case e => {
        BotLogger.error(LOGTAG, e)
        val status = "Sorry, unable to add now!"
        status
      }
    }
  }

  def updateVehicleOwner(vehicle: Vehicle) = {
    //    val updateAction = vehicles.filter(_.registration === vehicleToBeUpdated.registration).map(v => v.owner).update(vehicleToBeUpdated.owner)
    val updateAction = (for {v <- vehicles if v.registration === vehicle.registration} yield v.owner).update(vehicle.owner)

    val updateFuture = db.run(updateAction)
    val resultFuture = updateFuture map { _ => s"Updated owner='${vehicle.owner}' for '${vehicle.registration}'"} recover {
      case t => {
        BotLogger.error(LOGTAG, t)
        "Unable to update now."
      }
    }
    exec(resultFuture)
  }

  // Helper method for running a query in this example file:
  def exec[T](sqlFuture: Future[T]): T = Await.result(sqlFuture, 1 seconds)

  def dropDB = {
    Await.result(db.run(sqlu"""drop table if exists VEHICLES;"""), Duration.Inf)
    BotLogger.info(LOGTAG, "Table dropped")
  }

  def setupDB(profile: String): Unit = {
    db = Database.forConfig(profile)
    val setup = DBIO.seq(
      vehicles.schema.create
    )
    def createTableIfNotInTables(tables: Vector[MTable]): Future[Unit] = {
      if (!tables.exists(_.name.name == vehicles.baseTableRow.tableName)) {
        val setupFuture = db.run(setup)
        setupFuture onComplete {
          case Success(o) => BotLogger.info(LOGTAG, "Opened database successfully")
          case Failure(t) => BotLogger.error(LOGTAG, t.getMessage)
        }
        setupFuture
      } else {
        BotLogger.info(LOGTAG, "Table 'Vehicles' already exists.")
        Future()
      }
    }

    val createTableIfNotExist: Future[Unit] = db.run(MTable.getTables).flatMap(createTableIfNotInTables)

    Await.result(createTableIfNotExist, Duration.Inf)
  }

  def getVehicleOwner(registrationKey: String): Future[String] = {
    val getAction = (for {v <- vehicles if v.registration === registrationKey} yield v) result
    val getFuture = db.run(getAction)
    getFuture map { rs => s"Owner=>${rs.head.owner}" } recover { case _ => "Owner not registered." }
  }

  /* Not working on SQLite */
  def addOrUpdateVehicleOwner(vehicle: Vehicle) = {
    val x = db.run(vehicles.insertOrUpdate(vehicle))
    Await.result(x, 2 seconds)
  }
}
