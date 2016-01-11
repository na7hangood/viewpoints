package model.repositories

import model.{Subject, Commenter}
import services.Dynamo

import scala.collection.JavaConversions._


object SubjectRepository {

  def getSubject(id: Long) = {
    Option(Dynamo.subjectTable.getItem("id", id)).map(Subject.fromItem)
  }

  def updateSubject(subject: Subject) = {
    try {
      Dynamo.subjectTable.putItem(subject.toItem)
      Some(subject)
    } catch {
      case e: Error => None
    }
  }

  def loadAllSubjects = Dynamo.subjectTable.scan().map(Subject.fromItem)
}
