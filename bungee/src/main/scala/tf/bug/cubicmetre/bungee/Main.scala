package tf.bug.cubicmetre.bungee

import java.net.InetSocketAddress

import cats.effect._
import cats.implicits._
import fs2.io.tcp.{Socket, SocketGroup}
import fs2.{io => _, _}
import polymorphic.Instance
import scodec.stream.StreamDecoder
import tf.bug.cubicmetre.protocol.packet.all.Handshake
import tf.bug.cubicmetre.protocol.{ConnectionState, ProtocolVersion}

import scala.concurrent.duration._

object Main extends IOApp {

  override def run(args: List[String]): IO[ExitCode] =
    program[IO].compile.drain.as(ExitCode.Success)

  def program[F[_]: ConcurrentEffect: ContextShift]: Stream[F, Unit] =
    for {
      blocker <- Stream.resource(Blocker[F])
      socketGroup <- Stream.resource(SocketGroup[F](blocker))
      server = socketGroup.server(new InetSocketAddress(25565))
      handle <- server.map(handleClient[F]).parJoin(256)
    } yield handle

  def handleClient[F[_]: Concurrent: RaiseThrowable](client: Resource[F, Socket[F]]): Stream[F, Unit] =
    Stream.resource(client).flatMap { c =>
      c.reads(8192)
        .evalTap(b => Sync[F].delay(println((b.toInt & 0xff).toHexString)))
        .through(StreamDecoder.many(ProtocolVersion.`1.15.2`.codecs(ConnectionState.Handshaking)).toPipeByte)
        .evalTap(b => Sync[F].delay(println(b)))
        .flatMap {
          case Instance(Handshake(pv, _, _, _), _) =>
            Stream.eval(Sync[F].delay(println(pv))).map(_ => 1.toByte)
        }
        .through(c.writes(Some(1.seconds)))
    }

}
