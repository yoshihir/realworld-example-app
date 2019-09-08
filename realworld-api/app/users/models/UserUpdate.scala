package users.models

import authentication.models.PlainTextPassword
import commons.models.{Email, Username}
import play.api.libs.json.{Format, Json}

case class UserUpdate(email: Option[Email], username: Option[Username], bio: Option[String], image: Option[String],
                      password: Option[PlainTextPassword])

object UserUpdate {

  implicit val updateUserFormat: Format[UserUpdate] = Json.format[UserUpdate]

}