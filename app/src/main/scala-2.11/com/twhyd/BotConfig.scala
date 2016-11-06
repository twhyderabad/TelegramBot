package com.twhyd

import com.typesafe.config.ConfigFactory

object BotConfig {
  private val KEY_BOT_NAME = "bot_name"

  val conf = ConfigFactory.load()

  val BOT_USERNAME = if (conf.hasPath(KEY_BOT_NAME)) conf.getString(KEY_BOT_NAME) else "TwHydBot"
  val BOT_TOKEN = conf.getString("api_token")

  //webhook url
  val BOT_PATH: String = ""

  val ACTION_VEHICLE_OWNER_DEPRECATED: String = "/vehicleOwner"
  val ACTION_VEHICLE_OWNER: String = "/vehicle_owner"
  val ACTION_ADD_VEHICLE_OWNER: String = "/add_vehicle_owner"
  val ACTION_ADD_VEHICLE_OWNER_DEPRECATED: String = "/addVehicleOwner"

  val MSG_INVALID_ADD_VEHICLE_FORMAT = s"Please provide the input in format '$ACTION_ADD_VEHICLE_OWNER <regKey> " +
    s"<owner name>'"
  val MSG_INVALID_GET_VEHICLE_FORMAT = s"Please provide the input in format '$ACTION_VEHICLE_OWNER <regKey>'"
  val MSG_INVALID_FORMAT_GENERIC = "You commanded '%s', right now we don't support it but we soon might. :) "
}
