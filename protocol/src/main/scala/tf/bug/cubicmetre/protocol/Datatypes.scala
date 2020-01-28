package tf.bug.cubicmetre.protocol

import scodec._
import scodec.bits.BitVector
import scodec.bits.BitVector.GroupedOp
import scodec.codecs._
import spire.math.{UByte, UShort}

object Datatypes {

  private def gen[T, P: ProtocolVersion](c: Codec[T]): Serializer[T, P] =
    new Serializer[T, P] {
      override val codec: Codec[T] = c
    }

  trait Instances {

    implicit val booleanCodec: Codec[Boolean] = byte.exmap(
      {
        case 0x00 => Attempt.successful(false)
        case 0x01 => Attempt.successful(true)
        case a    => Attempt.failure(Err(s"expected byte value 0 or 1, got $a"))
      }, {
        case false => Attempt.successful(0x00.toByte)
        case true  => Attempt.successful(0x01.toByte)
      }
    )

    implicit val byteCodec: Codec[Byte] = byte

    implicit val ubyteCodec: Codec[UByte] = byte.xmap(UByte(_), _.signed)

    implicit val shortCodec: Codec[Short] = short16

    implicit val ushortCodec: Codec[UShort] = short16.xmap(UShort(_), _.toShort)

    implicit val intCodec: Codec[Int] = int32

    implicit val longCodec: Codec[Long] = int64

    implicit val varintCodec: Codec[VarInt] = Codec(
      new Encoder[VarInt] {
        override def encode(value: VarInt): Attempt[BitVector] = {
          val groups = BitVector.fromInt(value.value).reverse.grouped(7).map(_.reverse).map(_.padLeft(7)).toVector
          val set = groups.head +: groups.tail.takeWhile(_.populationCount != 0)
          val withMSB = set.init.map(_.insert(0, true))
          val last = set.last.insert(0, false)
          Attempt.successful(BitVector.concat(withMSB) ++ last)
        }

        override def sizeBound: SizeBound = SizeBound(1, Option(5))
      },
      new Decoder[VarInt] {
        override def decode(bits: BitVector): Attempt[DecodeResult[VarInt]] = { // FIXME make it error on invalid varints
          val groups = bits.grouped(8).take(5).toVector
          if (groups.nonEmpty) {
            val withMSB = groups.takeWhile(_.head).map(_.tail).reverse
            val last = groups(withMSB.size)
            val whole = last ++ BitVector.concat(withMSB)
            Attempt.successful(DecodeResult(VarInt(whole.toInt()), bits.drop((withMSB.size.toLong + 1L) * 8L)))
          } else Attempt.failure(Err.insufficientBits(8, 0))
        }
      }
    )

    implicit val varlongCodec: Codec[VarLong] = Codec(
      new Encoder[VarLong] {
        override def encode(value: VarLong): Attempt[BitVector] = {
          val groups = BitVector.fromLong(value.value).reverse.grouped(7).map(_.reverse).map(_.padLeft(7)).toVector
          val set = groups.head +: groups.tail.takeWhile(_.populationCount != 0)
          val withMSB = set.init.map(_.insert(0, true))
          val last = set.last.insert(0, false)
          Attempt.successful(BitVector.concat(withMSB) ++ last)
        }

        override def sizeBound: SizeBound = SizeBound(1, Option(5))
      },
      new Decoder[VarLong] {
        override def decode(bits: BitVector): Attempt[DecodeResult[VarLong]] = { // FIXME make it error on invalid varlongs
          val groups = bits.grouped(8).take(10).toVector
          if (groups.nonEmpty) {
            val withMSB = groups.takeWhile(_.head).map(_.tail).reverse
            val last = groups(withMSB.size).tail
            val whole = last ++ BitVector.concat(withMSB)
            Attempt.successful(DecodeResult(VarLong(whole.toLong()), bits.drop((withMSB.size.toLong + 1L) * 8L)))
          } else Attempt.failure(Err.insufficientBits(8, 0))
        }
      }
    )

    implicit val stringCodec: Codec[String] = variableSizeBytes(varintCodec.xmap(_.value, VarInt), utf8)

    implicit def booleanSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Boolean, P] = gen(booleanCodec)

    implicit def byteSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Byte, P] = gen(byteCodec)

    implicit def ubyteSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[UByte, P] = gen(ubyteCodec)

    implicit def shortSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Short, P] = gen(shortCodec)

    implicit def ushortSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[UShort, P] = gen(ushortCodec)

    implicit def intSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Int, P] = gen(intCodec)

    implicit def longSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Long, P] = gen(longCodec)

    implicit def floatSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Float, P] = gen(float)

    implicit def doubleSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[Double, P] = gen(double)

    implicit def varintSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[VarInt, P] = gen(varintCodec)

    implicit def varlongSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[VarLong, P] = gen(varlongCodec)

    implicit def stringSerializer[P](implicit ev: ProtocolVersion[P]): Serializer[String, P] = gen(stringCodec)

  }

}
