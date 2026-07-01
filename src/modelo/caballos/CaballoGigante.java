package modelo.caballos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("gigante")
public class CaballoGigante extends Caballo {

    private static final Random random = new Random();

    public CaballoGigante() {}

    public CaballoGigante(String nombre) {
        super(nombre, 30, 3, 100);
    }

    @Override
    public String getTipo() { return "Gigante"; }

    @Override
    public int calcularAvance() {
        if (energia <= 0) return velocidadBase - 2;
        int avance = velocidadBase + random.nextInt(6);
        int desgaste = resistencia + random.nextInt(4);
        energia = Math.max(0, energia - desgaste);
        return avance;
    }
}
