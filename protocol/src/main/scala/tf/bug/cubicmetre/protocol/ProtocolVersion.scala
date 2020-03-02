package tf.bug.cubicmetre.protocol

import cats.evidence._
import polymorphic.{Exists, Instance}
import polymorphic.syntax.all._
import scodec.Codec
import singleton.ops._
import tf.bug.cubicmetre.protocol.ConnectionState.{Handshaking, Login, Play, Status}
import tf.bug.cubicmetre.protocol.implicits.varIntCodec
import tf.bug.cubicmetre.protocol.packet.Packet
import tf.bug.cubicmetre.protocol.packet.v578

sealed trait ProtocolVersion[Code] {
  type Name

  val codeIsSingletonEv: Code <~< XInt
  val nameIsSingletonEv: Name <~< XString

  val code: Int
  val name: String

  val packets: ConnectionState => Vector[Exists[Packet]]

  final val codecs: ConnectionState => Codec[Instance[Packet]] = { cs =>
    val packetVectors = packets(cs)
    scodec.codecs.variableSizeBytes(varIntCodec.xmapc[Int](identity)(varInt), scodec.codecs.choice(packetVectors.map { packet =>
      val packetInst: Packet[packet.T] = packet.value
      packetInst.codec.xmapc(Instance[Packet](_)(packetInst))(_.first.asInstanceOf[packet.T])
    }: _*))
  }
}

object ProtocolVersion {

  type Aux[C <: XInt, N <: XString] = ProtocolVersion[C] { type Name = N }

  def apply[C <: XInt, N <: XString](
    packet: ConnectionState => Vector[Exists[Packet]]
  )(implicit sc: SafeInt[C], sn: SafeString[N]): Aux[C, N] = new ProtocolVersion[C] {
    override type Name = N

    override val codeIsSingletonEv: C <~< XInt = implicitly
    override val nameIsSingletonEv: N <~< XString = implicitly

    override val code: Int = sc.value
    override val name: String = sn.value
    override val packets: ConnectionState => Vector[Exists[Packet]] = packet
  }

  val `1.15.2`: Aux[578, "1.15.2"] = ProtocolVersion[578, "1.15.2"] {
    case Handshaking => v578.`1.15.2`.handshakePackets
    case Play        => v578.`1.15.2`.playPackets
    case Status      => v578.`1.15.2`.statusPackets
    case Login       => v578.`1.15.2`.loginPackets
  }

  val map: Map[Int, Exists[ProtocolVersion]] = Map(
    578 -> Exists[ProtocolVersion, 578](`1.15.2`)
  )

}
