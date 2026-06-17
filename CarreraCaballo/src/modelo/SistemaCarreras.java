package modelo;

/**
 * Clase vacía tras el refactor a acceso directo por DAO.
 *
 * Toda la lógica fue distribuida:
 *   - login/registro      → ControladorJugador + JugadorDAOImpl
 *   - caballos/seed       → ControladorCaballo + CaballoDAOImpl
 *   - carrera/historial   → ControladorCarrera + CaballoDAOImpl + HistorialDAOImpl + PuntajeService
 *   - estado de sesión    → VistaCarreras (JugadorDTO jugadorActual, String caballoSeleccionado)
 */
public class SistemaCarreras {
}
