package modelo;

import modelo.caballos.Caballo;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ResultadoCarrera {

    private List<Caballo> posiciones;

    public ResultadoCarrera(List<Caballo> participantes) {
        this.posiciones = new ArrayList<>(participantes);
        this.posiciones.sort(Comparator.comparingInt(Caballo::getDistRecorrida).reversed());
    }

    public Caballo getGanador() {
        return posiciones.get(0);
    }

    public int posicion(Caballo caballo) {
        for (int i = 0; i < posiciones.size(); i++) {
            if (posiciones.get(i).getNombre().equals(caballo.getNombre())) {
                return i + 1;
            }
        }
        return -1;
    }

    public List<Caballo> getPosiciones() {
        return posiciones;
    }
}
