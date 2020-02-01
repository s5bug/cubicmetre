package tf.bug.cubicmetre.protocol.packets

import scodec.Codec
import singleton.ops._
import tf.bug.cubicmetre.protocol.ProtocolVersion

case class Packet[T, V <: XInt](id: Int, codec: Codec[T])(implicit versionEv: ProtocolVersion[V]) {

  val version: ProtocolVersion[V] = versionEv

}

sealed trait PacketRange[T, L <: XInt, H <: XInt] {

  val lowestVersion: ProtocolVersion[L]
  val highestVersion: ProtocolVersion[H]

  def at[V <: XInt](implicit version: ProtocolVersion[V], inBoundsLower: Require[V >= L], inBoundsUpper: Require[V <= H]): Packet[T, V]

}

object PacketRange {

  type At[T, V <: XInt] = PacketRange[T, V, V]

  def apply[T, L <: XInt, H <: XInt](id: ProtocolVersion.In[L, H] => Int, codec: ProtocolVersion.In[L, H] => Codec[T])(implicit versionL: ProtocolVersion[L], versionH: ProtocolVersion[H]): PacketRange[T, L, H] =
    new PacketRange[T, L, H] {
      override val lowestVersion: ProtocolVersion[L] = versionL
      override val highestVersion: ProtocolVersion[H] = versionH

      override def at[V <: XInt](implicit version: ProtocolVersion[V], inBoundsLower: Require[V >= L], inBoundsUpper: Require[V <= H]): Packet[T, V] =
        Packet[T, V](id(ProtocolVersion.In(version)), codec(ProtocolVersion.In(version)))(version)
    }

}
