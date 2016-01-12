package model.command

import model.Subject
import model.repositories.{SubjectRepository, Sequences}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}


case class CreateSubjectCommand(name: String, link: Option[String] = None) extends Command {

  override type T = Subject

  override def process(): Option[T] = {
    val subject = Subject(
      id = Sequences.subjectId.getNextId,
      name = name,
      link = link,
      viewpoints = Nil,
      revision = 1l
    )

    SubjectRepository.updateSubject(subject)

    //publish draft to go here
  }
}

object CreateSubjectCommand {
  implicit val createCommenterCommandFormat: Format[CreateSubjectCommand] = (
    (JsPath \ "name").format[String] and
      (JsPath \ "link").formatNullable[String]
    )(CreateSubjectCommand.apply, unlift(CreateSubjectCommand.unapply))
}
