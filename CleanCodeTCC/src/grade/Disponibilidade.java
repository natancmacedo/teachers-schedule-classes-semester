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

    protected Matrix disponibilidade;

    public Disponibilidade(Matrix disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public Matrix getDisponibilidade() {
        return disponibilidade;
    }

    public double[][] getDisponibilidadeArray() {
        return disponibilidade.getArray();
    }

    protected double getDisponibilidadeNoHorario(Horario horario) {
        return this.getDisponibilidadeNoHorario(horario.dia, horario.hora);
    }

    public double getDisponibilidadeNoHorario(Integer dia, Integer hora) {
        return this.disponibilidade.getArray()[hora][dia];
    }

    protected Double getDisponibilidadeNoHorarioDouble(Integer dia, Integer hora) {
        Double disponibilidadeDouble = getDisponibilidadeNoHorario(dia, hora);
        return disponibilidadeDouble;
    }

    private void setDisponibilidadeNoHorario(Horario horario, double valor) {
        this.disponibilidade.getArray()[horario.hora][horario.dia] = valor;
    }

    protected void zeraDisponibilidadeNoHorario(Horario horario) {
        this.setDisponibilidadeNoHorario(horario, 0);
    }

    protected Integer getQuantidadeHoras() {
        return disponibilidade.getArray().length;
    }

    protected Integer getQuantidadeDias() {
        return disponibilidade.getArray()[0].length;
    }

}
