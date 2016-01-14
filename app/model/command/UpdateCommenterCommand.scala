package model.command

import com.gu.pandomainauth.model.User
import model.Commenter
import model.repositories.{SubjectRepository, CommenterRepository}
import services.AtomPublisher


case class UpdateCommenterCommand(commenter: Commenter) extends Command {
  override type T = Commenter

  override def process()(implicit user: User): Option[T] = {
    val res = CommenterRepository.updateCommenter(commenter)

    SubjectRepository.getSubjectsWithCommenter(commenter.id) foreach {s =>
      AtomPublisher.publishDraft(s)
    }

    res
  }
}
