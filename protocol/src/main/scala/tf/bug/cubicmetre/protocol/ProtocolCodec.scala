package tf.bug.cubicmetre.protocol

import scodec.Codec
import scodec.codecs._
import shapeless.ops.hlist.{LiftAll, Mapper}
import shapeless.{Generic, HList, Poly1}
import spire.math.{UByte, UShort}

sealed trait ProtocolCodec[A] {

  val scodecCodec: Codec[A]

}

final case class ProtocolCodecRaw[A](scodecCodec: Codec[A]) extends ProtocolCodec[A]

final case class ProtocolCodecComposite[A, Repr <: HList, AllProtocolCodec <: HList, AllCodec <: HList]()(
  implicit ev: Generic.Aux[A, Repr],
  evAllCodec: LiftAll.Aux[ProtocolCodec, Repr, AllProtocolCodec],
  grabInnerCodecs: Mapper.Aux[ProtocolCodec.PolyScodecCodec.type, AllProtocolCodec, AllCodec],
  toHListCodec: ToHListCodec.Aux[AllCodec, Repr]
) extends ProtocolCodec[A] {
  override val scodecCodec: Codec[A] =
    scodec.codecs.hlist(grabInnerCodecs(evAllCodec.instances)).xmapc(ev.from)(ev.to)
}

object ProtocolCodec {

  object PolyScodecCodec extends Poly1 {
    implicit def caseProtocolCodec[A]: Case.Aux[ProtocolCodec[A], Codec[A]] = at(_.scodecCodec)
  }

  def composite[A]: PartiallyAppliedComposite[A] = new PartiallyAppliedComposite[A]

  class PartiallyAppliedComposite[A] {

    def apply[Repr <: HList, AllProtocolCodec <: HList, AllCodec <: HList]()(
      implicit ev: Generic.Aux[A, Repr],
      evAllCodec: LiftAll.Aux[ProtocolCodec, Repr, AllProtocolCodec],
      grabInnerCodecs: Mapper.Aux[PolyScodecCodec.type, AllProtocolCodec, AllCodec],
      toHListCodec: ToHListCodec.Aux[AllCodec, Repr]
    ): ProtocolCodecComposite[A, Repr, AllProtocolCodec, AllCodec] =
      ProtocolCodecComposite[A, Repr, AllProtocolCodec, AllCodec]()(ev, evAllCodec, grabInnerCodecs, toHListCodec)

  }

  trait Instances {

    implicit val varLongCodec: Codec[VarLong] = Codec(varLongEncoder, varLongDecoder)

    implicit val varIntCodec: Codec[VarInt] = Codec(varIntEncoder, varIntDecoder)

    implicit val booleanProtocolCodec: ProtocolCodec[Boolean] = ProtocolCodecRaw(bool)
    implicit val byteProtocolCodec: ProtocolCodec[Byte] = ProtocolCodecRaw(byte)
    implicit val ubyteProtocolCodec: ProtocolCodec[UByte] = ProtocolCodecRaw(byte.xmapc(UByte(_))(_.signed))
    implicit val shortProtocolCodec: ProtocolCodec[Short] = ProtocolCodecRaw(short16)
    implicit val ushortProtocolCodec: ProtocolCodec[UShort] = ProtocolCodecRaw(short16.xmapc(UShort(_))(_.toShort))
    implicit val intProtocolCodec: ProtocolCodec[Int] = ProtocolCodecRaw(int32)

    implicit val stringProtocolCodec: ProtocolCodec[String] = ProtocolCodecRaw(
      variableSizeBytes(varIntCodec.xmapc[Int](identity)(varInt), limitedSizeBytes(32767, utf8))
    )

    implicit val varIntProtocolCodec: ProtocolCodec[VarInt] = ProtocolCodecRaw(varIntCodec)
    implicit val varLongProtocolCodec: ProtocolCodec[VarLong] = ProtocolCodecRaw(varLongCodec)

  }

}
