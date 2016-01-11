package model

import com.amazonaws.services.dynamodbv2.document.Item
import com.gu.contentatom.thrift.atom.viewpoints.{Viewpoint => ThriftViewpoint}
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, JsPath, Format, Json}

import scala.util.control.NonFatal

case class Viewpoint(
  commenter: Commenter,
  quote: String,
  link: Option[String],
  date: Option[DateTime]
) {

  def toItem = Item.fromJSON(Json.toJson(this).toString())

  def toThrift: ThriftViewpoint = {
    ThriftViewpoint(
      commenter = commenter.toThrift,
      quote = quote,
      link = link,
      date = date.map(_.getMillis)
    )
  }

}

object Viewpoint {
  implicit val commenterFormat: Format[Viewpoint] = (
    (JsPath \ "commenter").format[Commenter] and
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
