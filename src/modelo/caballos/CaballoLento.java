package modelo.caballos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("LENTO")
public class CaballoLento extends Caballo {

    private static final Random random = new Random();

    public CaballoLento() {}

    public CaballoLento(String nombre) {
        super(nombre, 10, 1, 100);
    }

    @Override
    public String getTipo() { return "Lento"; }

    @Override
    public int calcularAvance() {
        int avance = velocidadBase + random.nextInt(4);
        return avance;
    }
}
