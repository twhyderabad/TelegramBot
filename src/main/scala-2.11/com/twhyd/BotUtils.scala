package com.twhyd

/**
  * Created by raj on 9/28/16.
  */
object BotUtils {

  val regex_registration_number = "^[A-Za-z]{2}( |-)?[0-9]{2}( |-)?[A-Za-z]?( |-)?[A-Za-z]{2}( |-)?[0-9]{1,4}".r

  val regex_command = "^\\/\\w+".r

  //  val regex_command = "(^\\/\\w+) (.*)".r
  /*  val u = regex_registration_number.findFirstIn("MH14-FE-5151")
    val w = regex_registration_number.findFirstIn("MH14-FE-5151 Adam de Levine")
    val x = regex_registration_number.findFirstIn("MH14 FE5151 Adam de Levine")
    val y = regex_registration_number.findFirstIn("MH14FE5151 Adam de Levine")
    val z = regex_registration_number.findFirstIn("MH14-FE 5151 Adam de Levine")*/

  def getCommandAndParams(s: String) = {
    val output = regex_command.findFirstMatchIn(s) //map (m => (m.matched, m.after.toString.trim))
    val commandAndParams = output.map(m => m.matched) match {
      case Some(m) => (Some(m), output.map(m => m.after.toString.trim))
      case None => (None, None)
    }
    commandAndParams
  }

  def getRegKeyAndOwner(s: String) = {
    val output = regex_registration_number findFirstMatchIn s
    val regKeyAndOwner = output.map(m => m.matched) match {
      case Some(m) => (Some(m), output.map(m => m.after.toString.trim))
      case None => (None, None)
    }
    regKeyAndOwner
  }

  //  print(getRegKeyAndOwner("MH14 FE 5151 Adam de Levine"))
  //  print(getRegKeyAndOwner("/add_vehicle_owner MH14 FE 5151 Adam de Levine"))
}