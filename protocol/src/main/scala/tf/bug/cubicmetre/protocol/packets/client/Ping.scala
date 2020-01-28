package tf.bug.cubicmetre.protocol.packets.client

import scodec.Codec
import tf.bug.cubicmetre.protocol.ProtocolVersion
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet

case class Ping(nonce: Long)

object Ping {

  trait Instances {

    implicit val oneFifteenTwoPingPacket: Packet.`1.15.2`[Ping] = new Packet[Ping] {
      override type Version = ProtocolVersion.`1.15.2`
      override val version: ProtocolVersion[ProtocolVersion.`1.15.2`] = implicitly
      override val codec: Codec[Ping] = implicitly
      override val id: Int = 1
    }

  }

}
