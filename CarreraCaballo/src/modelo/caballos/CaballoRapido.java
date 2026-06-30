package modelo.caballos;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Random;

@Entity
@DiscriminatorValue("RAPIDO")
public class CaballoRapido extends Caballo {

    private static final Random random = new Random();

    public CaballoRapido() {}

    public CaballoRapido(String nombre) {
        super(nombre, 12, 4, 100);
    }

    @Override
    public int calcularAvance() {
        if (energia <= 0) return velocidadBase - 4;
        int avance = velocidadBase + random.nextInt(8);
        int desgaste = 8 + random.nextInt(8);
        energia = Math.max(0, energia - desgaste);
        return avance;
    }
}
