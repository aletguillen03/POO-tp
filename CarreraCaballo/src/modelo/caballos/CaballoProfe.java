package modelo.caballos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("PROFE")
public class CaballoProfe extends Caballo {

    private static final Random random = new Random();

    public CaballoProfe() {}

    public CaballoProfe(String nombre) {
        super(nombre, 12, 2, 100);
    }

    @Override
    public String getTipo() { return "Profe"; }

    @Override
    public int calcularAvance() {
        if (energia <= 0) return velocidadBase - 1;
        int avance = velocidadBase + random.nextInt(8);
        int desgaste = resistencia + random.nextInt(3);
        energia = Math.max(0, energia - desgaste);
        return avance;
    }
}
