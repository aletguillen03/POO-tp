package dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistorialDTO {

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public final String fecha;
    public final String caballoElegido;
    public final String ganador;
    public final int posicion;
    public final int puntajeObtenido;
    public final String posicionesFinales;

    public HistorialDTO(LocalDateTime fecha, String caballoElegido, String ganador,
                        int posicion, int puntajeObtenido, String posicionesFinales) {
        this.fecha           = fecha != null ? fecha.format(FMT) : "—";
        this.caballoElegido  = caballoElegido;
        this.ganador         = ganador;
        this.posicion        = posicion;
        this.puntajeObtenido = puntajeObtenido;
        this.posicionesFinales = posicionesFinales;
    }
}
