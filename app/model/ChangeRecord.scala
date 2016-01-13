package model

import org.joda.time.DateTime
import com.gu.pandomainauth.model.{User => PandomainUser}
import com.gu.contentatom.thrift.{ChangeRecord => ThriftChangeRecord}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}


case class ChangeRecord(date: DateTime, user: Option[User]) {
  def toThrift: ThriftChangeRecord = {
    ThriftChangeRecord(
      date = date.getMillis,
      user = user.map(_.toThrift)
    )
  }
}

object ChangeRecord {
  implicit val changeRecordFormat: Format[ChangeRecord] = (
    (JsPath \ "date").format[DateTime] and
      (JsPath \ "user").formatNullable[User]
    )(ChangeRecord.apply, unlift(ChangeRecord.unapply))

  def apply(pandomainUser: PandomainUser): ChangeRecord = {
    new ChangeRecord(DateTime.now(), Some(User(pandomainUser)))
  }

}
