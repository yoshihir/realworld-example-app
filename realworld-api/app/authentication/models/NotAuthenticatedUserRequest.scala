package authentication.models

import authentication.api.OptionallyAuthenticatedUserRequest
import play.api.mvc.{Request, WrappedRequest}

class NotAuthenticatedUserRequest[+A](request: Request[A])
  extends WrappedRequest[A](request) with OptionallyAuthenticatedUserRequest[A] {

  override def authenticatedUserOption: Option[AuthenticatedUser] = None

}