package tf.bug.cubicmetre.nbt

import cats.implicits._
import scodec._
import scodec.bits._
import scodec.interop.cats._
import scodec.codecs._

sealed trait NBT

case class NBTByte(value: Byte) extends NBT

case class NBTShort(value: Short) extends NBT

case class NBTInt(value: Int) extends NBT

case class NBTLong(value: Long) extends NBT

case class NBTFloat(value: Float) extends NBT

case class NBTDouble(value: Double) extends NBT

case class NBTBytes(value: Vector[NBTByte]) extends NBT

case class NBTString(value: String) extends NBT

case class NBTList[+T <: NBT](value: Vector[T]) extends NBT

case class NBTCompound(value: Map[NBTString, NBT]) extends NBT

case class NBTInts(value: Vector[NBTInt]) extends NBT

case class NBTLongs(value: Vector[NBTLong]) extends NBT

object NBT {

  def consumeUntil[A, B](consume: Codec[A], until: Codec[B]): Codec[(Vector[A], B)] = {
    val rightConsume = consume.exmap[Either[B, A]](v => Attempt.Successful(Right(v)), e => Attempt.fromEither(e.leftMap(_ => Err("got left in rightConsume instead of right"))))
    val leftUntil = until.exmap[Either[B, A]](v => Attempt.Successful(Left(v)), e => Attempt.fromEither(e.swap.leftMap(_ => Err("got right in leftUntil instead of left"))))
    val eitherConsumeOrUntil = choice(leftUntil, rightConsume).asDecoder
    def recursiveDec(initial: BitVector, step: Vector[A]): Attempt[DecodeResult[(Vector[A], B)]] =
      eitherConsumeOrUntil.decode(initial).flatMap {
        case DecodeResult(Left(done), remainder)      => Attempt.Successful(DecodeResult((step, done), remainder))
        case DecodeResult(Right(continue), remainder) => recursiveDec(remainder, step :+ continue)
      }
    val goDecode: Decoder[(Vector[A], B)] = Decoder.apply { bitVec =>
      recursiveDec(bitVec, Vector.empty)
    }
    val goEncode: Encoder[(Vector[A], B)] = Encoder.apply { tup =>
      val (v: Vector[A], t) = tup
      v.traverse(consume.encode).map(_.combineAll).flatMap(vbv => until.encode(t).map(vbv ++ _))
    }
    Codec(goEncode, goDecode)
  }

//  val nbtCodec: Codec[NBTCompound] =
//    consumeUntil(byte.consume(b => nbtStringCodec ~ nbtTypes(b))(n => nbtType(n._2)), constant(ByteVector.fromByte(0)))
//      .xmap[Vector[(NBTString, NBT)]]({ case (a, ()) => a }, a => (a, ()))
//      .xmap(v => NBTCompound(v.toMap), _.value.toVector)
  // TODO figure out a good way to Codec[NBTCompound]

  val snbtCodec: Codec[NBTCompound] = Codec[NBTCompound]((_: NBTCompound) => Attempt.failure(Err("snbtCodec not implemented")), (_: BitVector) => Attempt.failure(Err("snbtCodec not implemented")))

}
