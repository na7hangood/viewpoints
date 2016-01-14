package model.command

import com.gu.pandomainauth.model.User
import model.{DenormalisedSubject, Subject}
import model.repositories.SubjectRepository
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}
import services.AtomPublisher

case class UpdateSubjectCommand(id: Long, name: String, link: Option[String] = None) extends Command {

  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {
    val originalSubject = SubjectRepository.getSubject(id).get
    val modifiedSubject = originalSubject.copy(
      name = name,
      link = sanitise(link),
      revision = originalSubject.revision + 1,
      lastModified = Some(changeRecord(user))
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    res foreach{ s => AtomPublisher.publishDraft(s) }

    res.map(_.denormalise)
  }
}

object UpdateSubjectCommand {
  implicit val createCommenterCommandFormat: Format[UpdateSubjectCommand] = (
    (JsPath \ "id").format[Long] and
      (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String]
    )(UpdateSubjectCommand.apply, unlift(UpdateSubjectCommand.unapply))
}
