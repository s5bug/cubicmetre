package tf.bug.cubicmetre.protocol.chat

import io.circe._
import io.circe.syntax._
import tf.bug.cubicmetre.nbt.NBT
import tf.bug.cubicmetre.protocol.item.Item
import tf.bug.cubicmetre.protocol.entity.Entity

sealed trait HoverEvent {

  val action: String
  val value: String

}

object HoverEvent {

  case class ShowText(text: Text) extends HoverEvent {
    override val value: String = Printer.noSpaces
      .copy(dropNullValues = true)
      .print(tf.bug.cubicmetre.protocol.implicits.minecraftTextEncoder.apply(text))
    override val action: String = "show_text"
  }

  case class ShowItem(item: Item) extends HoverEvent {
    override val value: String = {
      val Right(decoded) = NBT.snbtCodec.encode(item.chat).require.decodeUtf8
      decoded
    }
    override val action: String = "show_item"
  }

  case class ShowEntity(entity: Entity) extends HoverEvent {
    override val value: String = {
      val Right(decoded) = NBT.snbtCodec.encode(entity.chat).require.decodeUtf8
      decoded
    }
    override val action: String = "show_entity"
  }

  trait Instances {

    import tf.bug.cubicmetre.protocol.implicits.minecraftTextEncoder

    implicit val hoverEventEncoder: Encoder[HoverEvent] = Encoder.instance { // FIXME the encoders in ShowItem and ShowEntity have a chance to fail
      case t @ ShowText(text) => Json.obj("action" -> t.action.asJson, "value" -> text.asJson)
      case t                  => Json.obj("action" -> t.action.asJson, "value" -> t.value.asJson)
    }

  }

}
