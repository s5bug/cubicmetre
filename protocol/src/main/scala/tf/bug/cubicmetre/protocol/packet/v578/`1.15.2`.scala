package tf.bug.cubicmetre.protocol.packet.v578

import polymorphic.Exists
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packet.Packet
import tf.bug.cubicmetre.protocol.packet.all

object `1.15.2` {

  val handshakePackets: Vector[Exists[Packet]] = Vector(
    Exists.apply(implicitly[Packet[all.Handshake]])
  )

  val playPackets: Vector[Exists[Packet]] = Vector(
    )

  val statusPackets: Vector[Exists[Packet]] = Vector(
    )

  val loginPackets: Vector[Exists[Packet]] = Vector(
    )

}
