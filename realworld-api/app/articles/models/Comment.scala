
package articles.models

import java.time.Instant

import commons.models.{BaseId, IdMetaModel, Property, WithId}
import users.models.UserId
import play.api.libs.json._
import slick.jdbc.H2Profile.api.{DBIO => _, MappedTo => _, Rep => _, TableQuery => _, _}

case class Comment(id: CommentId,
                   articleId: ArticleId,
                   authorId: UserId,
                   body: String,
                   createdAt: Instant,
                   updatedAt: Instant,
                  ) extends WithId[Long, CommentId]

case class CommentId(value: Long) extends AnyVal with BaseId[Long]

object CommentId {
  implicit val commentIdFormat: Format[CommentId] = new Format[CommentId] {
    override def reads(json: JsValue): JsResult[CommentId] =
      Reads.LongReads.reads(json).map(CommentId(_))

    override def writes(o: CommentId): JsNumber = Writes.LongWrites.writes(o.value)
  }

  implicit val commentIdDbMapping: BaseColumnType[CommentId] = MappedColumnType.base[CommentId, Long](
    vo => vo.value,
    id => CommentId(id)
  )
}

object CommentMetaModel extends IdMetaModel {

  val articleId: Property[ArticleId] = Property("articleId")
  val authorId: Property[UserId] = Property("authorId")
  val body: Property[String] = Property("body")
  val updatedAt: Property[Instant] = Property("updatedAt")
  val createdAt: Property[Instant] = Property("createdAt")

  override type ModelId = CommentId
}