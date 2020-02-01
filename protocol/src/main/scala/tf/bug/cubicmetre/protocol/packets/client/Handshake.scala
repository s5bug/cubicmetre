package tf.bug.cubicmetre.protocol.packets.client

import spire.math.UShort
import tf.bug.cubicmetre.protocol.VarInt
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.PacketRange

case class Handshake(
  protocolVersion: VarInt,
  serverAddress: String,
  serverPort: UShort,
  nextState: VarInt
)

object Handshake {

  trait Instances {

    implicit val nettyRewriteHandshakePacket: PacketRange[Handshake, 0, 578] =
      PacketRange[Handshake, 0, 578](_ => 0, _ => implicitly)

  }

}
