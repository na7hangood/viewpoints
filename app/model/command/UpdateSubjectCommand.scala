package model.command

import model.{DenormalisedSubject, Subject}
import model.repositories.SubjectRepository
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}

case class UpdateSubjectCommand(id: Long, name: String, link: Option[String] = None) extends Command {

  override type T = DenormalisedSubject

  override def process(): Option[T] = {
    val originalSubject = SubjectRepository.getSubject(id).get
    val modifiedSubject = originalSubject.copy(
      name = name,
      link = link,
      revision = originalSubject.revision + 1
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    //publish draft to go here

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
