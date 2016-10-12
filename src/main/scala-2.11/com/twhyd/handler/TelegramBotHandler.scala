package com.twhyd.handler

import com.twhyd.BotConfig._
import com.twhyd.BotUtils
import com.twhyd.dao.{DB, Vehicle}
import org.telegram.telegrambots.TelegramApiException
import org.telegram.telegrambots.api.methods.send.SendMessage
import org.telegram.telegrambots.api.objects.Update
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.logging.BotLogger

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TelegramBotHandler extends TelegramLongPollingBot {

  val LOGTAG = "TelegramBotHandler"

  override def onUpdateReceived(update: Update): Unit = {
    if (update.hasMessage) {
      val message = update.getMessage
      //check if the message has text. it could also contain for example a location ( message.hasLocation() )
      if (message.hasText) {
        val input: String = message.getText
        BotLogger.info(LOGTAG, input)

        //create an object that contains the information to send back the message
        val sendMessageRequest = new SendMessage
        sendMessageRequest.setChatId(message.getChatId.toString) //who should get from the message the sender that sent it.

        val commandAndParams = BotUtils.getCommandAndParams(input)
        val result = commandAndParams match {
          case (Some(command), Some(params)) if params.nonEmpty => process(command, params)
          case (Some(ACTION_VEHICLE_OWNER), Some(params)) if params.isEmpty => MSG_INVALID_GET_VEHICLE_FORMAT
          case (Some(ACTION_ADD_VEHICLE_OWNER), Some(params)) if params.isEmpty => MSG_INVALID_ADD_VEHICLE_FORMAT
          case (_) => String.format(MSG_INVALID_FORMAT_GENERIC, input)
        }

        result match {
          case msg: String => sendResponse(sendMessageRequest, msg)
          case action: Future[String] => action.map(msg => sendResponse
          (sendMessageRequest, msg))
        }
      }
    }
  }

  def sendResponse(sendMessageRequest: SendMessage, msg: String): Any = {
    BotLogger.info(LOGTAG, s"response=>$msg")
    sendMessageRequest.setText(msg)
    try {
      sendMessage(sendMessageRequest)
    } catch {
      case e: TelegramApiException => {
        BotLogger.error(LOGTAG, e)
      }
    }
  }

  def process(command: String, input: String) = {
    if (ACTION_VEHICLE_OWNER.equalsIgnoreCase(command) || ACTION_VEHICLE_OWNER_DEPRECATED.equalsIgnoreCase(command)
    ) getVehicleOwner(input)
    else if (ACTION_ADD_VEHICLE_OWNER.equalsIgnoreCase(command) || ACTION_ADD_VEHICLE_OWNER_DEPRECATED
      .equalsIgnoreCase(command)) addVehicleOwner(input)
    else
      String.format(MSG_INVALID_FORMAT_GENERIC, command)
  }

  def standardizeRegistrationKey(regKey: String): String = regKey.replaceAll(" ", "").replaceAll("-", "").toUpperCase

  def addVehicleOwner(input: String): Future[String] = {
    val regKeyAndOwner = BotUtils.getRegKeyAndOwner(input)
    regKeyAndOwner match {
      case (Some(regKey), Some(owner)) if (regKey.nonEmpty && owner.nonEmpty) => DB.addVehicleOwner(Vehicle
      (standardizeRegistrationKey(regKey), owner))
      case _ => Future {
        MSG_INVALID_ADD_VEHICLE_FORMAT
      }
    }
  }

  def getVehicleOwner(inputs: String) = if (inputs.length > 1) DB.getVehicleOwner(standardizeRegistrationKey(inputs)) else
    "Please provide the registration number."

  def isValidAddFormat(inputs: List[String]) = {
    inputs.isEmpty || inputs.size < 2
  }

  override def getBotUsername: String = BOT_USERNAME

  override def getBotToken: String = BOT_TOKEN
}
