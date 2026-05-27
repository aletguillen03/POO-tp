package dto;

import java.util.List;

public class ResultadoDTO {

    public String ganador;
    public int puntajeObtenido;
    public List<String> posiciones;

    public ResultadoDTO(String ganador, int puntajeObtenido, List<String> posiciones) {
        this.ganador = ganador;
        this.puntajeObtenido = puntajeObtenido;
        this.posiciones = posiciones;
    }
}
