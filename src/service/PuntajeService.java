package service;

import modelo.Jugador;

public class PuntajeService {

    public int calcularPuntaje(int pos) {
        if (pos == 1) return 100;
        if (pos == 2) return 50;
        return 10;
    }

    public void asignarPuntaje(Jugador j, int pos) {
        int puntos = calcularPuntaje(pos);
        j.sumarPuntaje(puntos);
    }

    public int consultarPuntaje(Jugador j) {
        return j.getPuntaje();
    }
}
