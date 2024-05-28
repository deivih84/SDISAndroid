import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintWriter
import java.net.Socket
import java.net.ServerSocket


private const val puertoServer = 12346
private lateinit var socketServer: Socket
private lateinit var outServer: PrintWriter
private lateinit var inServer: BufferedReader

private const val puertoCliente = 12345
private lateinit var socketCliente: Socket
private lateinit var outputCliente: OutputStream
private lateinit var inputCliente: InputStream

fun main() { // Recuerden primero lanzar el nodo java, luego a mí y después al cliente android

    ServerSocket(puertoCliente).use { serverSocketCliente ->

        conexionServidor()
        println("Servidor conectado.")

        conexionCliente(serverSocketCliente)
        println("Cliente conectado: ${socketCliente.inetAddress.hostAddress}\n${socketCliente.localAddress}")


        var mensaje: String
        while (true) {
            // Recibir mensaje del cliente
            mensaje = recibirMensajeCliente()

            // Reenviar al servidor
            enviarMensajeServidor(mensaje)

            // Recibir mensaje del servidor
            mensaje = recibirMensajeServidor()

            // Reenviar de vuelta al cliente 😎
            enviarMensajeCliente(mensaje)

            // Repetir hasta que la muerte los separe (Literalmente)
        }
    }
}


/**
 * Todas las funciones bien preciosas, definidas por separado,
 * con un nombre descriptivo y no más de 4 líneas de código.🥰
 *
 */

/**
 * Espera una conexión entrante desde un cliente y
 * establece los flujos de entrada y salida para la comunicación.
 *
 * @param serverSocketCliente El `ServerSocket` que está esperando la conexión de un cliente.
 */
fun conexionCliente(serverSocketCliente: ServerSocket) {
    println("Esperando conexión en ${serverSocketCliente.inetAddress}")
    // Conectarse al cliente android
    socketCliente = serverSocketCliente.accept()
    inputCliente = socketCliente.getInputStream()
    outputCliente = socketCliente.getOutputStream()
}

/**
 * Conecta a un servidor en el host local en el puerto especificado
 * y establece los flujos de entrada y salida para la comunicación.
 *
 */
fun conexionServidor() {
    socketServer = Socket("localhost", puertoServer)
    outServer = PrintWriter(socketServer.getOutputStream(), true)
    inServer = BufferedReader(InputStreamReader(socketServer.getInputStream()))
}

/**
 * Envía un mensaje al servidor a través del flujo de salida.
 *
 * @param mensaje El mensaje a enviar al servidor.
 */
fun enviarMensajeServidor(mensaje: String) {
    outServer.println(mensaje)
}

/**
 * Recibe un mensaje del servidor a través del flujo de entrada.
 *
 * @return El mensaje recibido del servidor.
 */
fun recibirMensajeServidor(): String {
    val respuesta = inServer.readLine()
    return respuesta
}

/**
 * Envía un mensaje al cliente a través del flujo de salida.
 *
 * @param mensaje El mensaje a enviar al cliente.
 */
fun enviarMensajeCliente(mensaje: String) {
    outputCliente.write(mensaje.toByteArray())
    outputCliente.flush()
}

/**
 * Recibe un mensaje del cliente a través del flujo de entrada.
 *
 * @return El mensaje recibido del cliente.
 */
fun recibirMensajeCliente(): String {
    val mensaje = ByteArray(1024)
    val cantidadLeida = inputCliente.read(mensaje)
    return String(mensaje, 0, cantidadLeida)
}
