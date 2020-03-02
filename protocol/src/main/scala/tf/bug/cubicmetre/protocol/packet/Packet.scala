package tf.bug.cubicmetre.protocol.packet

import scodec.Codec
import tf.bug.cubicmetre.protocol.ProtocolCodec

trait Packet[A] {

  val id: Int
  protected val codecT: ProtocolCodec[A]

  final lazy val codec: Codec[A] =
    codecT.scodecCodec // FIXME id ~>

}

object Packet {

  trait Instances extends all.Instances

}
