package model.command

import com.gu.pandomainauth.model.User
import model.{DenormalisedSubject, Subject}
import model.repositories.{SubjectRepository, Sequences}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}
import services.AtomPublisher


case class CreateSubjectCommand(name: String, link: Option[String] = None) extends Command {

  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {

    val cr = changeRecord(user)
    val subject = Subject(
      id = Sequences.subjectId.getNextId,
      name = name,
      link = link,
      viewpoints = Nil,
      revision = 1l,
      lastModified = Some(cr),
      created = Some(cr),
      published = None
    )

    val res = SubjectRepository.updateSubject(subject)

    res foreach{ s => AtomPublisher.publishDraft(s) }

    res.map(_.denormalise)
  }
}

object CreateSubjectCommand {
  implicit val createCommenterCommandFormat: Format[CreateSubjectCommand] = (
    (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String]
    )(CreateSubjectCommand.apply, unlift(CreateSubjectCommand.unapply))
}
