package controllers

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

  def getSubjects = AuthAction {
    Ok(Json.toJson(SubjectRepository.loadAllSubjects))
  }

  def getSubject(id: Long) = AuthAction {
    SubjectRepository.getSubject(id) map { s =>
      Ok(Json.toJson(s))
    } getOrElse NotFound
  }

}
