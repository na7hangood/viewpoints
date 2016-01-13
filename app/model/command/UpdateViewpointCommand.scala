package model.command

import model.{Viewpoint, DenormalisedSubject}
import model.command.Command
import model.repositories.{Sequences, SubjectRepository}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}


case class UpdateViewpointCommand (
  subjectId: Long,
  viewpointId: Long,
  commenterId: Long,
  quote: String,
  link: Option[String]
  ) extends Command {

  override type T = DenormalisedSubject

  override def process(): Option[T] = {
    val originalSubject = SubjectRepository.getSubject(subjectId).get

    val viewpoint = Viewpoint(
      id = Sequences.viewpointId.getNextId,
      commenterId = commenterId,
      quote = quote,
      link = link,
      date = None
    )

    val modifiedSubject = originalSubject.copy(
      viewpoints = originalSubject.viewpoints.map { vp =>
        if (vp.id == viewpointId) {
          vp.copy(
            commenterId = commenterId,
            quote = quote,
            link = link
          )
        } else {
          vp
        }
      },
      revision = originalSubject.revision + 1
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    //publish draft to go here

    res.map(_.denormalise)
  }
}

object UpdateViewpointCommand {
  implicit val updateViewpointCommandFormat: Format[UpdateViewpointCommand] = (
    (JsPath \ "subjectId").format[Long] and
      (JsPath \ "viewpointId").format[Long] and
      (JsPath \ "commenterId").format[Long] and
      (JsPath \ "quote").format[String] and
      (JsPath \ "link").formatNullable[String]
    )(UpdateViewpointCommand.apply, unlift(UpdateViewpointCommand.unapply))
}

