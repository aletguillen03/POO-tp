package dao;

import modelo.Jugador;
import java.util.List;

public interface IJugadorDAO {
    void guardar(Jugador jugador);
    Jugador buscarPorMail(String mail);
    void actualizar(Jugador jugador);
    List<Jugador> listarTodos();
}
