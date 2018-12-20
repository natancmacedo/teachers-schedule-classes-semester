/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import Jama.Matrix;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public final class DisponibilidadeProfessor extends Disponibilidade {

    public DisponibilidadeProfessor(Matrix disponibilidade) {
        super(disponibilidade);
    }

    public final Boolean horarioEstaDisponivel(Horario horario) {
        return this.horarioEstaDisponivel(horario.dia, horario.hora);
    }

    public final Boolean horarioEstaDisponivel(Integer dia, Integer hora) {
        return super.getDisponibilidadeNoHorario(dia, hora) > 0.0;
    }

    public double somaDasDisponibilidade() {
        double disponibilidadeTotal = 0.0;

        for (int hora = 0; hora < super.getQuantidadeHoras(); hora++) {
            for (int dia = 0; dia < super.getQuantidadeDias(); dia++) {
                disponibilidadeTotal += matrizDisponibilidade.getArrayCopy()[hora][dia];
            }
        }

        return disponibilidadeTotal;
    }

}
