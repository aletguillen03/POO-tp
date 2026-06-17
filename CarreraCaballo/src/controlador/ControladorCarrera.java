package controlador;

import dao.IHistorialDAO;
import dao.IJugadorDAO;
import dao.impl.HistorialDAOImpl;
import dao.impl.JugadorDAOImpl;
import dto.CaballoDTO;
import dto.EstadoCarreraDTO;
import dto.HistorialDTO;
import dto.ResultadoDTO;
import modelo.Carrera;
import modelo.HistorialCarrera;
import modelo.Jugador;
import modelo.ResultadoCarrera;
import modelo.caballos.Caballo;
import service.PuntajeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Controlador responsable de la carrera: prepararla, avanzar turnos,
 * detectar al ganador y resolver el resultado (puntaje + historial).
 */
public class ControladorCarrera {

    private Carrera carreraActual;
    private PuntajeService puntajeService;
    private IHistorialDAO historialDAO;
    private IJugadorDAO jugadorDAO;
    private int turnoActual;

    public ControladorCarrera() {
        this.puntajeService = new PuntajeService();
        this.historialDAO = new HistorialDAOImpl();
        this.jugadorDAO = new JugadorDAOImpl();
        this.turnoActual = 0;
    }

    public void prepararCarrera(List<Caballo> participantes, int distancia) {
        this.turnoActual = 0;
        this.carreraActual = new Carrera(participantes, distancia);
    }

    public EstadoCarreraDTO avanzarTurno() {
        boolean terminada = carreraActual.avanzarTurno();
        turnoActual++;

        List<CaballoDTO> posiciones = new ArrayList<>();
        for (Caballo c : carreraActual.getParticipantes()) {
            posiciones.add(new CaballoDTO(c.getId(), c.getNombre(),
                                          c.getVelocidadBase(), c.getDistRecorrida(), c.getColor()));
        }
        return new EstadoCarreraDTO(posiciones, turnoActual, terminada);
    }

    public boolean hayGanador() {
        return carreraActual.hayGanador();
    }

    public ResultadoDTO obtenerResultado(Jugador jugador) {
        ResultadoCarrera resultado = carreraActual.getResultado();

        String ganador = resultado.getGanador().getNombre();
        List<String> posiciones = new ArrayList<>();
        for (Caballo c : resultado.getPosiciones()) {
            posiciones.add(c.getNombre() + " — " + c.getDistRecorrida() + " m");
        }

        int puntajeObtenido = 0;
        if (jugador != null && jugador.getCaballoElegido() != null) {
            int pos = resultado.posicion(jugador.getCaballoElegido());
            puntajeObtenido = puntajeService.calcularPuntaje(pos);
            puntajeService.asignarPuntaje(jugador, pos);
            jugadorDAO.actualizar(jugador);
            registrarHistorial(jugador, resultado, pos, puntajeObtenido);
        }

        return new ResultadoDTO(ganador, puntajeObtenido, posiciones);
    }

    public List<HistorialDTO> getHistorial(Jugador jugador) {
        List<HistorialDTO> dtos = new ArrayList<>();
        if (jugador == null) return dtos;
        for (HistorialCarrera h : historialDAO.listarPorJugador(jugador)) {
            dtos.add(new HistorialDTO(h.getFecha(), h.getCaballoElegido(), h.getGanador(),
                                      h.getPosicion(), h.getPuntajeObtenido(), h.getPosicionesFinales()));
        }
        return dtos;
    }

    private void registrarHistorial(Jugador jugador, ResultadoCarrera resultado,
                                    int posicion, int puntos) {
        StringBuilder sb = new StringBuilder();
        List<Caballo> posiciones = resultado.getPosiciones();
        for (int i = 0; i < posiciones.size(); i++) {
            if (i > 0) sb.append("|");
            sb.append(i + 1).append(". ")
              .append(posiciones.get(i).getNombre())
              .append(" — ").append(posiciones.get(i).getDistRecorrida()).append("m");
        }
        historialDAO.guardar(new HistorialCarrera(
            jugador,
            jugador.getCaballoElegido().getNombre(),
            posicion,
            puntos,
            resultado.getGanador().getNombre(),
            sb.toString(),
            LocalDateTime.now()
        ));
    }
}
