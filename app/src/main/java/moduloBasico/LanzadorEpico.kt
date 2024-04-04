package java.moduloBasico

import moduloBasico.Nodo
import moduloBasico.Utiles
import kotlin.time.measureTime

/*
Lo dejo aquí, por si acaso: cd out/production/SDIS1;
Raiz 4000 abc123
Hijo 8000 abc123 0.0.0.0 4000 abc123
Args[0] = nombre, Args[1] = Puerto, Args[2] = Token del hijo
 */
object LanzadorEpico {
    @JvmStatic
    fun main(args: Array<String>) {
        // Nuevo nodo 😎
        var nuevoNodo: Nodo? = null
        try {
            if (args.size != 7) nuevoNodo = Nodo(args[0], args[1].toInt(), args[2])
        } catch (e: java.lang.Exception) {
            e.printStackTrace(System.err)
        }
//        \033[1m\033[38;2;255;87;51mAyuda sobre los comandos 😀\033[0m
        val tiempo = measureTime {
            when (args.size) {
                6 -> nuevoNodo!!.conexionPadre(args[3], args[4].toInt(), args[5])  // Argumentos del padre...
                7 -> repeat(args[6].toInt()) { // nombre = nombre + iterador; puerto = puerto + iterador; clave es la misma
                    println("Creado el hijo: " + args[0] + it + ". Disfrute 😊")
                    val nodoi = Nodo(args[0] + it, 0, args[2])
                    nodoi.conexionPadre(args[3], args[4].toInt(), args[5])
                }
            }
        }
        println("Tiempo en lanzar: $tiempo \n")

        while (true) {
            val comando = readln().trim().split(" ")
            when (comando[0]) { // Una vez más kotlin demostrando porque es mucho mejor que java
                "log" -> println(nuevoNodo!!.log())
                "padre" -> nuevoNodo!!.conexionPadre(comando[1], comando[2].toInt(), comando[3])
                //"nodo" -> nuevoNodo!!.getNodo(comando[1], nuevoNodo.ip, nuevoNodo.puerto)
//                "multicast" -> nuevoNodo!!.mandaMensaje(comando[1], comando[2].toInt(), MensajeProtocolo.multicast(nuevoNodo.ip, comando[3], comando[4].toInt(), comando[5]))
                "nNodos" -> println("Hay " + nuevoNodo!!.numNodos + " nodos conectados actualmente.")
//                "transmision" -> nuevoNodo!!.mandaMensaje(comando[1], comando[2].toInt(), MensajeProtocolo.transmission(nuevoNodo.ip, comando[3], comando[4].toInt()))
                "grafo" -> nuevoNodo!!.grafo(comando[1], comando[2].toInt(), nuevoNodo.nombre)
                "ping" -> nuevoNodo!!.ping(comando[1], comando[2].toInt(), comando[3].toInt())
                "help" -> Utiles.ayudaLanzador()

                else -> println("Comando incorrecto, si no lo tienes claro prueba a escribir \"help\"😉\n")
            }
        }
    }
}