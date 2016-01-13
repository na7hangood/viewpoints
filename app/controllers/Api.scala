package controllers

import model.Commenter
import model.command.{UpdateSubjectCommand, CreateSubjectCommand, UpdateCommenterCommand, CreateCommenterCommand}
import model.repositories.{SubjectRepository, CommenterRepository}
import play.api.libs.json.Json
import play.api.mvc.Controller

object Api extends Controller with PanDomainAuthActions {

  def getCommenters = AuthAction {
    Ok(Json.toJson(CommenterRepository.loadAllCommenters))
  }
  
  def getCommenter(id: Long) = AuthAction {
    CommenterRepository.getCommenter(id) map { c =>
      Ok(Json.toJson(c))
    } getOrElse NotFound
  }

  def createCommenter = AuthAction { req =>
    req.body.asJson.map { json =>
      json.as[CreateCommenterCommand].process
      NoContent
    }.getOrElse(BadRequest)
  }

  def updateCommenter(id: Long) = AuthAction { req =>
    CommenterRepository.getCommenter(id).map { _ =>
      req.body.asJson.map { json =>
        val commenter = json.as[Commenter]
        UpdateCommenterCommand(commenter).process
        NoContent
      }.getOrElse(BadRequest)
    } getOrElse(NotFound)
  }

  def getSubjects = AuthAction {
    Ok(Json.toJson(SubjectRepository.loadAllSubjects))
  }

  def getSubject(id: Long) = AuthAction {
    SubjectRepository.getSubject(id) map { s =>
      Ok(Json.toJson(s.denormalise))
    } getOrElse NotFound
  }

  def createSubject = AuthAction { req =>
    req.body.asJson.map { json =>
      val subject = json.as[CreateSubjectCommand].process
      subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
    }.getOrElse(BadRequest)
  }

  def updateSubject(id: Long) = AuthAction { req =>
    CommenterRepository.getCommenter(id).map { _ =>
      req.body.asJson.map { json =>
        val subject = json.as[UpdateSubjectCommand].process
        subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
      }.getOrElse(BadRequest)
    } getOrElse(NotFound)
  }

}
