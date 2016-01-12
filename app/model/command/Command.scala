package model.command


trait Command {
  type T

  def process(): Option[T]

}
