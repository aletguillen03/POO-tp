package controlador;

import dao.IJugadorDAO;
import dao.impl.JugadorDAOImpl;
import dto.JugadorDTO;
import modelo.Jugador;
import service.PuntajeService;

/**
 * Controlador responsable unicamente de la entidad Jugador
 * (alta, autenticacion y consulta de puntaje).
 *
 * Mantiene la sesion del jugador actual para que la Vista pueda
 * pasar la entidad a los demas controladores sin estado global.
 */
public class ControladorJugador {

    private IJugadorDAO jugadorDAO;
    private PuntajeService puntajeService;
    private Jugador jugadorActual;

    public ControladorJugador() {
        this.jugadorDAO = new JugadorDAOImpl();
        this.puntajeService = new PuntajeService();
    }

    public JugadorDTO login(String mail, String password) {
        Jugador jugador = jugadorDAO.buscarPorMail(mail);
        if (jugador != null && jugador.getContrasena().equals(password)) {
            jugadorActual = jugador;
            return new JugadorDTO(jugador.getNombre(), jugador.getMail(), jugador.getPuntaje());
        }
        return null;
    }

    public JugadorDTO registrarJugador(String nombre, String mail, String password) {
        if (jugadorDAO.buscarPorMail(mail) != null) {
            return null;
        }
        Jugador nuevo = new Jugador(nombre, mail, password);
        jugadorDAO.guardar(nuevo);
        jugadorActual = nuevo;
        return new JugadorDTO(nuevo.getNombre(), nuevo.getMail(), nuevo.getPuntaje());
    }

    public int consultarPuntaje(Jugador jugador) {
        return puntajeService.consultarPuntaje(jugador);
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }
}
