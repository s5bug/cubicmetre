package tf.bug.cubicmetre

/** Not like a normal Minecraft chunk. Holds 256x256 chunks, not 32x32 chunks. */
case class Region(x: Int, z: Int) {

  def apply(chunkOffset: ChunkOffset): Chunk =
    Chunk(x * 256 + chunkOffset.x.toInt, chunkOffset.y, z * 256 + chunkOffset.z.toInt)

}
