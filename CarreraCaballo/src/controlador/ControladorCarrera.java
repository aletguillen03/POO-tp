package controlador;

import dto.CaballoDTO;
import dto.EstadoCarreraDTO;
import dto.JugadorDTO;
import dto.ResultadoDTO;
import modelo.Carrera;
import modelo.ResultadoCarrera;
import modelo.SistemaCarreras;
import modelo.caballos.Caballo;
import service.PuntajeService;

import java.util.ArrayList;
import java.util.List;

public class ControladorCarrera {

    private SistemaCarreras sistema;
    private PuntajeService puntajeService;
    private int turnoActual;

    public ControladorCarrera() {
        this.sistema = SistemaCarreras.getInstancia();
        this.puntajeService = new PuntajeService();
        this.turnoActual = 0;
    }

    public JugadorDTO login(String mail, String contrasena) {
        return sistema.login(mail, contrasena);
    }

    public JugadorDTO crearJugador(String nombre, String mail, String contrasena) {
        return sistema.registrarJugador(nombre, mail, contrasena);
    }

    public List<CaballoDTO> getCaballosDisponibles() {
        return sistema.getCaballosDisponibles();
    }

    public boolean elegirCaballo(String nombreCaballo) {
        Caballo caballo = sistema.buscarCaballo(nombreCaballo);
        if (caballo != null) {
            sistema.getJugadorActual().setCaballoElegido(caballo);
            return true;
        }
        return false;
    }

    public void prepararCarrera(int distancia) {
        turnoActual = 0;
        sistema.crearCarrera(sistema.getJugadorActual(), distancia);
    }

    public EstadoCarreraDTO avanzarTurno() {
        Carrera carrera = sistema.getCarreraEnCurso();
        boolean terminada = carrera.avanzarTurno();
        turnoActual++;

        List<CaballoDTO> posiciones = new ArrayList<>();
        for (Caballo c : carrera.getParticipantes()) {
            posiciones.add(new CaballoDTO(c.getNombre(), c.getVelocidadBase(), c.getDistRecorrida(), c.getColor()));
        }

        return new EstadoCarreraDTO(posiciones, turnoActual, terminada);
    }

    public String getNombreJugadorActual() {
        return sistema.getJugadorActual().getNombre();
    }

    public int consultarPuntaje() {
        return sistema.getJugadorActual().getPuntaje();
    }

    public ResultadoDTO obtenerResultado() {
        sistema.finalizarCarrera(puntajeService);

        ResultadoCarrera resultado = sistema.getResultadoActual();
        String ganador = resultado.getGanador().getNombre();

        List<String> posiciones = new ArrayList<>();
        for (Caballo c : resultado.getPosiciones()) {
            posiciones.add(c.getNombre() + " — " + c.getDistRecorrida() + " m");
        }

        int puntajeObtenido = 0;
        if (sistema.getJugadorActual() != null && sistema.getJugadorActual().getCaballoElegido() != null) {
            int pos = resultado.posicion(sistema.getJugadorActual().getCaballoElegido());
            puntajeObtenido = puntajeService.calcularPuntaje(pos);
        }

        return new ResultadoDTO(ganador, puntajeObtenido, posiciones);
    }
}
