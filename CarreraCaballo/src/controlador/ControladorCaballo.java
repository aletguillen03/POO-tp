package controlador;

import dao.ICaballoDAO;
import dao.impl.CaballoDAOImpl;
import dto.CaballoDTO;
import modelo.Jugador;
import modelo.caballos.Caballo;
import modelo.caballos.CaballoEstandar;
import modelo.caballos.CaballoLento;
import modelo.caballos.CaballoRandom;
import modelo.caballos.CaballoRapido;

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
        if (caballoDAO.listarDisponibles().isEmpty()) {
            crearCaballosAleatorios();
        }
    }

    /** Devuelve los caballos disponibles como DTOs para la vista. */
    public List<CaballoDTO> listarDisponibles() {
        List<CaballoDTO> dtos = new ArrayList<>();
        for (Caballo c : caballoDAO.listarDisponibles()) {
            dtos.add(new CaballoDTO(c.getId(), c.getNombre(),
                                    c.getVelocidadBase(), c.getDistRecorrida(), c.getColor()));
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

    /** Crea 4 caballos (uno de cada subtipo) con nombres aleatorios. */
    public void crearCaballosAleatorios() {
        List<String> pool = new ArrayList<>(Arrays.asList(NOMBRES));
        Collections.shuffle(pool, new Random());

        caballoDAO.guardar(new CaballoRapido(pool.get(0)));
        caballoDAO.guardar(new CaballoLento(pool.get(1)));
        caballoDAO.guardar(new CaballoEstandar(pool.get(2)));
        caballoDAO.guardar(new CaballoRandom(pool.get(3)));
    }
}
