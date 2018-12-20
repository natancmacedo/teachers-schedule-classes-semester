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
public class Disponibilidade {

    protected Matrix matrizDisponibilidade;

    public Disponibilidade(Matrix disponibilidade) {
        this.matrizDisponibilidade = disponibilidade;
    }

    public Matrix getDisponibilidade() {
        return matrizDisponibilidade;
    }

    public double[][] getDisponibilidadeArray() {
        return matrizDisponibilidade.getArray();
    }

    protected double getDisponibilidadeNoHorario(Horario horario) {
        return this.getDisponibilidadeNoHorario(horario.dia, horario.hora);
    }

    public double getDisponibilidadeNoHorario(Integer dia, Integer hora) {
        return this.matrizDisponibilidade.getArray()[hora][dia];
    }

    protected Double getDisponibilidadeNoHorarioDouble(Integer dia, Integer hora) {
        return getDisponibilidadeNoHorario(dia, hora);
    }

    private void setDisponibilidadeNoHorario(Horario horario, double valor) {
        this.matrizDisponibilidade.getArray()[horario.hora][horario.dia] = valor;
    }

    protected void zeraDisponibilidadeNoHorario(Horario horario) {
        this.setDisponibilidadeNoHorario(horario, 0);
    }

    protected Integer getQuantidadeHoras() {
        return matrizDisponibilidade.getArray().length;
    }

    protected Integer getQuantidadeDias() {
        return matrizDisponibilidade.getArray()[0].length;
    }

}
