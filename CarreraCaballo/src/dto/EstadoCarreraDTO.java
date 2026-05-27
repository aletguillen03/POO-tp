package dto;

import java.util.List;

public class EstadoCarreraDTO {

    public List<CaballoDTO> caballos;
    public int turno;
    public boolean hayGanador;

    public EstadoCarreraDTO(List<CaballoDTO> caballos, int turno, boolean hayGanador) {
        this.caballos = caballos;
        this.turno = turno;
        this.hayGanador = hayGanador;
    }
}
