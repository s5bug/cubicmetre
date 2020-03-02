package tf.bug.cubicmetre.protocol.packet.all

import tf.bug.cubicmetre.protocol.ProtocolCodec
import tf.bug.cubicmetre.protocol.packet.Packet

trait Instances {

  implicit val handshakePacket: Packet[Handshake] = new Packet[Handshake] {
    override val id: Int = 0
    override val codecT: ProtocolCodec[Handshake] = Handshake.protocolCodec
  }

}
