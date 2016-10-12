package com.twhyd.dao

import org.scalatest.{AsyncFunSuite, BeforeAndAfterAll}
import slick.driver.SQLiteDriver.api._
import slick.jdbc.meta.MTable
import slick.lifted.TableQuery

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

/**
  * Created by raj on 10/9/16.
  */
class DBIntTest extends AsyncFunSuite with BeforeAndAfterAll {
  var db: _root_.slick.driver.SQLiteDriver.backend.DatabaseDef = _
  var vehicles: TableQuery[Vehicles] = _

  override def beforeAll() {
    val profile: String = "sqllite-test"
    vehicles = TableQuery[Vehicles]
    db = Database.forConfig(profile)
    DB.setupDB(profile)
  }

  override def afterAll() {
    DB.dropDB
  }

  test("vehicles table should exist") {
    val tables: Vector[MTable] = Await.result(db.run(MTable.getTables), 1 seconds)
    val exists = tables.exists(_.name.name == vehicles.baseTableRow
      .tableName)
    assert(exists)
  }

  def exec[T](future: Future[T]): T = Await.result(future, 2 seconds)

  test("should be able to add vehicle") {
    val vehicle = Vehicle("MH14FE5151", "John Doe")
    val addFuture = DB.addVehicleOwner(vehicle)
    val gold = s"Registration number - '${vehicle.registration}' mapped to owner '${vehicle.owner}' successfully"
    addFuture map { result => assert(result === gold) }
  }

  test("should be able to update owner") {
    val vehicle = Vehicle("MH14FE5151", "John Doe two")
    val updateFuture = DB.addVehicleOwner(vehicle)
    val gold = "Updated owner='John Doe two' for 'MH14FE5151'"
    updateFuture map { result => assert(result === gold) }
  }

  test("should be able to get owner for registered user") {
    val regKey = "MH14FE3333"
    val vehicle = Vehicle("MH14FE3333", "John Doe Three")
    val addFuture = DB.addVehicleOwner(vehicle)
    exec(addFuture)
    DB.getVehicleOwner(regKey) map {owner => assert(owner === vehicle.owner) }
  }

  test("should return proper msg for unregistered user") {
    val regKey = "MH14FE9999"
    DB.getVehicleOwner(regKey) map {owner => assert(owner === "Owner not registered.") }
  }
}
