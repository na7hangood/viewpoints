package model.command

import com.gu.pandomainauth.model.User
import model.{Viewpoint, DenormalisedSubject}
import model.repositories.{Sequences, SubjectRepository}
import org.joda.time.DateTime
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}
import services.AtomPublisher

case class CreateViewpointCommand(
  subjectId: Long,
  commenterId: Long,
  quote: String,
  link: Option[String],
  date: Option[DateTime]
 ) extends Command {

  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {
    val originalSubject = SubjectRepository.getSubject(subjectId).get

    val viewpoint = Viewpoint(
      id = Sequences.viewpointId.getNextId,
      commenterId = commenterId,
      quote = quote,
      link = sanitise(link),
      date = date
    )

    val modifiedSubject = originalSubject.copy(
      viewpoints = originalSubject.viewpoints ::: viewpoint :: Nil,
      revision = originalSubject.revision + 1,
      lastModified = Some(changeRecord(user))
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    res foreach{ s => AtomPublisher.publishDraft(s) }

    res.map(_.denormalise)
  }
}

object CreateViewpointCommand {
  implicit val createViewpointCommandFormat: Format[CreateViewpointCommand] = (
    (JsPath \ "subjectId").format[Long] and
      (JsPath \ "commenterId").format[Long] and
      (JsPath \ "quote").format[String] and
      (JsPath \ "link").formatNullable[String] and
      (JsPath \ "date").formatNullable[DateTime]
    )(CreateViewpointCommand.apply, unlift(CreateViewpointCommand.unapply))
}
