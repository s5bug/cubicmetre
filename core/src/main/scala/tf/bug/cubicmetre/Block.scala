package tf.bug.cubicmetre

import spire.math.UByte
import squants.space.LengthConversions._

case class Block(x: Long, y: UByte, z: Long) {

  def chunk: Chunk = Chunk((x / 16L).toInt, y / UByte(16), (z / 16L).toInt)

  def offset: BlockOffset =
    BlockOffset(UByte((((x % 16) + 16) % 16).toInt), y % UByte(16), UByte((((z % 16) + 16) % 16).toInt))

  def center: Position = Position((x.toDouble + 0.5d).metres, (y.toDouble + 0.5d).metres, (z.toDouble + 0.5d).metres)

}

case class BlockOffset(x: UByte, y: UByte, z: UByte)
