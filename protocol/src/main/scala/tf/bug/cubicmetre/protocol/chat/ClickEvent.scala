package tf.bug.cubicmetre.protocol.chat

sealed trait ClickEvent {

  val action: String
  val value: String

}

object ClickEvent {

  case class OpenUrl(value: String) extends ClickEvent {
    override val action: String = "open_url"
  }

  case class SendInChat(value: String) extends ClickEvent {
    override val action: String = "run_command"
  }

  case class SuggestInChat(value: String) extends ClickEvent {
    override val action: String = "suggest_command"
  }

  case class ChangePage(number: Int) extends ClickEvent {
    override val value: String = number.toString
    override val action: String = "change_page"
  }

}
