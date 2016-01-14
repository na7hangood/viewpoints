package model

import com.gu.contentatom.thrift.{User => ThriftUser}
import com.gu.pandomainauth.model.{User => PandomainUser}
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsPath, Format}

case class User(firstName: Option[String], lastName: Option[String], email: String) {
  def toThrift: ThriftUser = {
    ThriftUser(
      firstName = firstName,
      lastName = lastName,
      email = email
    )
  }
}

object User {
  implicit val userFormat: Format[User] = (
    (JsPath \ "firstName").formatNullable[String] and
      (JsPath \ "lastName").formatNullable[String] and
      (JsPath \ "email").format[String]
    )(User.apply, unlift(User.unapply))

  def apply(pandomainUser: PandomainUser): User = {
    new User(
      firstName = Some(pandomainUser.firstName),
      lastName = Some(pandomainUser.lastName),
      email = pandomainUser.email
    )
  }
}
