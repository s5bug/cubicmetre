package tf.bug.cubicmetre.protocol.packets.server

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import scodec.{Attempt, Codec, Decoder, Encoder, Err}
import tf.bug.cubicmetre.protocol.chat.Text
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.PacketRange

case class Response(
  responseJson: ResponseJson
)

case class ResponseJson(
  version: ResponseVersion,
  players: ResponsePlayers,
  description: Text,
  favicon: Option[String]
)

case class ResponseVersion(
  name: String,
  protocol: Int
)

case class ResponsePlayers(
  max: Int,
  online: Int,
  sample: Vector[Json] // TODO
)

object Response {

  trait Instances {

    implicit val responseJsonCodec: Codec[ResponseJson] =
      Codec(
        Encoder { j: ResponseJson =>
          stringCodec.encode(j.asJson.printWith(Printer.noSpaces.copy(dropNullValues = true)))
        },
        Decoder(_ => Attempt.failure(Err("decoding json not implemented")))
      )

    implicit val nettyRewriteResponsePacket: PacketRange[Response, 0, 578] =
      PacketRange[Response, 0, 578](_ => 0, _ => implicitly)

  }

}
