package tf.bug.cubicmetre

import spire.math.UByte

case class Chunk(x: Int, y: UByte, z: Int) {

  def region: Region = Region(x / 256, z / 256)

  def offset: ChunkOffset = ChunkOffset(UByte(((x % 256) + 256) % 256), y, UByte(((z % 256) + 256) % 256))

  def apply(blockOffset: BlockOffset): Block =
    Block(x * 16L + blockOffset.x.toLong, y * UByte(16) + blockOffset.y, z * 16L + blockOffset.z.toLong)

}

case class ChunkOffset(x: UByte, y: UByte, z: UByte)
