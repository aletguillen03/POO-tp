package modelo.caballos;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity
@Table(name = "caballos")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public abstract class Caballo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    protected String nombre;

    protected int velocidadBase;
    protected int resistencia;
    protected int energia;
    protected int distRecorrida;

    public Caballo() {}

    public Caballo(String nombre, int velocidadBase, int resistencia, int energia) {
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.resistencia = resistencia;
        this.energia = energia;
        this.distRecorrida = 0;
    }

    public abstract int calcularAvance();

    public abstract String getTipo();

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public int getVelocidadBase() { return velocidadBase; }
    public int getResistencia() { return resistencia; }
    public int getEnergia() { return energia; }
    public int getDistRecorrida() { return distRecorrida; }

    public void setDistRecorrida(int dist) { this.distRecorrida = dist; }
    public void setEnergia(int energia) { this.energia = energia; }
}
