package dto;

public class CaballoDTO {

    public Long id;
    public String nombre;
    public int velocidadBase;
    public int distRecorrida;

    public CaballoDTO(String nombre, int velocidadBase, int distRecorrida) {
        this(null, nombre, velocidadBase, distRecorrida);
    }

    public CaballoDTO(Long id, String nombre, int velocidadBase, int distRecorrida) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.distRecorrida = distRecorrida;
    }
}
