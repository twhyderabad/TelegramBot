package com.twhyd.dao

import slick.driver.SQLiteDriver.api._
import slick.lifted.{Rep, Tag}

case class Contact(name: String, number: String, email: Option[String] = None, id: Option[Int] = None)

class contacts(tag: Tag) extends Table[Contact](tag, "CONTACTS") {
  def id: Rep[Int] = column[Int]("id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def number: Rep[String] = column[String]("number")

  def email: Rep[String] = column[String]("email")

  def * = (name, number, email.?, id.?) <> (Contact.tupled, Contact.unapply)

  def idx = index("idx_name", name, unique = true)

}
