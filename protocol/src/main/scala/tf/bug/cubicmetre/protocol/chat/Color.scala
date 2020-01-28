package tf.bug.cubicmetre.protocol.chat

import io.circe.Encoder

sealed trait Color {

  val name: String

}

object Color {

  case object Black extends Color {
    override val name = "black"
  }
  case object DarkBlue extends Color {
    override val name = "dark_blue"
  }
  case object DarkGreen extends Color {
    override val name = "dark_green"
  }
  case object DarkCyan extends Color {
    override val name = "dark_aqua"
  }
  case object DarkRed extends Color {
    override val name = "dark_red"
  }
  case object Purple extends Color {
    override val name = "dark_purple"
  }
  case object Gold extends Color {
    override val name = "gold"
  }
  case object Gray extends Color {
    override val name = "gray"
  }
  case object DarkGray extends Color {
    override val name = "dark_gray"
  }
  case object Blue extends Color {
    override val name = "blue"
  }
  case object Green extends Color {
    override val name = "green"
  }
  case object Cyan extends Color {
    override val name = "aqua"
  }
  case object Red extends Color {
    override val name = "red"
  }
  case object Pink extends Color {
    override val name = "light_purple"
  }
  case object Yellow extends Color {
    override val name = "yellow"
  }
  case object White extends Color {
    override val name = "white"
  }

  case object Reset extends Color {
    override val name = "reset"
  }

  trait Instances {

    implicit val minecraftColorAsJson: Encoder[Color] = Encoder.encodeString.contramap(_.name)

  }

}
