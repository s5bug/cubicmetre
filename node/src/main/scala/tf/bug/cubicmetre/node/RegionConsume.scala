package tf.bug.cubicmetre.node

import cats.Functor
import cats.effect._
import fs2.Pipe
import fs2.kafka._
import tf.bug.cubicmetre.Region

object RegionConsume {

  def consumerSettings[F[_]: Sync](bootstrap: String): ConsumerSettings[F, String, String] = // TODO change from String, String
    ConsumerSettings[F, String, String]
      .withAutoOffsetReset(AutoOffsetReset.Earliest)
      .withBootstrapServers(bootstrap)

  def subscribeToRegion[F[_]: Functor](
    region: Region
  ): Pipe[F, KafkaConsumer[F, String, String], KafkaConsumer[F, String, String]] =
    in => in.evalTap(_.subscribeTo(s"region-${region.x}-${region.z}"))

  def unsubscribeFromRegion[F[_]: Functor](
    region: Region
  ): Pipe[F, KafkaConsumer[F, String, String], KafkaConsumer[F, String, String]] =
    in => in.evalTap(_.unsubscribe) // FIXME

}
