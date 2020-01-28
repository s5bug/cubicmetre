package tf.bug.cubicmetre.protocol.packets.client

import scodec.Codec
import tf.bug.cubicmetre.protocol.ProtocolVersion
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet

case class Request()

object Request {

  trait Instances {

    implicit val oneFifteenTwoRequestPacket: Packet.`1.15.2`[Request] = new Packet[Request] {
      override type Version = ProtocolVersion.`1.15.2`
      override val version: ProtocolVersion[ProtocolVersion.`1.15.2`] = implicitly
      override val codec: Codec[Request] = implicitly
      override val id: Int = 0
    }

  }

}
