package model.repositories

import model.Commenter
import services.Dynamo
import scala.collection.JavaConversions._


object CommenterRepository {

  def getCommenter(id: Long) = {
    Option(Dynamo.commenterTable.getItem("id", id)).map(Commenter.fromItem)
  }

  def updateCommenter(commenter: Commenter) = {
    try {
      Dynamo.commenterTable.putItem(commenter.toItem)
      Some(commenter)
    } catch {
      case e: Error => None
    }
  }

  def loadAllCommenters = Dynamo.commenterTable.scan().map(Commenter.fromItem)
}
