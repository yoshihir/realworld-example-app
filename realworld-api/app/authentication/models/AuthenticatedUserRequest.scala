package authentication.models

import authentication.api.OptionallyAuthenticatedUserRequest
import play.api.mvc.Request
import play.api.mvc.Security.AuthenticatedRequest

class AuthenticatedUserRequest[+A](authenticatedUser: AuthenticatedUser, request: Request[A])
  extends AuthenticatedRequest[A, AuthenticatedUser](authenticatedUser, request)
    with OptionallyAuthenticatedUserRequest[A] {

  override val authenticatedUserOption: Option[AuthenticatedUser] = Some(authenticatedUser)

}
