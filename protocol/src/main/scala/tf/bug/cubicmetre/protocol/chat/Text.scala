package tf.bug.cubicmetre.protocol.chat

import java.util.UUID

import cats.data.NonEmptyVector
import io.circe._
import io.circe.generic.auto._

sealed trait Text {

  val bold: Option[Boolean]
  val italic: Option[Boolean]
  val underlined: Option[Boolean]
  val strikethrough: Option[Boolean]
  val obfuscated: Option[Boolean]
  val color: Option[Color]

  val insertion: Option[String]

  val clickEvent: Option[ClickEvent]
  val hoverEvent: Option[HoverEvent]

  val extra: Option[NonEmptyVector[Text]]

}

case class Constant(
  text: String,
  bold: Option[Boolean] = None,
  italic: Option[Boolean] = None,
  underlined: Option[Boolean] = None,
  strikethrough: Option[Boolean] = None,
  obfuscated: Option[Boolean] = None,
  color: Option[Color] = None,
  insertion: Option[String] = None,
  clickEvent: Option[ClickEvent] = None,
  hoverEvent: Option[HoverEvent] = None,
  extra: Option[NonEmptyVector[Text]] = None
) extends Text

case class Translation(
  key: String,
  `with`: Option[Vector[Text]],
  bold: Option[Boolean] = None,
  italic: Option[Boolean] = None,
  underlined: Option[Boolean] = None,
  strikethrough: Option[Boolean] = None,
  obfuscated: Option[Boolean] = None,
  color: Option[Color] = None,
  insertion: Option[String] = None,
  clickEvent: Option[ClickEvent] = None,
  hoverEvent: Option[HoverEvent] = None,
  extra: Option[NonEmptyVector[Text]] = None
) extends Text

case class Keybind(
  keybind: String,
  bold: Option[Boolean] = None,
  italic: Option[Boolean] = None,
  underlined: Option[Boolean] = None,
  strikethrough: Option[Boolean] = None,
  obfuscated: Option[Boolean] = None,
  color: Option[Color] = None,
  insertion: Option[String] = None,
  clickEvent: Option[ClickEvent] = None,
  hoverEvent: Option[HoverEvent] = None,
  extra: Option[NonEmptyVector[Text]] = None
) extends Text

case class Score(
  score: ScoreComponent,
  bold: Option[Boolean] = None,
  italic: Option[Boolean] = None,
  underlined: Option[Boolean] = None,
  strikethrough: Option[Boolean] = None,
  obfuscated: Option[Boolean] = None,
  color: Option[Color] = None,
  insertion: Option[String] = None,
  clickEvent: Option[ClickEvent] = None,
  hoverEvent: Option[HoverEvent] = None,
  extra: Option[NonEmptyVector[Text]] = None
) extends Text

case class ScoreComponent(name: Either[String, UUID], objective: String, value: String)

object Text {

  import tf.bug.cubicmetre.protocol.implicits.minecraftColorAsJson

  implicit lazy val nonEmptyTextVector: Encoder[NonEmptyVector[Text]] =
    implicitly[Encoder[Vector[Text]]](Encoder.encodeVector(textEncoder)).contramap(_.toVector)

  lazy val textEncoder: Encoder[Text] =
    implicitly[Encoder[Text]].mapJson(o => o.asObject.get.apply(o.asObject.get.keys.head).get)
  lazy val textDecoder: Decoder[Text] = implicitly

  trait Instances {

    implicit val minecraftTextEncoder: Encoder[Text] = textEncoder
    implicit val minecraftTextDecoder: Decoder[Text] = textDecoder

  }

}
