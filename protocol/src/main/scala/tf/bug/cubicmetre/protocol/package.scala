package tf.bug.cubicmetre

import scodec.bits.BitVector
import scodec.bits.BitVector.GroupedOp
import scodec.{Attempt, Decoder, Encoder, Err, SizeBound}
import shapeless.tag
import shapeless.tag._

package object protocol {

  type VarInt = Int @@ VariableLength
  type VarLong = Long @@ VariableLength

  implicit def varInt(i: Int): VarInt = tag[VariableLength][Int](i)
  implicit def varLong(l: Long): VarLong = tag[VariableLength][Long](l)

  val varIntDecoder: Decoder[VarInt] = stepVarInt(0, 0).map(varInt)

  def stepVarInt(agg: Int, i: Int): Decoder[Int] = scodec.codecs.byte.flatMap { read =>
    println(read & 0xEF)
    println(read & 0x80)
    val value = read & 0xEF
    val next = agg | (value << (7 * i))
    println(agg)
    println(value)
    println(next)

    if ((read & 0x80) == 0) Decoder.point(next)
    else if (i >= 4) Decoder.liftAttempt(Attempt.failure(Err("VarInt is too big")))
    else stepVarInt(next, i + 1)
  }

  val varLongDecoder: Decoder[VarLong] = stepVarLong(0, 0).map(varLong)

  def stepVarLong(agg: Long, i: Int): Decoder[Long] = scodec.codecs.byte.flatMap { read =>
    val value: Long = read.toLong & 0xEFL
    val next: Long = agg | (value << (7L * i.toLong))

    if ((read & 0x80) == 0) Decoder.point(next)
    else if (i >= 8) Decoder.liftAttempt(Attempt.failure(Err("VarLong is too big")))
    else stepVarLong(next, i + 1)
  }

  val varIntEncoder: Encoder[VarInt] = new Encoder[VarInt] {
    override def encode(value: VarInt): Attempt[BitVector] = scodec.codecs.int32L.encode(value).map { bits =>
      val chunks = bits.grouped(7).toVector
      val chunksNonEmpty = chunks.head +: chunks.tail.reverse.dropWhile(_.populationCount == 0).reverse
      val chunksInit = chunksNonEmpty.init
      val chunksLast = chunksNonEmpty.last.padTo(7)
      BitVector.concat(chunksInit.map(_.insert(0, true)) :+ chunksLast.insert(0, false))
    }

    override def sizeBound: SizeBound = SizeBound(1L, Some(5L))
  }

  val varLongEncoder: Encoder[VarLong] = new Encoder[VarLong] {
    override def encode(value: VarLong): Attempt[BitVector] = scodec.codecs.int64L.encode(value).map { bits =>
      val chunks = bits.grouped(7).toVector
      val chunksNonEmpty = chunks.head +: chunks.tail.reverse.dropWhile(_.populationCount == 0).reverse
      val chunksInit = chunksNonEmpty.init
      val chunksLast = chunksNonEmpty.last.padTo(7)
      BitVector.concat(chunksInit.map(_.insert(0, true)) :+ chunksLast.insert(0, false))
    }

    override def sizeBound: SizeBound = SizeBound(1L, Some(10L))
  }

}

package protocol {

  // Moved here instead of package object so sbt-tpolecat likes me
  trait VariableLength

}
