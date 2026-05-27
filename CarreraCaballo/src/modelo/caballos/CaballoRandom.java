package modelo.caballos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("RANDOM")
public class CaballoRandom extends Caballo {

    private static final Random random = new Random();

    public CaballoRandom() {}

    public CaballoRandom(String nombre) {
        super(nombre, 10, 3, 100);
    }

    @Override
    public String getColor() { return "BLUE"; }

    @Override
    public int calcularAvance() {
        return 1 + random.nextInt(20);
    }
}
