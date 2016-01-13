package services

import java.util.Properties

import com.amazonaws.services.s3.model.GetObjectRequest
import services.Config._

import scala.collection.JavaConversions._


object Config extends AwsInstanceTags {

  lazy val conf = readTag("Stage") match {
    case Some("PROD") =>    new ProdConfig
    case Some("CODE") =>    new CodeConfig
    case _ =>               new DevConfig
  }

  def apply() = {
    conf
  }
}

sealed trait Config {

  def commenterTableName: String
  def subjectTableName: String
  def sequenceTableName: String

  def liveAtomStreamName: String
  def previewAtomStreamName: String

  def logShippingStreamName: Option[String]
  def pandaDomain: String
  def pandaAuthCallback: String
}

class DevConfig extends Config {
  override def commenterTableName = "viewpoints-commenter-dev"
  override def subjectTableName = "viewpoints-subject-dev"
  override def sequenceTableName = "viewpoints-sequence-dev"

  override def liveAtomStreamName = "atom-viewpoints-live-CODE"
  override def previewAtomStreamName = "atom-viewpoints-preview-CODE"

  override def logShippingStreamName = Some("elk-CODE-KinesisStream-M03ERGK5PVD9")
  override def pandaDomain: String = "local.dev-gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.local.dev-gutools.co.uk/oauthCallback"
}

class CodeConfig extends Config {
  override def commenterTableName = "viewpoints-commenter-dev"
  override def subjectTableName = "viewpoints-subject-dev"
  override def sequenceTableName = "viewpoints-sequence-dev"

  override def liveAtomStreamName = "atom-viewpoints-live-CODE"
  override def previewAtomStreamName = "atom-viewpoints-preview-CODE"

  override def logShippingStreamName = Some("elk-PROD-KinesisStream-1PYU4KS1UEQA")
  override def pandaDomain: String = "code.dev-gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.code.dev-gutools.co.uk/oauthCallback"
}

class ProdConfig extends Config {
  override def commenterTableName = "viewpoints-commenter-prod"
  override def subjectTableName = "viewpoints-subject-prod"
  override def sequenceTableName = "viewpoints-sequence-prod"

  override def liveAtomStreamName = "atom-viewpoints-live-PROD"
  override def previewAtomStreamName = "atom-viewpoints-preview-PROD"

  override def logShippingStreamName = Some("elk-PROD-KinesisStream-1PYU4KS1UEQA")
  override def pandaDomain: String = "gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.gutools.co.uk/oauthCallback"
}


