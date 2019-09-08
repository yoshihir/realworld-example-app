package authentication.pac4j

import authentication.api._
import authentication.models.{CredentialsWrapper, JwtToken, SecurityUserIdProfile}
import authentication.pac4j.controllers.{Pack4jAuthenticatedActionBuilder, Pack4jOptionallyAuthenticatedActionBuilder}
import authentication.pac4j.services.{JwtTokenGenerator, UsernameAndPasswordAuthenticator}
import authentication.repositories.SecurityUserRepo
import com.softwaremill.macwire.wire
import commons.CommonsComponents
import commons.config.WithExecutionContextComponents
import commons.services.ActionRunner
import org.pac4j.core.profile.CommonProfile
import org.pac4j.jwt.config.signature.SecretSignatureConfiguration
import org.pac4j.jwt.credentials.authenticator.{JwtAuthenticator => Pac4jJwtAuthenticator}
import org.pac4j.jwt.profile.JwtGenerator
import org.pac4j.play.store.{PlayCacheSessionStore, PlaySessionStore}
import play.api.Configuration
import play.api.cache.AsyncCacheApi
import play.api.mvc.PlayBodyParsers
import play.cache.DefaultAsyncCacheApi

private[authentication] trait Pac4jComponents extends WithExecutionContextComponents with CommonsComponents {

  def actionRunner: ActionRunner

  def securityUserRepo: SecurityUserRepo

  lazy val usernamePasswordAuthenticator: Authenticator[CredentialsWrapper] = wire[UsernameAndPasswordAuthenticator]

  def configuration: Configuration

  private lazy val signatureConfig = {
    val secret: String = configuration.get[String]("play.http.secret.key")
    new SecretSignatureConfiguration(secret)
  }

  lazy val jwtAuthenticator: Pac4jJwtAuthenticator = {
    new Pac4jJwtAuthenticator(signatureConfig)
  }

  def playBodyParsers: PlayBodyParsers

  def securityUserProvider: SecurityUserProvider

  lazy val authenticatedAction: AuthenticatedActionBuilder = wire[Pack4jAuthenticatedActionBuilder]
  lazy val optionallyAuthenticatedAction: OptionallyAuthenticatedActionBuilder =
    wire[Pack4jOptionallyAuthenticatedActionBuilder]

  def defaultCacheApi: AsyncCacheApi

  private lazy val _: PlaySessionStore = {
    val defaultAsyncCacheApi = new DefaultAsyncCacheApi(defaultCacheApi)
    val syncCacheApi: play.cache.SyncCacheApi = new play.cache.DefaultSyncCacheApi(defaultAsyncCacheApi)

    new PlayCacheSessionStore(syncCacheApi)
  }

  lazy val pack4jJwtAuthenticator: TokenGenerator[SecurityUserIdProfile, JwtToken] = {
    val _: JwtGenerator[CommonProfile] = new JwtGenerator(signatureConfig)
    wire[JwtTokenGenerator]
  }
}