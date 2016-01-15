package controllers

import model.Commenter
import model.command._
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
    implicit val user = req.user

    req.body.asJson.map { json =>
      json.as[CreateCommenterCommand].process
      NoContent
    }.getOrElse(BadRequest)
  }

  def updateCommenter(id: Long) = AuthAction { req =>
    implicit val user = req.user

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
    implicit val user = req.user

    req.body.asJson.map { json =>
      val subject = json.as[CreateSubjectCommand].process
      subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
    }.getOrElse(BadRequest)
  }

  def updateSubject(id: Long) = AuthAction { req =>
    implicit val user = req.user

    SubjectRepository.getSubject(id).map { _ =>
      req.body.asJson.map { json =>
        val subject = json.as[UpdateSubjectCommand].process
        subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
      }.getOrElse(BadRequest)
    } getOrElse(NotFound)
  }

  def createViewpoint(subjectId: Long) = AuthAction { req =>
    implicit val user = req.user

    SubjectRepository.getSubject(subjectId).map { _ =>
      req.body.asJson.map { json =>
        val subject = json.as[CreateViewpointCommand].process
        subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
      }.getOrElse(BadRequest)
    } getOrElse(NotFound)
  }

  def updateViewpoint(subjectId: Long, viewpointId: Long) = AuthAction { req =>
    implicit val user = req.user

    SubjectRepository.getSubject(subjectId).map { _ =>
      req.body.asJson.map { json =>
        val subject = json.as[UpdateViewpointCommand].process
        subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
      }.getOrElse(BadRequest)
    } getOrElse(NotFound)
  }

  def deleteViewpoint(subjectId: Long, viewpointId: Long) = AuthAction { req =>
    implicit val user = req.user

    SubjectRepository.getSubject(subjectId).map { s =>
        val subject = new DeleteViewpointCommand(s, viewpointId).process
        subject.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
    } getOrElse(NotFound)
  }

  def publishSubject(subjectId: Long) = AuthAction { req =>
    implicit val user = req.user

    SubjectRepository.getSubject(subjectId).map { s =>
      val res = new PublishSubjectCommand(s).process()
      res.map{ s => Ok(Json.toJson(s))}.getOrElse(InternalServerError)
    } getOrElse(NotFound)
  }
}
