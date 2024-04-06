package com.miau.sdisandroid

import java.net.ServerSocket
import java.net.Socket

object Utiles {
    fun getConnectionKey(chunk: String, puertoHijo: Int): String {
        return "$chunk:$puertoHijo"
    }

    fun socketToStringPair(socket: Socket): String {
        return getSocketIP(socket) + ":" + getSocketPort(socket)
    }

    fun getSocketIP(socket: Socket): String {
        return socket.getInetAddress().hostAddress
    }

    fun getSocketPort(socket: Socket): Int {
        return socket.getPort()
    }

    fun getSocketIP(socket: ServerSocket): String {
        return socket.getInetAddress().hostAddress
    }

    fun getSocketPort(socket: ServerSocket): Int {
        return socket.getLocalPort()
    }

    fun socketEquals(hijoSocket: Socket, socket: Socket): Boolean {
        return getSocketIP(hijoSocket) == getSocketIP(socket) && getSocketPort(hijoSocket) == getSocketPort(
            socket
        )
    }

    fun ayudaLanzador() {
        println(
            """
                ${'\u001b'}[1m${'\u001b'}[38;2;123;198;232mAyuda sobre los comandos 😀${'\u001b'}[0m
                                
                Al ejecutar este lanzador has creado un nodo que almacena un mapa de claves y valores.
                Puedes lanzar más nodos en otra terminal/dispositivo y conectarlos entre ellos creando asi una jerarquía.

                Tienes acceso a varios comandos que te pueden ayudar:
                [log] Te muestra la jerarquía con respecto a tu nodo.
                                
                [padre <ip> <puerto> <token>] Te conectará a tu nuevo padre (desconectándote del antiguo).
                    ip  -IP del nodo padre
                    puerto  -Puerto del nodo padre
                    token   -Token de seguridad del nodo padre

                [nodo <nombre>] Consulta un nombre de nodo (mediante su ruta).
                    nombre  -Nombre del nodo a buscar
                    
                [multicast <ip> <puerto> <nomVar> <valor> <token>] Retransmite un mensaje que se especifique a todos los nodos descendientes.
                    ip      -Ip del nodo desde el que se enviará el multicast
                    puerto  -Puerto del nodo desde el que se enviará el multicast
                    nomVar  -Nombre de la variable a retransmitir
                    valor   -Valor (entero) de la variable que se ha compartido
                    token   -Token de seguridad de la conversación
                    
                [nNodos] Te muestra la cantidad de nodos conectados a tu nodo actual
                                
                [transmision <nombreDest> <puertoDest> <nombreVar> <valor>] Transmitir una variable a un nodo concreto.
                    nombreDest  -Nombre del nodo destinatario
                    puertoDest  -Puerto del nodo destinatario
                    nombreVar   -Nombre de la variable a transmitir
                    valor       -Valor entero que se le asignará a la variable
                    
                [grafo <ip> <puerto>] Muestra una representacion gráfica de la gerarquía (pijadas...)
                    ip      -La ip del servidor Python en el que se va a generar el grafo en HTML
                    puerto  -El puerto de dicho servidor (Ejecutar visualizer.py antes de meter este comando)
                    
                [ping <ip> <puerto> <num>] Manda pings al nodo indicado para comprobar que estan ahí
                    ip      -La ip del nodo al que se manda el ping
                    puerto  -El puerto del nodo al que se manda el ping
                    num     -La cantidad de pings que se van a mandar
                    
                [help] Soy yo 😎 (Meta-ayuda sobre la propia ayuda [dada por la ayuda en si😜])
                    Espero haberte ayudado
                
                """.trimIndent()
        )
    }
    enum class Servicio {
        NODOHIJO,
        NODOPADRE,
        ATAQUEDDOS,
        INDEFINIDO
    }
}
