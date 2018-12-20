/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import Jama.Matrix;

/**
 *
 * @author natan
 */
public final class DisponibilidadePeriodo extends Disponibilidade {

    public DisponibilidadePeriodo(Matrix disponibilidade) {
        super(disponibilidade);
    }

    public void somaDisponbilidade(Matrix disponibilidade) {
        this.disponibilidade.plusEquals(disponibilidade);
    }

    public double quantidadeProfessoresDisputandoHorario(Horario horario) {
        return super.getDisponibilidadeNoHorario(horario.dia, horario.hora);
    }

    public void zerarDisponibilidade() {
        this.disponibilidade.timesEquals(0);
    }

    public Integer quantidadeDisponibilidadesDiferentesZero() {
        Integer locaisPossiveis = 0;
        for (int hora = 0; hora < this.getQuantidadeHoras(); hora++) {
            for (int dia = 0; dia < this.getQuantidadeDias(); dia++) {
                if (this.getDisponibilidadeNoHorario(dia, hora) > 0.0) {
                    locaisPossiveis++;
                }
            }
        }

        return locaisPossiveis;
    }
}
