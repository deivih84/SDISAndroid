package com.miau.sdisandroid

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.time.measureTime

/*
Lo dejo aquí, por si acaso: cd out/production/SDIS1;
Raiz 4000 abc123
Hijo 8000 abc123 0.0.0.0 4000 abc123
Args[0] = nombre, Args[1] = Puerto, Args[2] = Token del hijo
 */
object LanzadorEpico {
    @OptIn(DelicateCoroutinesApi::class)
    @JvmStatic
    fun lanzador(args: Array<String?>, servicio: Utiles.Servicio) { // PUEDE QUE LOS ARGUMENTOS SEAN NULOS No sabemos 🤔
        println("HOLI LANZADOR $servicio")
        repeat(7) {
            println(args[it])
        }

        // Nuevo nodo 😎 83.58.116.170
        val nuevoNodo = nuevoNodo(args[0]!!, args[1]!!.toInt(), args[2]!!)
        println(nuevoNodo)
        val tiempo = measureTime {
            GlobalScope.launch {
                when (servicio) {
                    Utiles.Servicio.NODOHIJO -> nuevoNodo!!.conexionPadre(args[3], args[4]!!.toInt(), args[5])  // Argumentos del padre...
                    Utiles.Servicio.ATAQUEDDOS -> repeat(args[6]!!.toInt()) { // nombre = nombre + iterador; puerto = puerto + iterador; clave es la misma
                        println("Creado el hijo: " + args[0] + it + ". Disfrute 😊")
                        val nodoi = Nodo(args[0] + it, 0, args[2]!!)
                        nodoi.conexionPadre(args[3], args[4]!!.toInt(), args[5])
                    }

                    Utiles.Servicio.NODOPADRE -> println("Padre")
                    Utiles.Servicio.INDEFINIDO -> println("Indefinido")
                }
            }
        }
        println("Tiempo en lanzar: $tiempo \n")
    }
    private fun nuevoNodo(nombre: String, puerto: Int, token: String): Nodo? {
        var nuevoNodo: Nodo? = null
        try { // Se crea el nuevo nodo a menos que se quiera un ataque DDos
            nuevoNodo = Nodo(nombre, puerto, token)
        } catch (e: java.lang.Exception) {
            e.printStackTrace(System.err)
        }

        return nuevoNodo
    }
}