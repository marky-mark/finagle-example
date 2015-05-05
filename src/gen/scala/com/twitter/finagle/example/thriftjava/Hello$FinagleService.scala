/**
 * Generated by Scrooge
 *   version: ?
 *   rev: ?
 *   built at: ?
 */
package com.twitter.finagle.example.thriftjava

import com.twitter.finagle.{Service => FinagleService}
import com.twitter.scrooge.ThriftStruct
import com.twitter.util.Future
import java.nio.ByteBuffer
import java.util.Arrays
import org.apache.thrift.protocol._
import org.apache.thrift.TApplicationException
import org.apache.thrift.transport.{TMemoryBuffer, TMemoryInputTransport}
import scala.collection.mutable.{
  ArrayBuffer => mutable$ArrayBuffer, HashMap => mutable$HashMap}
import scala.collection.{Map, Set}


@javax.annotation.Generated(value = Array("com.twitter.scrooge.Compiler"), date = "2015-05-05T11:57:31.947+0100")
class Hello$FinagleService(
  iface: Hello[Future],
  protocolFactory: TProtocolFactory
) extends FinagleService[Array[Byte], Array[Byte]] {
  import Hello._

  protected val functionMap = new mutable$HashMap[String, (TProtocol, Int) => Future[Array[Byte]]]()

  protected def addFunction(name: String, f: (TProtocol, Int) => Future[Array[Byte]]) {
    functionMap(name) = f
  }

  protected def exception(name: String, seqid: Int, code: Int, message: String): Future[Array[Byte]] = {
    try {
      val x = new TApplicationException(code, message)
      val memoryBuffer = new TMemoryBuffer(512)
      val oprot = protocolFactory.getProtocol(memoryBuffer)

      oprot.writeMessageBegin(new TMessage(name, TMessageType.EXCEPTION, seqid))
      x.write(oprot)
      oprot.writeMessageEnd()
      oprot.getTransport().flush()
      Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  protected def reply(name: String, seqid: Int, result: ThriftStruct): Future[Array[Byte]] = {
    try {
      val memoryBuffer = new TMemoryBuffer(512)
      val oprot = protocolFactory.getProtocol(memoryBuffer)

      oprot.writeMessageBegin(new TMessage(name, TMessageType.REPLY, seqid))
      result.write(oprot)
      oprot.writeMessageEnd()

      Future.value(Arrays.copyOfRange(memoryBuffer.getArray(), 0, memoryBuffer.length()))
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  final def apply(request: Array[Byte]): Future[Array[Byte]] = {
    val inputTransport = new TMemoryInputTransport(request)
    val iprot = protocolFactory.getProtocol(inputTransport)

    try {
      val msg = iprot.readMessageBegin()
      functionMap.get(msg.name) map { _.apply(iprot, msg.seqid) } getOrElse {
        TProtocolUtil.skip(iprot, TType.STRUCT)
        exception(msg.name, msg.seqid, TApplicationException.UNKNOWN_METHOD,
          "Invalid method name: '" + msg.name + "'")
      }
    } catch {
      case e: Exception => Future.exception(e)
    }
  }

  // ---- end boilerplate.

  addFunction("hi", { (iprot: TProtocol, seqid: Int) =>
    try {
      val args = hi$args.decode(iprot)
      iprot.readMessageEnd()
      (try {
        iface.hi()
      } catch {
        case e: Exception => Future.exception(e)
      }) flatMap { value: String =>
        reply("hi", seqid, hi$result(success = Some(value)))
      } rescue {
        case e => Future.exception(e)
      }
    } catch {
      case e: TProtocolException => {
        iprot.readMessageEnd()
        exception("hi", seqid, TApplicationException.PROTOCOL_ERROR, e.getMessage)
      }
      case e: Exception => Future.exception(e)
    }
  })
}