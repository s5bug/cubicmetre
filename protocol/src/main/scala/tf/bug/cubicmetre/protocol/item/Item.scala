package tf.bug.cubicmetre.protocol.item

import tf.bug.cubicmetre.nbt.NBTCompound

case class Item() {

  val chat: NBTCompound = NBTCompound(Map()) // FIXME

}
