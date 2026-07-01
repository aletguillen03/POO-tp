package controlador;

import dao.ICaballoDAO;
import dao.impl.CaballoDAOImpl;
import dto.CaballoDTO;
import modelo.Jugador;
import modelo.caballos.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Controlador responsable unicamente de la entidad Caballo
 * (listado, seleccion y alta de caballos).
 */
public class ControladorCaballo {

    private static final String[] NOMBRES = {
        "Rayo", "Trueno", "Centella", "Relampago", "Viento",
        "Tornado", "Cometa", "Lucero", "Furia", "Sombra", "Brio", "Huracan"
    };

    private ICaballoDAO caballoDAO;

    public ControladorCaballo() {
        this.caballoDAO = new CaballoDAOImpl();
        asegurarCaballosBase();
    }

    /** Devuelve los caballos disponibles como DTOs para la vista. */
    public List<CaballoDTO> listarDisponibles() {
        List<CaballoDTO> dtos = new ArrayList<>();
        for (Caballo c : caballoDAO.listarDisponibles()) {
            dtos.add(new CaballoDTO(c.getId(), c.getNombre(),
                                    c.getVelocidadBase(), c.getDistRecorrida(), c.getTipo()));
        }
        return dtos;
    }

    /** Devuelve las entidades Caballo que participaran en la carrera. */
    public List<Caballo> obtenerParticipantes() {
        return caballoDAO.listarDisponibles();
    }

    /** Busca el caballo por id y lo asigna como elegido del jugador. */
    public void seleccionarCaballo(Jugador jugador, int idCaballo) {
        Caballo caballo = caballoDAO.buscarPorId((long) idCaballo);
        if (caballo != null) {
            jugador.setCaballoElegido(caballo);
        }
    }

    /**
     * Agrega solo los tipos de caballo que todavia no existan en la base.
     * No borra nada y no duplica: si ya estan todos, no hace nada.
     */
    public void asegurarCaballosBase() {
        List<Caballo> existentes = caballoDAO.listarDisponibles();

        // 1) asumo que no tengo ninguno
        boolean hayRapido = false, hayLento = false, hayEstandar = false;
        boolean hayRandom = false;

        // 2) recorro los guardados y marco los que SI hay
        for (Caballo c : existentes) {
            if (c instanceof CaballoRapido)   hayRapido = true;
            if (c instanceof CaballoLento)    hayLento = true;
            if (c instanceof CaballoRandom)   hayRandom = true;
            if (c instanceof CaballoEstandar)   hayEstandar = true;

        }

        // 3) agrego SOLO los que faltan
        List<String> pool = new ArrayList<>(Arrays.asList(NOMBRES));
        Collections.shuffle(pool, new Random());
        int i = 0;

        if (!hayRapido)   caballoDAO.guardar(new CaballoRapido(pool.get(i++)));
        if (!hayLento)    caballoDAO.guardar(new CaballoLento(pool.get(i++)));
        if (!hayRandom)   caballoDAO.guardar(new CaballoRandom(pool.get(i++)));
        if (!hayEstandar) caballoDAO.guardar(new CaballoEstandar(pool.get(i++)));

    }
}
