package services

import java.nio.ByteBuffer
import com.amazonaws.regions.{Regions, Region}
import com.amazonaws.services.cloudwatch.AmazonCloudWatchAsyncClient
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.ec2.AmazonEC2Client
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.ec2.model.{Filter, DescribeTagsRequest}
import com.amazonaws.services.kinesis.AmazonKinesisClient
import com.amazonaws.services.sqs.AmazonSQSClient
import com.amazonaws.util.EC2MetadataUtils
import com.twitter.scrooge.ThriftStruct
import scala.collection.JavaConverters._

object AWS {

  lazy val region = Region getRegion Regions.EU_WEST_1

  lazy val EC2Client = region.createClient(classOf[AmazonEC2Client], null, null)
  lazy val CloudWatch = region.createClient(classOf[AmazonCloudWatchAsyncClient], null, null)
  lazy val Kinesis = region.createClient(classOf[AmazonKinesisClient], null, null)
  lazy val S3Client = region.createClient(classOf[AmazonS3Client], null, null)


}

trait AwsInstanceTags {
  lazy val instanceId = Option(EC2MetadataUtils.getInstanceId)

  def readTag(tagName: String) = {
    instanceId.flatMap { id =>
      val tagsResult = AWS.EC2Client.describeTags(
        new DescribeTagsRequest().withFilters(
          new Filter("resource-type").withValues("instance"),
          new Filter("resource-id").withValues(id),
          new Filter("key").withValues(tagName)
        )
      )
      tagsResult.getTags.asScala.find(_.getKey == tagName).map(_.getValue)
    }
  }
}

object Dynamo {
  lazy val client = AWS.region.createClient(classOf[AmazonDynamoDBClient], null, null)
  lazy val dynamoDb = new DynamoDB(client)

  lazy val commenterTable = dynamoDb.getTable(Config().commenterTableName)
  lazy val subjectTable = dynamoDb.getTable(Config().subjectTableName)
  lazy val sequenceTable = dynamoDb.getTable(Config().sequenceTableName)
}


class KinesisStreamProducer(streamName: String) {

  def publishUpdate(key: String, data: String) {
    publishUpdate(key, ByteBuffer.wrap(data.getBytes("UTF-8")))
  }

  def publishUpdate(key: String, data: Array[Byte]) {
    publishUpdate(key, ByteBuffer.wrap(data))
  }

//  def publishUpdate(key: String, struct: ThriftStruct) {
//    publishUpdate(key, ByteBuffer.wrap(ThriftSerializer.serializeToBytes(struct)))
//  }

  def publishUpdate(key: String, dataBuffer: ByteBuffer) {
    AWS.Kinesis.putRecord(streamName, dataBuffer, key)
  }
}

object KinesisStreams {
//  lazy val tagUpdateStream = new KinesisStreamProducer(Config().tagUpdateStreamName)
//  lazy val sectionUpdateStream = new KinesisStreamProducer(Config().sectionUpdateStreamName)
//  lazy val taggingOperationsStream = new KinesisStreamProducer(Config().taggingOperationsStreamName)
}
