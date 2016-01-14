package model.command

import com.gu.pandomainauth.model.User
import model.{Viewpoint, DenormalisedSubject}
import model.repositories.{Sequences, SubjectRepository}
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}
import services.AtomPublisher


case class UpdateViewpointCommand (
  subjectId: Long,
  viewpointId: Long,
  commenterId: Long,
  quote: String,
  link: Option[String],
  date: Option[DateTime]
  ) extends Command {

  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {
    val originalSubject = SubjectRepository.getSubject(subjectId).get

    val modifiedSubject = originalSubject.copy(
      viewpoints = originalSubject.viewpoints.map { vp =>
        if (vp.id == viewpointId) {
          vp.copy(
            commenterId = commenterId,
            quote = quote,
            link = link,
            date = date
          )
        } else {
          vp
        }
      },
      revision = originalSubject.revision + 1,
      lastModified = Some(changeRecord(user))
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    res foreach{ s => AtomPublisher.publishDraft(s) }

    res.map(_.denormalise)
  }
}

object UpdateViewpointCommand {
  implicit val updateViewpointCommandFormat: Format[UpdateViewpointCommand] = (
    (JsPath \ "subjectId").format[Long] and
      (JsPath \ "viewpointId").format[Long] and
      (JsPath \ "commenterId").format[Long] and
      (JsPath \ "quote").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "date").formatNullable[DateTime]
    )(UpdateViewpointCommand.apply, unlift(UpdateViewpointCommand.unapply))
}

