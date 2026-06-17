package controlador;

import dto.CaballoDTO;
import modelo.SistemaCarreras;
import modelo.caballos.Caballo;

import java.util.List;

public class ControladorCaballo {

    private final SistemaCarreras sistema;

    public ControladorCaballo() {
        this.sistema = SistemaCarreras.getInstancia();
    }

    public List<CaballoDTO> getCaballosDisponibles() {
        return sistema.getCaballosDisponibles();
    }

    public boolean elegirCaballo(String nombreCaballo) {
        Caballo caballo = sistema.buscarCaballo(nombreCaballo);
        if (caballo == null) {
            return false;
        }
        sistema.getJugadorActual().setCaballoElegido(caballo);
        return true;
    }
}
