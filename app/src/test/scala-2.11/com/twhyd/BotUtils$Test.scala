package com.twhyd

import org.scalatest.FlatSpec

class BotUtils$Test extends FlatSpec {
  val validCommandAndInput = "/add_vehicle_owner MH14 FE 5151 Adam de Levine"
  val validAddInput = "MH14 FE 5151 Adam de Levine"
  val invalidInputCommandOnly = "/add_vehicle_owner"
  val invalidAddInput = "/add_vehicle_owner MH14 FE 5151"

  it should "return command and params from the input" in {
    val gold = (Some("/add_vehicle_owner"), Some("MH14 FE 5151 Adam de Levine"))
    val lead = BotUtils.getCommandAndParams(validCommandAndInput)
    assert(gold === lead)
  }

  it should "not return command for invalid input" in {
    val output = BotUtils.getCommandAndParams(invalidInputCommandOnly)
    assert(output match {
      case (None, None) => true
      case (Some(_), None) => true
      case (Some(_), Some("")) => true
      case (_, _) => false
    })
  }

  it should "return regkey and owner for input" in {
    val gold = (Some("MH14 FE 5151"), Some("Adam de Levine"))
    val lead = BotUtils.getRegKeyAndOwner(validAddInput)
    assert(gold === lead)
  }

  it should "not return regkey and owner for invalid input" in {
    val outputInvalidInput = BotUtils.getRegKeyAndOwner(invalidAddInput)
    assert(outputInvalidInput match {
      case (None, None) => true
      case (Some(_), None) => true
      case (Some(_), Some("")) => true
      case (_, _) => false
    })

    val outputInvalidInputCommandOnly = BotUtils.getRegKeyAndOwner(invalidInputCommandOnly)
    assert(outputInvalidInputCommandOnly match {
      case (None, None) => true
      case (Some(_), None) => true
      case (Some(_), Some("")) => true
      case (_, _) => false
    })
  }
}
