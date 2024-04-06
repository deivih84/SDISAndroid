package com.miau.sdisandroid

import com.miau.sdisandroid.common.MensajeProtocolo
import com.miau.sdisandroid.common.Primitiva
import java.net.*
import java.nio.charset.StandardCharsets
import java.util.Scanner
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.stream.Collectors

// La clase NODO abre un servidor y un sirviente a la vez
// Hay un sirviente en cada máquina
class Nodo(// Obtiene nombre del nodo
    val nombre: String, puerto: Int, tokenSeguridad: String) {
    private val tokenSeguridad // Nombre del nodo y tokenseguridad que se usa para comprobar que el nodo que se quiere conectar es valido
            : String

    // Obtiene el puerto del servidor
    private val puerto: Int // Puerto del servidor
    private val serverSocket // Socket del servidor
            : ServerSocket
    private val variables // Variables del nodo
            : VariablesNodo
    private val descendientes //Mapea nombres de nodos hijos con sus conexiones abiertas
            : ConcurrentHashMap<String, Connection>
    private var padre // Conexion hacia el nodo padre
            : Connection?
    private var multicastConnection //Conexion hacia el cliente multicast
            : Connection?
    private val exec = Executors.newCachedThreadPool() // Para usar un hilo cuando se necesite

    // Inicializacion del Primer Nodo
    init {
        serverSocket = ServerSocket(puerto)
        this.puerto = serverSocket.getLocalPort()
        variables = VariablesNodo()
        descendientes = ConcurrentHashMap()
        padre = null
        multicastConnection = null
        this.tokenSeguridad = tokenSeguridad
        inicializarServidor()
        println(welcome()) // Devuelve información del nodo
    }

    /*Crea el hilo servidor, que espera a que haya conexiones entrantes.
     * Cuando se acepta una conexión, se delega a un sirviente.*/
    private fun inicializarServidor() {
        val mainServer = Thread({
            while (!Thread.currentThread().isInterrupted) { //Dentro del hilo servidor
                try {
                    val socketEntrante = serverSocket.accept()
                    val connection =
                        Connection(socketEntrante)
                    sirviente(connection)
                } catch (e: Exception) {
                    e.printStackTrace(System.err)
                }
            }
        }, "Hilo Servidor")
        mainServer.start() // Se lanza el hilo servidor y que haga sus locuras
    }

    val iP: InetAddress
        // Obtiene ip del nodo
        get() = serverSocket.getInetAddress()
    val externalIP: String?
        // Obtiene la ip publica
        get() {
            try {
                Scanner(
                    URL("http://checkip.amazonaws.com").openStream(),
                    "UTF-8"
                ).use { scanner ->  //Devuelve ip publica
                    return scanner.useDelimiter("\\A").next().trim { it <= ' ' }
                }
            } catch (e: Exception) {
                return null
            }
        }
    val numNodos: Int
        /**
         * Getter de cantidad de nodos conectados al padre
         *
         * @return Tamaño del mapa descendientes
         */
        get() = descendientes.size

    // Elimina una conexion existente
    private fun eliminarConexion(connection: Connection?) {
        if (connection == null) return  // No se puede eliminar socket nulo
        try {
            connection.close()
            println("Hasta luego: $connection")
        } catch (_: Exception) {
        }
        if (connection == padre) {
            padre =
                null //Si se muere el padre el hijo sigue funcionando pero debe de saber que no tiene padre
            return
        }
        if (connection == multicastConnection) {
            multicastConnection = null //Eliminar conexion multicast, si existe
            return
        }
        descendientes.entries.removeIf { (_, value): Map.Entry<String, Connection> -> value == connection }
    }

    /*
     * Procesa el mensaje, que mas quieres que haga
     *	Mensaje: mensaje que se ha recibido
     *	SocketEntrante: socket de la maquina que envio el mensaje
     * */
    @Throws(Exception::class)
    private fun procesarMensaje(mensaje: MensajeProtocolo, conexionEntrante: Connection) {
        println(mensaje.toString())
        System.out.flush()
        when (mensaje.primitiva) {
            Primitiva.ADD -> {
                comprobarTokenSeguridad(mensaje, conexionEntrante)
                if (!descendientes.containsKey(mensaje.nombreNodo)) {
                    descendientes[mensaje.nombreNodo] = conexionEntrante //Acaba bien
                } else {
                    conexionEntrante.mandarMensaje(MensajeProtocolo.nothing()) //Acaba mal
                    throw Exception()
                }
                conexionEntrante.mandarMensaje(MensajeProtocolo.addOk()) //Mensaje de confirmacion de que se ha recibido bien el mensaje
            }

            Primitiva.ADD_OK -> {}
            Primitiva.MULTICAST -> {
                comprobarTokenSeguridad(mensaje, conexionEntrante)
                variables.add(mensaje.nombreVariable, mensaje.valor)
                if (multicastConnection != null) multicastConnection!!.close() //Actualiza conexion del cliente multicast
                multicastConnection = conexionEntrante
                for (connection in descendientes.values) connection.mandarMensaje(
                    MensajeProtocolo.transmission(
                        serverSocket.getInetAddress(),
                        mensaje.nombreVariable,
                        mensaje.valor
                    )
                )
                conexionEntrante.mandarMensaje(MensajeProtocolo.multicastOk())
            }

            Primitiva.MULTICAST_OK -> {}
            Primitiva.TRANSMISSION -> {
                variables.add(mensaje.nombreVariable, mensaje.valor)
                for (connection in descendientes.values) connection.mandarMensaje(
                    MensajeProtocolo.transmission(
                        serverSocket.getInetAddress(),
                        mensaje.nombreVariable,
                        mensaje.valor
                    )
                )
                conexionEntrante.mandarMensaje(MensajeProtocolo.transmissionOk())
                if (descendientes.isEmpty()) padre!!.mandarMensaje(MensajeProtocolo.RETURN(nombre + ":" + variables.toString()))
            }

            Primitiva.TRANSMISSION_OK -> {}
            Primitiva.RETURN -> if (multicastConnection != null) multicastConnection!!.mandarMensaje(
                MensajeProtocolo.RETURN(
                    nombre + ":" + mensaje.hashmap
                )
            ) else if (padre != null) padre!!.mandarMensaje(
                MensajeProtocolo.RETURN(
                    nombre + ":" + mensaje.hashmap
                )
            )

            Primitiva.RETURN_END -> {}
            Primitiva.NOTHING -> println("¿¿¿Que intenta usted mensajearme???")
            Primitiva.BAD_TS -> {}
            Primitiva.NOTUNDERSTAND -> {}
            else -> conexionEntrante.mandarMensaje(MensajeProtocolo.notUnderstand())
        }
    }

    /*Valida el token de seguridad recibido por un mensaje en la conexion entrante especificada,
     * si no es válido, lanza una excepción, para que la conexión entrante se elimine en sirviente.*/
    @Throws(Exception::class)
    private fun comprobarTokenSeguridad(mensaje: MensajeProtocolo, conexionEntrante: Connection) {
        if (tokenSeguridad != mensaje.tokenSeguridad) {
            println("¿¿¿Quién es usted???")
            conexionEntrante.mandarMensaje(MensajeProtocolo.badTs())
            throw Exception()
        }
    }

    @Throws(Exception::class)
    private fun procesarString(mensaje: String) { //Cosas de python
        val chunks = mensaje.split(" ".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()
        grafo(chunks[0], chunks[1].toInt(), chunks[2])
    }

    @Throws(Exception::class)
    fun grafo(ip: String, puerto: Int, ruta: String) { //Cosas de python
        val mensajeSaliente = "$ip $puerto"
        var datos = ruta
        if (!descendientes.isEmpty()) datos += "," + descendientes.entries.stream()
            .map { (key): Map.Entry<String, Connection> -> "$ruta.$key" }
            .collect(Collectors.joining(","))
        mandaMensajeString(ip, puerto, datos)
        for ((key, value) in descendientes) {
            value.mandarMensaje("$mensajeSaliente $ruta.$key")
        }
    }

    //Para concetarse con el padre del que se pasan las varibles
    //nombrePadre:ip del padre
    //tokenSeguridad del que se quiere conectar con el padre
    fun conexionPadre(nombrePadre: String?, puertoPadre: Int, tokenSeguridad: String?) {
        eliminarConexion(padre) //Se elimina el padre que había😌 (si es que había)
        try {
            val socket = Socket(
                nombrePadre,
                puertoPadre
            ) //Como es un nuevo padre se asigna la ip y puerto del nuevo padre
            padre = Connection(socket)
            sirviente(padre!!)
            padre!!.mandarMensaje(
                MensajeProtocolo.add(
                    iP,
                    puerto,
                    nombre,
                    tokenSeguridad
                )
            ) //Se manda mensaje al padre de que alguien se quiere conectar, nombre y tokenseguridad son del hijo
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }

    /*
     * El sirviente crea una tarea que es asignada a un hilo virtual automaticamente
     * por el ejecutor. Se queda a la espera de mensajes entrantes en el socket indicado.
     * */
    private fun sirviente(connection: Connection) {
        exec.execute {
            //Escucha hasta que reciba un mensaje entrante
            try {
                var mensaje: Any?
                while (connection.readObject().also { mensaje = it } != null) {
                    if (mensaje is MensajeProtocolo) procesarMensaje(
                        mensaje as MensajeProtocolo,
                        connection
                    ) else if (mensaje is String) procesarString(
                        mensaje as String
                    )
                }
            } catch (e: Exception) {
                eliminarConexion(connection)
            }
        }
    }

    /*
     * Manda un mensaje al destino indicado. Si tiene un socket abierto lo usa
     * puertoHost: puerto que debe estar abierto del host para recibir mensaje
     * */
    fun mandaMensaje(ipHost: String?, puertoHost: Int, mensaje: Any?) {
        try {
            Connection(Socket(ipHost, puertoHost))
                .use { tempConexion ->
                tempConexion.mandarMensaje(
                    mensaje
                )
            }
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }

    /**
     * Este método establece una conexión socket a una dirección de un servidor Python y envía un mensaje en formato de cadena
     * que se puede decodificar fácilmente en Python usando UTF-8.
     *
     * @param ip La dirección IP del servidor al que se desea conectar.
     * @param puerto El puerto del servidor al que se desea conectar.
     * @param mensaje La cadena de mensaje que se desea enviar.
     */
    private fun mandaMensajeString(ip: String?, puerto: Int, mensaje: String) {
        try {
            Socket(ip, puerto).use { socket ->
                val out = socket.getOutputStream()
                // Convertimos el mensaje a UTF-8 y lo enviamos
                out.write(mensaje.toByteArray(StandardCharsets.UTF_8))
                out.flush() // Aseguramos que el mensaje se envíe completamente
            }
        } catch (e: Exception) {
            e.printStackTrace(System.err)
        }
    }

    fun ping(ip: String?, puerto: Int, mensajes: Int) {
        exec.execute {
            try {
                for (i in 0 until mensajes) {
                    mandaMensaje(ip, puerto, MensajeProtocolo.nothing())
                    Thread.sleep(1000)
                }
            } catch (e: Exception) {
                e.printStackTrace(System.err)
            }
        }
    }

    /*
     * Este metodo da la bienvenida a un nuevo nodo
     * */
    fun welcome(): String {
        return String.format(
            """
                ${'\u001b'}[1m${'\u001b'}[38;2;255;87;51mNodo creado con exito${'\u001b'}[0m
                %s
                """.trimIndent(), log()
        )
    }

    fun log(): String {
        return String.format(
            """
                Nombre: %s
                IP: %s
                IP externa: %s
                Puerto: %s
                Número hijos: %d
                %s
                Variables: %s
                """.trimIndent(),
            nombre, iP, externalIP,
            puerto, numNodos, infoVecinos(), variables.toString()
        )
    }

    // Dice quien es el padre y quienes son sus hijos
    fun infoVecinos(): String {
        return """
             Parent: ${if (padre == null) "NULL" else Utiles.socketToStringPair(padre!!.socket)}
             Descendientes: $descendientes
             """.trimIndent()
    }
}
