package model

import com.amazonaws.services.dynamodbv2.document.Item
import com.gu.contentatom.thrift.atom.viewpoints.ViewpointsAtom
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, JsPath, Format, Json}

import scala.util.control.NonFatal


case class Subject(
  name: String,
  link: Option[String],
  viewpoints: List[Viewpoint],
  revision: Long
) {
  def toItem = Item.fromJSON(Json.toJson(this).toString())

  def toThrift: ViewpointsAtom = {
    ViewpointsAtom(
      name = name,
      link = link,
      viewpoints = viewpoints.map(_.toThrift)
    )
  }

}

object Subject {
  implicit val subjectFormat: Format[Subject] = (
    (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "viewpoints").format[List[Viewpoint]] and
      (JsPath \ "revision").format[Long]
    )(Subject.apply, unlift(Subject.unapply))

  def fromItem(item: Item) = try {
    Json.parse(item.toJSON).as[Subject]
  } catch {
    case NonFatal(e) => Logger.error(s"failed to load subject ${item.toJSON}", e); throw e

  }

  def fromJson(json: JsValue) = json.as[Subject]
}
