package tf.bug.cubicmetre.node

import cats.effect._
import cats.implicits._
import fs2._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    program[IO].compile.drain.as(ExitCode.Success)

  def program[F[_]]: Stream[F, Unit] =
    Stream.empty

}
