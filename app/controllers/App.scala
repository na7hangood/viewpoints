package controllers

import java.util.Date

import model.repositories.SubjectRepository
import model.{Commenter, Viewpoint, Subject}
import org.joda.time.DateTime
import play.api.Logger
import play.api.mvc.{Action, Controller}

object App extends Controller with PanDomainAuthActions {

  def index = AuthAction {
    Ok(views.html.Application.app("Viewpoints"))
  }

  def preview(subjectId: Long) = AuthAction {
    SubjectRepository.getSubject(subjectId).map{ s =>
      val subject = s.denormalise
      Ok(views.html.Application.preview(subject))

    }.getOrElse(NotFound)
  }

}
