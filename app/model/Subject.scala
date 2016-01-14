package model

import com.amazonaws.services.dynamodbv2.document.Item
import com.gu.contentatom.thrift.atom.viewpoints.ViewpointsAtom
import org.joda.time.DateTime
import play.api.Logger
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsValue, JsPath, Format, Json}

import scala.util.control.NonFatal


case class Subject(
  id: Long,
  name: String,
  link: Option[String],
  viewpoints: List[Viewpoint],
  revision: Long,
  lastModified: Option[ChangeRecord],
  created: Option[ChangeRecord],
  published: Option[ChangeRecord]
) {
  def toItem = Item.fromJSON(Json.toJson(this).toString())

  def toThrift: ViewpointsAtom = {
    ViewpointsAtom(
      name = name,
      link = link,
      viewpoints = viewpoints.map(_.toThrift)
    )
  }

  def state = {
    published match {
      case None => "Draft"
      case Some(ChangeRecord(dt, _)) if (dt == lastModified.map(_.date).getOrElse(DateTime.now)) => "Live"
      case _ => "Live with unlaunched changes"
    }
  }

  def denormalise: DenormalisedSubject = {
    DenormalisedSubject(
      id = id,
      name = name,
      link = link,
      viewpoints = viewpoints.map(_.denormalise),
      revision = revision,
      lastModified = lastModified,
      created = created,
      published = published,
      state = state
    )
  }

}

object Subject {
  implicit val subjectFormat: Format[Subject] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "viewpoints").format[List[Viewpoint]] and
      (JsPath \ "revision").format[Long] and
      (JsPath \ "lastModified").formatNullable[ChangeRecord] and
      (JsPath \ "created").formatNullable[ChangeRecord] and
      (JsPath \ "published").formatNullable[ChangeRecord]
    )(Subject.apply, unlift(Subject.unapply))

  def fromItem(item: Item) = try {
    Json.parse(item.toJSON).as[Subject]
  } catch {
    case NonFatal(e) => Logger.error(s"failed to load subject ${item.toJSON}", e); throw e

  }

  def fromJson(json: JsValue) = json.as[Subject]
}

case class DenormalisedSubject(
                    id: Long,
                    name: String,
                    link: Option[String],
                    viewpoints: List[DenormalisedViewpoint],
                    revision: Long,
                    lastModified: Option[ChangeRecord],
                    created: Option[ChangeRecord],
                    published: Option[ChangeRecord],
                    state: String
)

object DenormalisedSubject {
  implicit val denormalisedSubjectFormat: Format[DenormalisedSubject] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "viewpoints").format[List[DenormalisedViewpoint]] and
      (JsPath \ "revision").format[Long] and
      (JsPath \ "lastModified").formatNullable[ChangeRecord] and
      (JsPath \ "created").formatNullable[ChangeRecord] and
      (JsPath \ "published").formatNullable[ChangeRecord] and
      (JsPath \ "state").format[String]
    )(DenormalisedSubject.apply, unlift(DenormalisedSubject.unapply))
}
