package services

import com.gu.contentatom.thrift._
import model.Subject
import play.twirl.api.Html


object AtomPublisher {

  def publishDraft(subject: Subject): Unit = {
    //KinesisStreams.previewAtomStream.publishUpdate(subject.id.toString, updateEvent(subject))
  }

  def publishLive(subject: Subject): Unit = {
    //KinesisStreams.liveAtomStream.publishUpdate(subject.id.toString, updateEvent(subject))
  }

  private def updateEvent(subject: Subject): ContentAtomEvent = {
    val defaultHtml: Html = views.html.Application.defaultRendering(subject.denormalise)

    val contentChangeDetails = ContentChangeDetails(
      lastModified = subject.lastModified.map( _.toThrift ),
      created = subject.created.map( _.toThrift ),
      published = subject.published.map( _.toThrift ),
      revision = subject.revision
    )

    ContentAtomEvent(
      atom = Atom(
        id = s"viewpoints/${subject.id}",
        atomType = AtomType.Viewpoints,
        labels = Nil,
        defaultHtml = defaultHtml.toString(),
        data = AtomData.Viewpoints(subject.toThrift),
        contentChangeDetails = contentChangeDetails
      ),
      eventType = EventType.Update,
      eventCreationTime = System.currentTimeMillis
    )
  }

}
