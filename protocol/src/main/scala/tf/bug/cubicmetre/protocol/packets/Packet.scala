package tf.bug.cubicmetre.protocol.packets

import scodec.Codec
import tf.bug.cubicmetre.protocol.ProtocolVersion

trait Packet[T] {

  type Version
  val version: ProtocolVersion[Version]

  val codec: Codec[T]

  val id: Int

}

object Packet {

  type Aux[T, V] = Packet[T] { type Version = V }

  type `1.15.2`[T] = Aux[T, ProtocolVersion.`1.15.2`]

}
