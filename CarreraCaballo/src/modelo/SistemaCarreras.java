package modelo;

import dao.ICaballoDAO;
import dao.IJugadorDAO;
import dao.impl.CaballoDAOImpl;
import dao.impl.JugadorDAOImpl;
import dto.CaballoDTO;
import dto.JugadorDTO;
import modelo.caballos.Caballo;
import service.PuntajeService;

import modelo.caballos.CaballoEstandar;
import modelo.caballos.CaballoLento;
import modelo.caballos.CaballoRandom;
import modelo.caballos.CaballoRapido;

import java.util.ArrayList;
import java.util.List;

public class SistemaCarreras {

    private static SistemaCarreras instancia;

    private List<Jugador> jugadores;
    private List<Caballo> caballosDisponibles;
    private List<Carrera> carreras;
    private Jugador jugadorActual;
    private Carrera carreraEnCurso;
    private ResultadoCarrera resultadoActual;
    private IJugadorDAO jugadorDAO;
    private ICaballoDAO caballoDAO;

    private SistemaCarreras() {
        jugadores = new ArrayList<>();
        caballosDisponibles = new ArrayList<>();
        carreras = new ArrayList<>();
        jugadorDAO = new JugadorDAOImpl();
        caballoDAO = new CaballoDAOImpl();
        cargarCaballosIniciales();
    }

    private void cargarCaballosIniciales() {
        if (!caballoDAO.listarDisponibles().isEmpty()) return;

        caballoDAO.guardar(new CaballoRapido("Rayo"));
        caballoDAO.guardar(new CaballoLento("Tortuga"));
        caballoDAO.guardar(new CaballoEstandar("Equilibrio"));
        caballoDAO.guardar(new CaballoRandom("Caos"));
    }

    public static SistemaCarreras getInstancia() {
        if (instancia == null) {
            instancia = new SistemaCarreras();
        }
        return instancia;
    }

    public JugadorDTO login(String mail, String contrasena) {
        Jugador jugador = jugadorDAO.buscarPorMail(mail);
        if (jugador != null && jugador.getContrasena().equals(contrasena)) {
            jugadorActual = jugador;
            return new JugadorDTO(jugador.getNombre(), jugador.getMail(), jugador.getPuntaje());
        }
        return null;
    }

    public JugadorDTO registrarJugador(String nombre, String mail, String contrasena) {
        if (jugadorDAO.buscarPorMail(mail) != null) {
            return null;
        }
        Jugador nuevo = new Jugador(nombre, mail, contrasena);
        jugadorDAO.guardar(nuevo);
        jugadores.add(nuevo);
        jugadorActual = nuevo;
        return new JugadorDTO(nuevo.getNombre(), nuevo.getMail(), nuevo.getPuntaje());
    }

    public List<CaballoDTO> getCaballosDisponibles() {
        caballosDisponibles = caballoDAO.listarDisponibles();
        List<CaballoDTO> dtos = new ArrayList<>();
        for (Caballo c : caballosDisponibles) {
            dtos.add(new CaballoDTO(c.getNombre(), c.getVelocidadBase(), c.getDistRecorrida(), c.getColor()));
        }
        return dtos;
    }

    public Carrera crearCarrera(Jugador jugador, int distancia) {
        List<Caballo> participantes = caballoDAO.listarDisponibles();
        carreraEnCurso = new Carrera(participantes, distancia);
        carreras.add(carreraEnCurso);
        return carreraEnCurso;
    }

    public Carrera getCarreraEnCurso() {
        return carreraEnCurso;
    }

    public ResultadoCarrera getResultadoActual() {
        return resultadoActual;
    }

    public void finalizarCarrera(PuntajeService puntajeService) {
        resultadoActual = carreraEnCurso.getResultado();
        if (resultadoActual != null && jugadorActual != null && jugadorActual.getCaballoElegido() != null) {
            int pos = resultadoActual.posicion(jugadorActual.getCaballoElegido());
            puntajeService.asignarPuntaje(jugadorActual, pos);
            jugadorDAO.actualizar(jugadorActual);
        }
    }

    public Jugador getJugadorActual() {
        return jugadorActual;
    }

    public Caballo buscarCaballo(String nombre) {
        List<Caballo> lista = caballoDAO.listarDisponibles();
        for (Caballo c : lista) {
            if (c.getNombre().equals(nombre)) {
                return c;
            }
        }
        return null;
    }
}
