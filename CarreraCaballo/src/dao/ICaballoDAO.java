package dao;

import modelo.caballos.Caballo;
import java.util.List;

public interface ICaballoDAO {
    void guardar(Caballo caballo);
    Caballo buscarPorId(Long id);
    List<Caballo> listarDisponibles();
}
