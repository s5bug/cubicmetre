package tf.bug.cubicmetre.bungee

import java.net.InetSocketAddress

import cats.data._
import cats.effect._
import cats.implicits._
import fs2.io.tcp.{Socket, SocketGroup}
import fs2.{io => _, _}
import polymorphic.{Exists, Instance}
import polymorphic.syntax.all._
import scodec.Codec
import scodec.bits.ByteVector
import scodec.stream.{StreamDecoder, StreamEncoder}
import tf.bug.cubicmetre.protocol.VarInt
import tf.bug.cubicmetre.protocol.chat.{Color, Constant, Keybind}
import tf.bug.cubicmetre.protocol.implicits._
import tf.bug.cubicmetre.protocol.packets.Packet
import tf.bug.cubicmetre.protocol.packets.client.{Handshake, Ping, Request}
import tf.bug.cubicmetre.protocol.packets.server._

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
        .through(StreamDecoder.many(packets).toPipeByte)
        .evalTap(b => Sync[F].delay(println(b)))
        .flatMap {
          case Instance(Handshake(VarInt(_), _, _, VarInt(_)), _) =>
            Stream.empty
          case Instance(Request(), _) =>
            val response: Response = Response(
              ResponseJson(
                ResponseVersion(
                  "1.15.2",
                  578
                ),
                ResponsePlayers(
                  100,
                  0,
                  Vector.empty
                ),
                Constant(
                  "Joe mama! ",
                  color = Color.Gold.some,
                  extra = NonEmptyVector.fromVector(
                    Vector(
                      Constant(
                        "Press ",
                        color = Color.Blue.some
                      ),
                      Keybind(
                        "key.forward",
                        color = Color.Green.some,
                        bold = true.some
                      ),
                      Constant(
                        " to move forward.",
                        color = Color.Blue.some
                      )
                    )
                  )
                ),
                None
              )
            )
            val encode: Codec[Response] = scodec.codecs
              .variableSizeBytes(varintCodec.xmap(_.value, VarInt),
                                 scodec.codecs.constant(ByteVector.fromByte(0)) ~> nettyRewriteResponsePacket.at[578].codec)
            Stream.emit(response).through(StreamEncoder.many(encode).toPipeByte)
          case Instance(Ping(nonce), _) =>
            val pong: Pong = Pong(nonce)
            val encode: Codec[Pong] = scodec.codecs
              .variableSizeBytes(varintCodec.xmap(_.value, VarInt),
                                 scodec.codecs.constant(ByteVector.fromByte(1)) ~> nettyRewritePongPacket.at[578].codec)
            Stream.emit(pong).through(StreamEncoder.many(encode).toPipeByte)
        }
        .through(c.writes(Some(1.seconds)))
    }

  val packets: Codec[Instance[Packet[*, 578]]] = {
    val packets: Vector[Exists[Packet[*, 578]]] = Vector(
      Exists[Packet[*, 578], Handshake](nettyRewriteHandshakePacket.at[578]),
      Exists[Packet[*, 578], Request](nettyRewriteRequestPacket.at[578]),
      Exists[Packet[*, 578], Ping](nettyRewritePingPacket.at[578])
    )
    scodec.codecs.choice(
      packets.map { ev: Exists[Packet[*, 578]] =>
        val codec: Codec[ev.T] = ev.value.codec
        val instCodec: Codec[Instance[Packet[*, 578]]] =
          codec.xmapc(Instance.capture[Packet[*, 578], ev.T](_)(ev.value))(_.first.asInstanceOf)
        val packetId = varintCodec.encode(VarInt(ev.value.id)).require
        val packetWithHead = scodec.codecs.constant(packetId) ~> instCodec
        scodec.codecs
          .variableSizeBytes(varintCodec.xmap(_.value, VarInt), packetWithHead)
          .withContext(s"packet[${ev.value.id.toHexString}]")
      }: _*
    )
  }

}
