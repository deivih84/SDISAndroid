package moduloBasico

import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

class Connection(val socket: Socket) : AutoCloseable {
    private val out: ObjectOutputStream
    private val `in`: ObjectInputStream

    init {
        out = ObjectOutputStream(socket.getOutputStream())
        out.flush()
        `in` = ObjectInputStream(socket.getInputStream())
    }

    @Throws(Exception::class)
    fun mandarMensaje(obj: Any?) {
        synchronized(out) {
            out.writeObject(obj)
            out.flush()
        }
    }

    @Throws(Exception::class)
    fun readObject(): Any {
        return `in`.readObject()
    }

    @Throws(Exception::class)
    override fun close() {
        `in`.close()
        out.close()
        socket.close()
    }

    override fun toString(): String {
        return "Connection{" +
                "Status=" + (if (socket.isClosed) "Closed" else "Open") +
                ", LocalAddress=" + (socket.getLocalAddress()
            .toString() + ":" + socket.getLocalPort()) +
                ", RemoteAddress=" + (if (socket.isConnected) socket.getRemoteSocketAddress()
            .toString() else "Disconnected") +
                '}'
    }
}