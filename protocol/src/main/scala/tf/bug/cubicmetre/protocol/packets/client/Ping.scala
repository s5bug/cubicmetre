package tf.bug.cubicmetre.protocol.packets.client

import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.PacketRange

case class Ping(nonce: Long)

object Ping {

  trait Instances {

    implicit val nettyRewritePingPacket: PacketRange[Ping, 0, 578] =
      PacketRange[Ping, 0, 578](_ => 1, _ => implicitly)

  }

}
