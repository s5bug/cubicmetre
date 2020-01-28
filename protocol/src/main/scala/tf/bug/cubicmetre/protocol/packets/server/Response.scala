package tf.bug.cubicmetre.protocol.packets.server

import io.circe._
import io.circe.generic.auto._
import io.circe.syntax._
import scodec.{Attempt, Codec, Decoder, Encoder, Err}
import tf.bug.cubicmetre.protocol.ProtocolVersion
import tf.bug.cubicmetre.protocol.chat.Text
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet

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

    implicit val oneFifteenTwoResponsePacket: Packet.`1.15.2`[Response] = new Packet[Response] {
      override type Version = ProtocolVersion.`1.15.2`
      override val version: ProtocolVersion[ProtocolVersion.`1.15.2`] = implicitly
      override val codec: Codec[Response] = implicitly
      override val id: Int = 0
    }

  }

}
