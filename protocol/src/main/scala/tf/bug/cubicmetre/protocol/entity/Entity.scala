package tf.bug.cubicmetre.protocol.entity

import tf.bug.cubicmetre.nbt.NBTCompound

case class Entity() {

  val chat: NBTCompound = NBTCompound(Map()) // FIXME

}
