package model

import com.amazonaws.services.dynamodbv2.document.Item
import com.gu.contentatom.thrift.atom.viewpoints.{Commenter => ThriftCommenter}
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, JsPath, Format, Json}

import scala.util.control.NonFatal


case class Commenter(
  id: Long,
  name: String,
  imageUrl: Option[String],
  description: Option[String],
  party: Option[String],
  category: Option[String]
) {

  def toItem = Item.fromJSON(Json.toJson(this).toString())

  def toThrift: ThriftCommenter = {
    ThriftCommenter(
      name = name,
      imageUrl = imageUrl,
      description = description,
      party = party
    )
  }

}

object Commenter {
  implicit val commenterFormat: Format[Commenter] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "imageUrl").formatNullable[String] and
      (JsPath \ "description").formatNullable[String] and
      (JsPath \ "party").formatNullable[String] and
      (JsPath \ "category").formatNullable[String]
    )(Commenter.apply, unlift(Commenter.unapply))

  def fromItem(item: Item) = try{
    Json.parse(item.toJSON).as[Commenter]
  } catch {
    case NonFatal(e) => Logger.error(s"failed to load section ${item.toJSON}", e); throw e

  }

  def fromJson(json: JsValue) = json.as[Commenter]
}
