package tf.bug.cubicmetre.protocol

import singleton.ops._

sealed trait ProtocolVersion[Code <: XInt] {
  type Name <: XString
  val code: Int
  val name: String
}

object ProtocolVersion {

  private def apply[C <: XInt, N <: XString](implicit evC: SafeInt[C], evN: SafeString[N]): Aux[C, N] = new ProtocolVersion[C] {
    override type Name = N
    override val code: Int = evC
    override val name: String = evN
  }

  type Aux[C <: XInt, N <: XString] = ProtocolVersion[C] { type Name = N }

  trait Instances {

    implicit val oneFifteenTwoProtocolVersion: ProtocolVersion.Aux[578, "1.15.2"] = ProtocolVersion[578, "1.15.2"]
    implicit val oneSevenTwoProtocolVersion: ProtocolVersion.Aux[4, "1.7.2"] = ProtocolVersion[4, "1.7.2"]
    implicit val thirteenthYearFortyFirstWeekSnapshotBProtocolVersion: ProtocolVersion.Aux[0, "13w41b"] = ProtocolVersion[0, "13w41b"]

  }

  sealed trait In[L <: XInt, H <: XInt] {

    type V <: XInt
    val value: ProtocolVersion[V]

    val lowerValue: ProtocolVersion[L]
    val upperValue: ProtocolVersion[H]
    val lowerBound: Require[V >= L]
    val upperBound: Require[V <= H]

  }

  object In {
    def apply[V0 <: XInt, L <: XInt, H <: XInt](valueZ: ProtocolVersion[V0])(implicit lowerVersionEv: ProtocolVersion[L], upperVersionEv: ProtocolVersion[H], lowerBoundEv: Require[V0 >= L], upperBoundEv: Require[V0 <= H]): In[L, H] = new In[L, H] {
      override type V = V0
      override val value: ProtocolVersion[V0] = valueZ

      override val lowerValue: ProtocolVersion[L] = lowerVersionEv
      override val upperValue: ProtocolVersion[H] = upperVersionEv
      override val lowerBound: Require[V0 >= L] = lowerBoundEv
      override val upperBound: Require[V0 <= H] = upperBoundEv
    }
  }

}
