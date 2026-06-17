package controlador;

import dto.JugadorDTO;
import modelo.SistemaCarreras;

public class ControladorJugador {

    private final SistemaCarreras sistema;

    public ControladorJugador() {
        this.sistema = SistemaCarreras.getInstancia();
    }

    public JugadorDTO login(String mail, String contrasena) {
        return sistema.login(mail, contrasena);
    }

    public JugadorDTO crearJugador(String nombre, String mail, String contrasena) {
        return sistema.registrarJugador(nombre, mail, contrasena);
    }

    public String getNombreJugadorActual() {
        return sistema.getJugadorActual().getNombre();
    }

    public int consultarPuntaje() {
        return sistema.getJugadorActual().getPuntaje();
    }
}
