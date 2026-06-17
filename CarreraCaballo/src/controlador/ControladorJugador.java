package controlador;

import dao.IJugadorDAO;
import dao.impl.JugadorDAOImpl;
import dto.JugadorDTO;
import modelo.Jugador;

public class ControladorJugador {

    private final IJugadorDAO jugadorDAO;

    public ControladorJugador() {
        this.jugadorDAO = new JugadorDAOImpl();
    }

    public JugadorDTO login(String mail, String contrasena) {
        Jugador jugador = jugadorDAO.buscarPorMail(mail);
        if (jugador != null && jugador.getContrasena().equals(contrasena)) {
            return new JugadorDTO(jugador.getNombre(), jugador.getMail(), jugador.getPuntaje());
        }
        return null;
    }

    public JugadorDTO crearJugador(String nombre, String mail, String contrasena) {
        if (jugadorDAO.buscarPorMail(mail) != null) return null;
        Jugador nuevo = new Jugador(nombre, mail, contrasena);
        jugadorDAO.guardar(nuevo);
        return new JugadorDTO(nuevo.getNombre(), nuevo.getMail(), nuevo.getPuntaje());
    }
}
