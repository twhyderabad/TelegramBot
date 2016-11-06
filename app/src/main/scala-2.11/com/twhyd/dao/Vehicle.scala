package com.twhyd.dao

import slick.driver.SQLiteDriver.api._
import slick.lifted.{Rep, Tag}

/**
  * Created by raj on 10/3/16.
  */
case class Vehicle(registration: String, owner: String, id: Option[Int] = None)

class Vehicles(tag: Tag) extends Table[Vehicle](tag, "VEHICLES") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def registration: Rep[String] = column[String]("registration")

  def owner: Rep[String] = column[String]("owner")

  def * = (registration, owner, id.?) <> (Vehicle.tupled, Vehicle.unapply)

  def idx = index("idx_registration", registration, unique = true)
}
