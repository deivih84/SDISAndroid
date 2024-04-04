package moduloBasico.common;

public enum Primitiva {
    // Agrega un nodo hijo a un nodo padre.
    // Uso: SocketAddress+TSPadre
    ADD,
    // Indica que la adición de un nodo hijo a un nodo padre fue correcta.
    ADD_OK,
    // Difunde una variable desde un nodo.
    // Uso: nombreVariable+valor+TS
    MULTICAST,
    // Indica que el inicio de un multicast fue correcto.
    MULTICAST_OK,
    // Transmisión intermedia de un multicast.
    // Uso: nombreVariable+valor
    TRANSMISSION,
    // Indica que la transmisión de un multicast fue iniciada correctamente.
    TRANSMISSION_OK,
    // Retorno de un HASHMAP.
    // Uso: String con el valor de un HASHMAP
    RETURN,
    // Indica el fin de la transmisión de HASHMAPs.
    RETURN_END,
    // Mensaje vacío, no realiza ninguna acción.
    NOTHING,
    // Error en la validación del token recibido.
    BAD_TS,
    // Indica un error en el protocolo, comando no entendido.
    NOTUNDERSTAND;
}