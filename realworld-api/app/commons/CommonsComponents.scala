package commons

import com.softwaremill.macwire.wire
import commons.repositories.{DateTimeProvider, DbConfigHelper}
import commons.services.{ActionRunner, InstantProvider}
import play.api.db.slick.DatabaseConfigProvider

trait CommonsComponents {
  lazy val actionRunner: ActionRunner = wire[ActionRunner]
  lazy val dbConfigHelper: DbConfigHelper = wire[DbConfigHelper]

  def databaseConfigProvider: DatabaseConfigProvider

  lazy val dateTimeProvider: DateTimeProvider = wire[InstantProvider]
}
