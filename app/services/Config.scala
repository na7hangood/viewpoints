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

  private lazy val remoteConfiguration: Map[String, String] = loadConfiguration

  def capiUrl: String = getRequiredStringProperty("capi.url")

  def capiKey: String = getRequiredStringProperty("capi.key")

  def getRequiredStringProperty(key: String): String = {
    remoteConfiguration.getOrElse(key, {
      throw new IllegalArgumentException(s"Property '$key' not configured")
    })
  }

  object aws {
    lazy val stack = readTag("Stack") getOrElse "flexible"
    lazy val stage = readTag("Stage") getOrElse "DEV"
    lazy val app = readTag("App") getOrElse "viewpoints"
  }

  private def loadConfiguration = {

    val bucketName = s"guconf-${aws.stack}"

    def loadPropertiesFromS3(propertiesKey: String, props: Properties): Unit = {
      val s3Properties = AWS.S3Client.getObject(new GetObjectRequest(bucketName, propertiesKey))
      val propertyInputStream = s3Properties.getObjectContent
      try {
        props.load(propertyInputStream)
      } finally {
        try {
          propertyInputStream.close()
        } catch {
          case _: Throwable => /*ignore*/
        }
      }
    }

    val props = new Properties()

    loadPropertiesFromS3(s"${aws.app}/global.properties", props)
    loadPropertiesFromS3(s"${aws.app}/${aws.stage}.properties", props)

    props.toMap
  }

  lazy val permissionsStage: String = readTag("Stage") match {
    case Some("DEV") => "CODE"
    case Some(stage) => stage
    case _ => "CODE"
  }

  def logShippingStreamName: Option[String]
  def pandaDomain: String
  def pandaAuthCallback: String
}

class DevConfig extends Config {
  override def logShippingStreamName = Some("elk-CODE-KinesisStream-M03ERGK5PVD9")
  override def pandaDomain: String = "local.dev-gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.local.dev-gutools.co.uk/oauthCallback"
}

class CodeConfig extends Config {
  override def logShippingStreamName = Some("elk-PROD-KinesisStream-1PYU4KS1UEQA")
  override def pandaDomain: String = "code.dev-gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.code.dev-gutools.co.uk/oauthCallback"
}

class ProdConfig extends Config {
  override def logShippingStreamName = Some("elk-PROD-KinesisStream-1PYU4KS1UEQA")
  override def pandaDomain: String = "gutools.co.uk"
  override def pandaAuthCallback: String = "https://viewpoints.gutools.co.uk/oauthCallback"
}


