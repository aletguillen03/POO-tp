package modelo;

import modelo.caballos.Caballo;
import java.util.List;

public class Carrera {

    private int distanciaTotal;
    private List<Caballo> participantes;
    private ResultadoCarrera resultado;

    public Carrera(List<Caballo> participantes, int distanciaTotal) {
        this.participantes = participantes;
        this.distanciaTotal = distanciaTotal;
        this.resultado = null;
        for (Caballo c : participantes) {
            c.setDistRecorrida(0);
            c.setEnergia(100);
        }
    }

    public boolean avanzarTurno() {
        for (Caballo c : participantes) {
            int avance = c.calcularAvance();
            c.setDistRecorrida(c.getDistRecorrida() + avance);
        }
        if (hayGanador()) {
            resultado = new ResultadoCarrera(participantes);
            return true;
        }
        return false;
    }

    public boolean hayGanador() {
        for (Caballo c : participantes) {
            if (c.getDistRecorrida() >= distanciaTotal) {
                return true;
            }
        }
        return false;
    }

    public ResultadoCarrera getResultado() {
        return resultado;
    }

    public List<Caballo> getParticipantes() {
        return participantes;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }
}
