package model

import com.amazonaws.services.dynamodbv2.document.Item
import com.gu.contentatom.thrift.atom.viewpoints.{Viewpoint => ThriftViewpoint}
import model.repositories.CommenterRepository
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, JsPath, Format, Json}

import scala.util.control.NonFatal

case class Viewpoint(
                       id: Long,
                       commenterId: Long,
                       quote: String,
                       link: Option[String],
                       date: Option[DateTime]
                       ) {

  def toItem = Item.fromJSON(Json.toJson(this).toString())

  def toThrift: ThriftViewpoint = {
    val commenter = CommenterRepository.getCommenter(commenterId) getOrElse { throw new RuntimeException(s"commenter $commenterId not found") }
    ThriftViewpoint(
      commenter = commenter.toThrift,
      quote = quote,
      link = link,
      date = date.map(_.getMillis)
    )
  }

  def denormalise: DenormalisedViewpoint = {
    DenormalisedViewpoint(
      id = id,
      commenter = CommenterRepository.getCommenter(commenterId).get,
      quote = quote,
      link = link,
      date = date
    )
  }
}

object Viewpoint {
  implicit val viewpointFormat: Format[Viewpoint] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "commenterId").format[Long] and
      (JsPath \ "quote").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "date").formatNullable[DateTime]
    )(Viewpoint.apply, unlift(Viewpoint.unapply))

  def fromItem(item: Item) = try{
    Json.parse(item.toJSON).as[Viewpoint]
  } catch {
    case NonFatal(e) => Logger.error(s"failed to load viewpoint ${item.toJSON}", e); throw e

  }

  def fromJson(json: JsValue) = json.as[Viewpoint]
}

case class DenormalisedViewpoint(
  id: Long,
  commenter: Commenter,
  quote: String,
  link: Option[String],
  date: Option[DateTime]
)

object DenormalisedViewpoint {
  implicit val denormalisedViewpointFormat: Format[DenormalisedViewpoint] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "commenter").format[Commenter] and
      (JsPath \ "quote").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "date").formatNullable[DateTime]
    )(DenormalisedViewpoint.apply, unlift(DenormalisedViewpoint.unapply))
}
