package model.command

import com.gu.pandomainauth.model.User
import model.ChangeRecord


trait Command {
  type T

  def process()(implicit user: User): Option[T]

  def changeRecord(user: User) = ChangeRecord(user)

  def sanitise(s: Option[String]): Option[String] = {
    s.map(_.trim) match {
      case Some("") => None
      case o => o
    }
  }

}
