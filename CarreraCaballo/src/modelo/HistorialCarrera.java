package modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_carreras")
public class HistorialCarrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "jugador_id", nullable = false)
    private Jugador jugador;

    private String caballoElegido;
    private int posicion;
    private int puntajeObtenido;
    private String ganador;

    @Column(length = 500)
    private String posicionesFinales;

    private LocalDateTime fecha;

    public HistorialCarrera() {}

    public HistorialCarrera(Jugador jugador, String caballoElegido, int posicion,
                             int puntajeObtenido, String ganador,
                             String posicionesFinales, LocalDateTime fecha) {
        this.jugador = jugador;
        this.caballoElegido = caballoElegido;
        this.posicion = posicion;
        this.puntajeObtenido = puntajeObtenido;
        this.ganador = ganador;
        this.posicionesFinales = posicionesFinales;
        this.fecha = fecha;
    }

    public Long getId()                  { return id; }
    public Jugador getJugador()          { return jugador; }
    public String getCaballoElegido()    { return caballoElegido; }
    public int getPosicion()             { return posicion; }
    public int getPuntajeObtenido()      { return puntajeObtenido; }
    public String getGanador()           { return ganador; }
    public String getPosicionesFinales() { return posicionesFinales; }
    public LocalDateTime getFecha()      { return fecha; }
}
