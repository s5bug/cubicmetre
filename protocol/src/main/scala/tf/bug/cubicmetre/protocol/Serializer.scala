package tf.bug.cubicmetre.protocol

import scodec.Codec

trait Serializer[T, P] {

  val codec: Codec[T]

}
