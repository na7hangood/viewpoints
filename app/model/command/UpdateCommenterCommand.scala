package model.command

import model.Commenter
import model.repositories.CommenterRepository


case class UpdateCommenterCommand(commenter: Commenter) extends Command {
  override type T = Commenter

  override def process(): Option[T] = {
    CommenterRepository.updateCommenter(commenter)

    // reindex atoms here.
  }
}
