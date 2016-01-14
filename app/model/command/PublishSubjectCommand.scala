package model.command

import com.gu.pandomainauth.model.User
import model.{Subject, DenormalisedSubject}
import model.repositories.{PublishedSubjectRepository, SubjectRepository}
import services.AtomPublisher


case class PublishSubjectCommand(subject: Subject) extends Command {
  override type T = DenormalisedSubject

  override def process()(implicit user: User): Option[T] = {

    val cr = changeRecord(user)

    val publishableSubject = subject.copy(
      revision = subject.revision + 1,
      lastModified = Some(cr),
      published = Some(cr)
    )

    val pubRes = PublishedSubjectRepository.updateSubject(publishableSubject)
    val res = SubjectRepository.updateSubject(pubRes.get)

    res.foreach{ s =>
      AtomPublisher.publishLive(s)
      AtomPublisher.publishDraft(s)
    }

    res.map(_.denormalise)
  }
}
