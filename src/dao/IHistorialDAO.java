package dao;

import modelo.HistorialCarrera;
import modelo.Jugador;
import java.util.List;

public interface IHistorialDAO {
    void guardar(HistorialCarrera historial);
    List<HistorialCarrera> listarPorJugador(Jugador jugador);
}
