package com.miau.sdisandroid

import kotlin.time.measureTime

/*
Lo dejo aquí, por si acaso: cd out/production/SDIS1;
Raiz 4000 abc123
Hijo 8000 abc123 0.0.0.0 4000 abc123
Args[0] = nombre, Args[1] = Puerto, Args[2] = Token del hijo
 */
object LanzadorEpico {
    @JvmStatic
    fun lanzador(args: Array<String?>, servicio: Utiles.Servicio) { // PUEDE QUE LOS ARGUMENTOS SEAN NULOS No sabemos 🤔
        println("HOLI LANZADOR $servicio")
        repeat(7) {
            println(args[it])
        }

        // Nuevo nodo 😎
/*        val nuevoNodo = nuevoNodo(args[0]!!, args[1]!!.toInt(), args[2]!!)
        val tiempo = measureTime {
            when (servicio) {
                Utiles.Servicio.NODOHIJO -> nuevoNodo!!.conexionPadre(args[3], args[4]!!.toInt(), args[5])  // Argumentos del padre...
                Utiles.Servicio.ATAQUEDDOS -> repeat(args[6]!!.toInt()) { // nombre = nombre + iterador; puerto = puerto + iterador; clave es la misma
                    println("Creado el hijo: " + args[0] + it + ". Disfrute 😊")
                    val nodoi = Nodo(args[0] + it, 0, args[2]!!)
                    nodoi.conexionPadre(args[3], args[4]!!.toInt(), args[5])
                }

                Utiles.Servicio.NODOPADRE -> TODO()
                Utiles.Servicio.INDEFINIDO -> TODO()
            }
        }
        println("Tiempo en lanzar: $tiempo \n")*/

/*        while (true) {
            val comando = readln().trim().split(" ")
            when (comando[0]) { // Una vez más kotlin demostrando porque es mucho mejor que java
                "log" -> println(nuevoNodo!!.log())
                "padre" -> nuevoNodo!!.conexionPadre(comando[1], comando[2].toInt(), comando[3])
//                "nodo" -> nuevoNodo!!.getNodo(comando[1], nuevoNodo.ip, nuevoNodo.puerto)
//                "multicast" -> nuevoNodo!!.mandaMensaje(comando[1], comando[2].toInt(), MensajeProtocolo.multicast(nuevoNodo.ip, comando[3], comando[4].toInt(), comando[5]))
                "nNodos" -> println("Hay " + nuevoNodo!!.numNodos + " nodos conectados actualmente.")
//                "transmision" -> nuevoNodo!!.mandaMensaje(comando[1], comando[2].toInt(), MensajeProtocolo.transmission(nuevoNodo.ip, comando[3], comando[4].toInt()))
                "grafo" -> nuevoNodo!!.grafo(comando[1], comando[2].toInt(), nuevoNodo.nombre)
                "ping" -> nuevoNodo!!.ping(comando[1], comando[2].toInt(), comando[3].toInt())
                "help" -> Utiles.ayudaLanzador()

                else -> println("Comando incorrecto, si no lo tienes claro prueba a escribir \"help\"😉\n")
            }
        }*/
    }
    fun nuevoNodo(nombre: String, puerto: Int, token: String): Nodo? {
        var nuevoNodo: Nodo? = null
        try { // Se crea el nuevo nodo a menos que se quiera un ataque DDos
            nuevoNodo = Nodo(nombre, puerto, token)
        } catch (e: java.lang.Exception) {
            e.printStackTrace(System.err)

        }
        return nuevoNodo
    }
}