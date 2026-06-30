package controlador;

import dao.ICaballoDAO;
import dao.IHistorialDAO;
import dao.IJugadorDAO;
import dao.impl.CaballoDAOImpl;
import dao.impl.HistorialDAOImpl;
import dao.impl.JugadorDAOImpl;
import dto.CaballoDTO;
import dto.EstadoCarreraDTO;
import dto.HistorialDTO;
import dto.JugadorDTO;
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

public class ControladorCarrera {

    private final ICaballoDAO caballoDAO;
    private final IHistorialDAO historialDAO;
    private final IJugadorDAO jugadorDAO;
    private final PuntajeService puntajeService;
    private Carrera carreraEnCurso;
    private int turnoActual;

    public ControladorCarrera() {
        this.caballoDAO      = new CaballoDAOImpl();
        this.historialDAO    = new HistorialDAOImpl();
        this.jugadorDAO      = new JugadorDAOImpl();
        this.puntajeService  = new PuntajeService();
        this.turnoActual     = 0;
    }

    public void prepararCarrera(int distancia) {
        turnoActual = 0;
        List<Caballo> participantes = caballoDAO.listarDisponibles();
        carreraEnCurso = new Carrera(participantes, distancia);
    }

    public EstadoCarreraDTO avanzarTurno() {
        boolean terminada = carreraEnCurso.avanzarTurno();
        turnoActual++;
        List<CaballoDTO> caballos = new ArrayList<>();
        for (Caballo c : carreraEnCurso.getParticipantes()) {
            caballos.add(new CaballoDTO(c.getNombre(), c.getVelocidadBase(), c.getDistRecorrida()));
        }
        return new EstadoCarreraDTO(caballos, turnoActual, terminada);
    }

    public ResultadoDTO obtenerResultado(JugadorDTO jugadorDTO, String nombreCaballo) {
        ResultadoCarrera resultado = carreraEnCurso.getResultado();

        Jugador jugador = jugadorDAO.buscarPorMail(jugadorDTO.mail);

        Caballo caballoElegido = null;
        for (Caballo c : resultado.getPosiciones()) {
            if (c.getNombre().equals(nombreCaballo)) {
                caballoElegido = c;
                break;
            }
        }

        int puntajeObtenido = 0;
        if (jugador != null && caballoElegido != null) {
            int pos = resultado.posicion(caballoElegido);
            puntajeObtenido = puntajeService.calcularPuntaje(pos);
            puntajeService.asignarPuntaje(jugador, pos);
            jugadorDAO.actualizar(jugador);

            StringBuilder sb = new StringBuilder();
            List<Caballo> posicionesList = resultado.getPosiciones();
            for (int i = 0; i < posicionesList.size(); i++) {
                if (i > 0) sb.append("|");
                sb.append(i + 1).append(". ")
                  .append(posicionesList.get(i).getNombre())
                  .append(" — ").append(posicionesList.get(i).getDistRecorrida()).append("m");
            }
            historialDAO.guardar(new HistorialCarrera(
                jugador, nombreCaballo, pos, puntajeObtenido,
                resultado.getGanador().getNombre(),
                sb.toString(), LocalDateTime.now()
            ));
        }

        List<String> posiciones = new ArrayList<>();
        for (Caballo c : resultado.getPosiciones()) {
            posiciones.add(c.getNombre() + " — " + c.getDistRecorrida() + " m");
        }
        return new ResultadoDTO(resultado.getGanador().getNombre(), puntajeObtenido, posiciones);
    }

    public List<HistorialDTO> getHistorial(JugadorDTO jugadorDTO) {
        Jugador jugador = jugadorDAO.buscarPorMail(jugadorDTO.mail);
        if (jugador == null) return new ArrayList<>();
        List<HistorialCarrera> lista = historialDAO.listarPorJugador(jugador);
        List<HistorialDTO> dtos = new ArrayList<>();
        for (HistorialCarrera h : lista) {
            dtos.add(new HistorialDTO(h.getFecha(), h.getCaballoElegido(), h.getGanador(),
                                      h.getPosicion(), h.getPuntajeObtenido(), h.getPosicionesFinales()));
        }
        return dtos;
    }
}
