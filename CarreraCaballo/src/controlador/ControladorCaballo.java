package controlador;

import dao.ICaballoDAO;
import dao.impl.CaballoDAOImpl;
import dto.CaballoDTO;
import modelo.caballos.Caballo;
import modelo.caballos.CaballoEstandar;
import modelo.caballos.CaballoLento;
import modelo.caballos.CaballoRandom;
import modelo.caballos.CaballoRapido;

import java.util.ArrayList;
import java.util.List;

public class ControladorCaballo {

    private final ICaballoDAO caballoDAO;

    public ControladorCaballo() {
        this.caballoDAO = new CaballoDAOImpl();
        cargarCaballosIniciales();
    }

    public List<CaballoDTO> getCaballosDisponibles() {
        List<Caballo> lista = caballoDAO.listarDisponibles();
        List<CaballoDTO> dtos = new ArrayList<>();
        for (Caballo c : lista) {
            dtos.add(new CaballoDTO(c.getNombre(), c.getVelocidadBase(), c.getDistRecorrida(), c.getColor()));
        }
        return dtos;
    }

    public Caballo buscarCaballo(String nombre) {
        for (Caballo c : caballoDAO.listarDisponibles()) {
            if (c.getNombre().equals(nombre)) return c;
        }
        return null;
    }

    private void cargarCaballosIniciales() {
        if (!caballoDAO.listarDisponibles().isEmpty()) return;
        caballoDAO.guardar(new CaballoRapido("Rayo"));
        caballoDAO.guardar(new CaballoLento("Tortuga"));
        caballoDAO.guardar(new CaballoEstandar("Equilibrio"));
        caballoDAO.guardar(new CaballoRandom("Caos"));
    }
}
