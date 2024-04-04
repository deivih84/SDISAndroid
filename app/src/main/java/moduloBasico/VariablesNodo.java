package moduloBasico;

import java.util.concurrent.ConcurrentHashMap;

/*
 * Este será el sistema que gestiona las variables de cada nodo. Fácil y sencillo.
 * */
public class VariablesNodo {
    private final ConcurrentHashMap<String, Object> map;

    public VariablesNodo() {
        map = new ConcurrentHashMap<>();
    }

    public VariablesNodo(VariablesNodo otro) {
        map = new ConcurrentHashMap<>(otro.getMap());
    }

    public ConcurrentHashMap<String, Object> getMap() {
        return new ConcurrentHashMap<>(map);
    }

    public void add(String var, Object valor) {
        if (!map.containsKey(var))
            map.put(var, valor);
    }

    public void updateKey(String var, Object valor) {
        if (map.containsKey(var))
            map.put(var, valor);
    }

    public void removeKey(String var) {
        map.remove(var);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object valor) {
        return map.containsValue(valor);
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
