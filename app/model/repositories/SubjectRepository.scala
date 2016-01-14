package model.repositories

import com.amazonaws.services.dynamodbv2.document.Table
import model.{Subject, Commenter}
import services.Dynamo

import scala.collection.JavaConversions._


trait SubjectRepositoryLike {

  def table: Table

  def getSubject(id: Long) = {
    Option(table.getItem("id", id)).map(Subject.fromItem)
  }

  def updateSubject(subject: Subject) = {
    try {
      table.putItem(subject.toItem)
      Some(subject)
    } catch {
      case e: Error => None
    }
  }

  def loadAllSubjects = table.scan().map(Subject.fromItem)

  def getSubjectsWithCommenter(commenterId: Long) = {
    loadAllSubjects.filter{s =>
      s.viewpoints.exists{vp => vp.commenterId == commenterId}
    }.toList
  }
}

object SubjectRepository extends SubjectRepositoryLike {
  override def table: Table = Dynamo.subjectTable
}

object PublishedSubjectRepository extends SubjectRepositoryLike {
  override def table: Table = Dynamo.publishedSubjectTable
}
