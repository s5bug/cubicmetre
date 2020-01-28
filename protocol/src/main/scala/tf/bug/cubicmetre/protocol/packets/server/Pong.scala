package tf.bug.cubicmetre.protocol.packets.server

import scodec.Codec
import tf.bug.cubicmetre.protocol.ProtocolVersion
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet

case class Pong(nonce: Long)

object Pong {

  trait Instances {

    implicit val oneFifteenTwoPongPacket: Packet.`1.15.2`[Pong] = new Packet[Pong] {
      override type Version = ProtocolVersion.`1.15.2`
      override val version: ProtocolVersion[ProtocolVersion.`1.15.2`] = implicitly
      override val codec: Codec[Pong] = implicitly
      override val id: Int = 1
    }

  }

}
