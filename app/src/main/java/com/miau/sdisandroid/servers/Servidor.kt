import java.io.*
import java.net.ServerSocket
import java.net.Socket

private const val puertoServer = 12346
private lateinit var socketCliente: Socket
private lateinit var outServer: PrintWriter
private lateinit var inServer: BufferedReader

fun main() {

    ServerSocket(puertoServer).use { serverSocket ->
        println("Servidor esperando conexiones...")
        conexionClient(serverSocket)
        println("Cliente conectado")

        while (true) {

            var mensaje = recibirMensajeClient()
            println("Mensaje recibido del cliente: $mensaje\n")

            println("Mensaje a mandar:")
            mensaje = readln()
            enviarMensajeClient(mensaje)
        }
    }
}

fun conexionClient(serverSocket: ServerSocket) {
    socketCliente = serverSocket.accept()
    outServer = PrintWriter(socketCliente.getOutputStream(), true)
    inServer = BufferedReader(InputStreamReader(socketCliente.getInputStream()))
}

fun enviarMensajeClient(mensaje: String) {
    outServer.println(mensaje)
}
fun recibirMensajeClient(): String {
    val respuesta = inServer.readLine()
    return respuesta
}

