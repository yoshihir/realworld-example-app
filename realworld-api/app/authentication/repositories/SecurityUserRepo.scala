package authentication.repositories

import java.time.Instant

import authentication.exceptions.MissingSecurityUserException
import commons.models.{Email, IdMetaModel, Property}
import commons.repositories._
import commons.repositories.mappings.JavaTimeDbMappings
import authentication.models.{PasswordHash, SecurityUser, SecurityUserId}
import commons.utils.DbioUtils.optionToDbio
import slick.dbio.DBIO
import slick.jdbc.H2Profile.api.{DBIO => _, MappedTo => _, Rep => _, TableQuery => _, _}
import slick.lifted.{ProvenShape, _}

import scala.concurrent.ExecutionContext

private[authentication] class SecurityUserRepo(implicit private val ex: ExecutionContext)
  extends BaseRepo[SecurityUserId, SecurityUser, SecurityUserTable] {

  def findByEmailOption(email: Email): DBIO[Option[SecurityUser]] = {
    require(email != null)

    query
      .filter(_.email === email)
      .result
      .headOption
  }

  def findByEmail(email: Email): DBIO[SecurityUser] = {
    require(email != null)

    findByEmailOption(email)
      .flatMap(optionToDbio(_, new MissingSecurityUserException(email.toString)))
  }


  override protected val mappingConstructor: Tag => SecurityUserTable = new SecurityUserTable(_)

  override protected val modelIdMapping: BaseColumnType[SecurityUserId] = SecurityUserId.securityUserIdDbMapping

  override protected val metaModel: IdMetaModel = SecurityUserMetaModel

  override protected val metaModelToColumnsMapping: Map[Property[_], SecurityUserTable => Rep[_]] = Map(
    SecurityUserMetaModel.id -> (table => table.id),
    SecurityUserMetaModel.email -> (table => table.email),
    SecurityUserMetaModel.password -> (table => table.password)
  )

}

protected class SecurityUserTable(tag: Tag) extends IdTable[SecurityUserId, SecurityUser](tag, "security_users")
  with JavaTimeDbMappings {

  def email: Rep[Email] = column("email")

  def password: Rep[PasswordHash] = column("password")

  def createdAt: Rep[Instant] = column("created_at")

  def updatedAt: Rep[Instant] = column("updated_at")

  def * : ProvenShape[SecurityUser] = (id, email, password, createdAt, updatedAt) <> (SecurityUser.tupled,
    SecurityUser.unapply)
}

private[authentication] object SecurityUserMetaModel extends IdMetaModel {
  override type ModelId = SecurityUserId

  val email: Property[Email] = Property("email")
  val password: Property[PasswordHash] = Property("password")
}


