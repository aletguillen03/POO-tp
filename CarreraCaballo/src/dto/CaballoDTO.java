package dto;

public class CaballoDTO {

    public Long id;
    public String nombre;
    public int velocidadBase;
    public int distRecorrida;
    public String color;

    public CaballoDTO(String nombre, int velocidadBase, int distRecorrida, String color) {
        this(null, nombre, velocidadBase, distRecorrida, color);
    }

    public CaballoDTO(Long id, String nombre, int velocidadBase, int distRecorrida, String color) {
        this.id = id;
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.distRecorrida = distRecorrida;
        this.color = color;
    }
}
