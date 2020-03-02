package tf.bug.cubicmetre.protocol

import tf.bug.cubicmetre.protocol.chat.{Color, HoverEvent, Text}
import tf.bug.cubicmetre.protocol.packet.Packet

object implicits
    extends ProtocolCodec.Instances
    with ConnectionState.Instances
    with Color.Instances
    with Text.Instances
    with HoverEvent.Instances
    with Packet.Instances
