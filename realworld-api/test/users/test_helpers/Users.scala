package users.test_helpers

import java.time.Instant

import users.models.{User, UserId}
import users.test_helpers.UserRegistrations._

object Users {
  val petycja: User = {
    User(UserId(-1), petycjaRegistration.username, petycjaRegistration.email, None, None, Instant.now(), Instant.now())
  }

  val kopernik: User = {
    User(UserId(-1), kopernikRegistration.username, kopernikRegistration.email, None, None, Instant.now(), Instant.now())
  }
}