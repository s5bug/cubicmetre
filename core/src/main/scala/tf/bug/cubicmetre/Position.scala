package tf.bug.cubicmetre

import squants.space.{Length, Meters}

case class Position(x: Length, y: Length, z: Length) {

  def xm: Double = x to Meters
  def ym: Double = y to Meters
  def zm: Double = z to Meters

}
