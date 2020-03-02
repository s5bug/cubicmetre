package tf.bug.cubicmetre.protocol

import tf.bug.cubicmetre.protocol.implicits.varIntCodec

sealed trait ConnectionState extends Product with Serializable

object ConnectionState {

  final case object Handshaking extends ConnectionState
  final case object Play extends ConnectionState
  final case object Status extends ConnectionState
  final case object Login extends ConnectionState

  trait Instances {

    implicit val connectionStateProtocolCodec: ProtocolCodec[ConnectionState] =
      ProtocolCodecRaw(
        varIntCodec
          .xmapc[Int](identity)(varInt)
          .xmapc {
            case 0 => Handshaking
            case 1 => Status
            case 2 => Login
            case 3 => Play
          } {
            case Handshaking => 0
            case Status      => 1
            case Login       => 2
            case Play        => 3
          }
      )

  }

}
