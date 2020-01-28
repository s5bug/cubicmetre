package tf.bug.cubicmetre.protocol.packets

import tf.bug.cubicmetre.protocol.packets.client._
import tf.bug.cubicmetre.protocol.packets.server._

trait Instances
    extends Handshake.Instances
    with Response.Instances
    with Ping.Instances
    with Request.Instances
    with Pong.Instances
