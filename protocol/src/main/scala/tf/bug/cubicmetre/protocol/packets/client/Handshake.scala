package tf.bug.cubicmetre.protocol.packets.client

import scodec.Codec
import spire.math.UShort
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet
import tf.bug.cubicmetre.protocol.{ProtocolVersion, VarInt}

case class Handshake(
  protocolVersion: VarInt,
  serverAddress: String,
  serverPort: UShort,
  nextState: VarInt
)

object Handshake {

  trait Instances {

    implicit val oneFifteenTwoHandshakePacket: Packet.`1.15.2`[Handshake] = new Packet[Handshake] {
      override type Version = ProtocolVersion.`1.15.2`
      override val version: ProtocolVersion[ProtocolVersion.`1.15.2`] = implicitly
      override val id: Int = 0
      override val codec: Codec[Handshake] = implicitly
    }

  }

}
