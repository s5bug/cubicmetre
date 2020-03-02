package tf.bug.cubicmetre.protocol.packet.all

import spire.math.UShort
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.{ConnectionState, ProtocolCodec, VarInt}

case class Handshake(
                      protocolVersion: VarInt,
                      serverAddress: String,
                      serverPort: UShort,
                      nextState: ConnectionState
)

object Handshake {

  val protocolCodec: ProtocolCodec[Handshake] = ProtocolCodec.composite[Handshake]()

}
