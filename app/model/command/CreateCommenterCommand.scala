package model.command

import model.Commenter
import model.repositories.{CommenterRepository, Sequences}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}


case class CreateCommenterCommand(
  name: String,
  imageUrl: Option[String] = None,
  description: Option[String] = None,
  party: Option[String] = None,
  category: Option[String] = None) extends Command {

  override type T = Commenter

  override def process(): Option[T] = {
    val commenter = Commenter(
      id = Sequences.commenterId.getNextId,
      name = name,
      imageUrl = imageUrl,
      description = description,
      party = party,
      category = category
    )

    CommenterRepository.updateCommenter(commenter)
  }
}

object CreateCommenterCommand {
  implicit val createCommenterCommandFormat: Format[CreateCommenterCommand] = (
      (JsPath \ "name").format[String] and
      (JsPath \ "imageUrl").formatNullable[String] and
      (JsPath \ "description").formatNullable[String] and
      (JsPath \ "party").formatNullable[String] and
      (JsPath \ "category").formatNullable[String]
    )(CreateCommenterCommand.apply, unlift(CreateCommenterCommand.unapply))
}
