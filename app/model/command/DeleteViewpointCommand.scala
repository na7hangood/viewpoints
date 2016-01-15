package model.command

import com.gu.pandomainauth.model.User
import model.repositories.SubjectRepository
import model.{DenormalisedSubject, Subject}
import services.AtomPublisher

case class DeleteViewpointCommand(subject: Subject, viewpointId: Long) extends Command {
  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {
    val modifiedSubject = subject.copy(
      viewpoints = subject.viewpoints.filterNot(_.id == viewpointId),
      revision = subject.revision + 1,
      lastModified = Some(changeRecord(user))
    )

    val res = SubjectRepository.updateSubject(modifiedSubject)

    res foreach{ s => AtomPublisher.publishDraft(s) }

    res.map(_.denormalise)
  }
}
