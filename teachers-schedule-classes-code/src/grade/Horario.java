/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author natan
 */
public final class Horario {

    public final Integer dia;
    public final Integer hora;

    public Horario(Integer dia, Integer hora) {
        this.dia = dia;
        this.hora = hora;
    }

    public static List<Horario> montaListaHorarios(Integer dia, Integer horaInicial, Integer horaFinal) {
        List<Horario> horarios = new ArrayList<>();

        for (int hora = horaInicial; hora <= horaFinal; hora++) {
            horarios.add(new Horario(dia, hora));
        }
        return horarios;
    }

    @Override
    public String toString() {
        return "Horario{" + "dia=" + dia + ", hora=" + hora + '}';
    }

}
