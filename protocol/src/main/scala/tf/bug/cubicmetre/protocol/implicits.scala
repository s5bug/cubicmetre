package tf.bug.cubicmetre.protocol

import tf.bug.cubicmetre.protocol.chat.{Color, HoverEvent, Text}

object implicits
    extends Datatypes.Instances
    with ProtocolVersion.Instances
    with packets.Instances
    with Color.Instances
    with Text.Instances
    with HoverEvent.Instances
