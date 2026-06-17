package controlador;

import dto.CaballoDTO;
import dto.EstadoCarreraDTO;
import dto.HistorialDTO;
import dto.ResultadoDTO;
import modelo.Carrera;
import modelo.ResultadoCarrera;
import modelo.SistemaCarreras;
import modelo.caballos.Caballo;
import service.PuntajeService;

import java.util.ArrayList;
import java.util.List;

public class ControladorCarrera {

    private final SistemaCarreras sistema;
    private final PuntajeService puntajeService;
    private int turnoActual;

    public ControladorCarrera() {
        this.sistema = SistemaCarreras.getInstancia();
        this.puntajeService = new PuntajeService();
        this.turnoActual = 0;
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

    public List<HistorialDTO> getHistorial() {
        return sistema.getHistorialJugadorActual();
    }

    public ResultadoDTO obtenerResultado() {
        sistema.finalizarCarrera(puntajeService);
        ResultadoCarrera resultado = sistema.getResultadoActual();

        List<String> posiciones = new ArrayList<>();
        for (Caballo c : resultado.getPosiciones()) {
            posiciones.add(c.getNombre() + " — " + c.getDistRecorrida() + " m");
        }

        int puntajeObtenido = 0;
        Caballo elegido = sistema.getJugadorActual() != null
                ? sistema.getJugadorActual().getCaballoElegido() : null;
        if (elegido != null) {
            puntajeObtenido = puntajeService.calcularPuntaje(resultado.posicion(elegido));
        }
        return new ResultadoDTO(resultado.getGanador().getNombre(), puntajeObtenido, posiciones);
    }
}
