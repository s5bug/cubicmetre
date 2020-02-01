package tf.bug.cubicmetre.protocol.packets.client

import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.PacketRange

case class Request()

object Request {

  trait Instances {

    implicit val nettyRewriteRequestPacket: PacketRange[Request, 0, 578] =
      PacketRange[Request, 0, 578](_ => 0, _ => implicitly)

  }

}
