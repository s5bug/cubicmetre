package tf.bug.cubicmetre.protocol

sealed trait ProtocolVersion[T] {
  type Code <: Int
  type Name <: String
  val code: Code
  val name: Name
}

object ProtocolVersion {

  case object `1.15.2`
  type `1.15.2` = `1.15.2`.type

  type AuxC[T, C <: Int] = ProtocolVersion[T] { type Code = C }
  type AuxN[T, N <: String] = ProtocolVersion[T] { type Name = N }
  type Aux[T, C <: Int, N <: String] = ProtocolVersion[T] { type Code = C; type Name = N }

  trait Instances {

    implicit val oneFifteenTwoProtocolVersion: ProtocolVersion.Aux[`1.15.2`, 578, "1.15.2"] =
      new ProtocolVersion[`1.15.2`] {
        override type Code = 578
        override type Name = "1.15.2"
        override val code: Code = 578
        override val name: Name = "1.15.2"
      }

  }

}
