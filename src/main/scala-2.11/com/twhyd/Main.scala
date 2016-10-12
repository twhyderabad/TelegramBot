package com.twhyd

import java.io.IOException
import java.util.logging.{ConsoleHandler, Level}

import com.twhyd.dao.DB
import com.twhyd.handler.TelegramBotHandler
import org.telegram.telegrambots.logging.{BotLogger, BotsFileHandler}
import org.telegram.telegrambots.{TelegramApiException, TelegramBotsApi}

class Main {

}

object Main {
  val LOGTAG = "MAIN"

  def main(args: Array[String]): Unit = {
    initializeLogging
    DB.setupDB("sqllite")
    startTelegramBot
  }

  private def initializeLogging = {
    BotLogger.setLevel(Level.ALL)
    BotLogger.registerLogger(new ConsoleHandler)
    try
      BotLogger.registerLogger(new BotsFileHandler)
    catch {
      case e: IOException => {
        BotLogger.severe(LOGTAG, e)
      }
    }
  }

  private def startTelegramBot = {
    val telegramBotsApi: TelegramBotsApi = new TelegramBotsApi
      val start = System.currentTimeMillis()
    try {
      telegramBotsApi.registerBot(new TelegramBotHandler)
    } catch {
      case e: TelegramApiException => {
        BotLogger.error(LOGTAG, e)
      }
    }
    val time = System.currentTimeMillis() - start
    BotLogger.info(LOGTAG, s"Bot initialized successfully in $time ms.")
  }
}