package dto;

public class CaballoDTO {

    public Long id;
    public String nombre;
    public int velocidadBase;
    public int distRecorrida;
    public String tipo;

    public CaballoDTO(String nombre, int velocidadBase, int distRecorrida, String tipo) {
        this(null, nombre, velocidadBase, distRecorrida, tipo);
    }

    public CaballoDTO(Long id, String nombre, int velocidadBase, int distRecorrida, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.distRecorrida = distRecorrida;
        this.tipo = tipo;
    }
}
