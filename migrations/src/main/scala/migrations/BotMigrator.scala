package migrations

import com.liyaos.forklift.slick._

object BotMigrator extends App
  with SlickMigrationCommandLineTool
  with SlickMigrationCommands
  with SlickMigrationManager
  with SlickCodegen {
  MigrationSummary
  execCommands(args.toList)
}

object MigrationSummary {}