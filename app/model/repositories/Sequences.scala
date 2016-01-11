package model.repositories

import com.amazonaws.services.dynamodbv2.document.spec.UpdateItemSpec
import com.amazonaws.services.dynamodbv2.document.{AttributeUpdate, Item, Table}
import com.amazonaws.services.dynamodbv2.model.ReturnValue
import services.Dynamo


object Sequences {

  val commenterId = new DynamoSequence(Dynamo.sequenceTable, "commenterId")
}

class DynamoSequence(sequenceTable: Table, sequenceName: String) {
  def getNextId: Long = {
    val incResult = sequenceTable.updateItem(
      new UpdateItemSpec().withPrimaryKey("sequenceName", sequenceName)
        .withAttributeUpdate(new AttributeUpdate("value").addNumeric(1))
        .withReturnValues(ReturnValue.ALL_NEW)
    )
    incResult.getItem.getLong("value")
  }

  // debug methods
  def getCurrentId: Long = {
    Dynamo.sequenceTable.getItem("sequenceName", sequenceName).getLong("value")
  }

  def setCurrentId(v: Long) {
    Dynamo.sequenceTable.putItem(new Item().withString("sequenceName", sequenceName).withLong("value", v))
  }
}
