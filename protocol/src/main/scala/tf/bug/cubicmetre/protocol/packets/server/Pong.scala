package tf.bug.cubicmetre.protocol.packets.server

import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.PacketRange

case class Pong(nonce: Long)

object Pong {

  trait Instances {

    implicit val nettyRewritePongPacket: PacketRange[Pong, 0, 578] =
      PacketRange[Pong, 0, 578](_ => 1, _ => implicitly)

  }

}
