package modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import modelo.caballos.Caballo;

@Entity
@Table(name = "jugadores")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Column(unique = true)
    private String mail;

    private String contrasena;

    private int puntaje;

    @Transient
    private Caballo caballoElegido;

    public Jugador() {}

    public Jugador(String nombre, String mail, String contrasena) {
        this.nombre = nombre;
        this.mail = mail;
        this.contrasena = contrasena;
        this.puntaje = 0;
    }

    public void sumarPuntaje(int puntos) {
        this.puntaje += puntos;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getMail() { return mail; }
    public String getContrasena() { return contrasena; }
    public int getPuntaje() { return puntaje; }
    public Caballo getCaballoElegido() { return caballoElegido; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setMail(String mail) { this.mail = mail; }
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    public void setPuntaje(int puntaje) { this.puntaje = puntaje; }
    public void setCaballoElegido(Caballo caballo) { this.caballoElegido = caballo; }
}
