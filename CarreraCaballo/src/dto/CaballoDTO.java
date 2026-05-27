package dto;

public class CaballoDTO {

    public String nombre;
    public int velocidadBase;
    public int distRecorrida;
    public String color;

    public CaballoDTO(String nombre, int velocidadBase, int distRecorrida, String color) {
        this.nombre = nombre;
        this.velocidadBase = velocidadBase;
        this.distRecorrida = distRecorrida;
        this.color = color;
    }
}
