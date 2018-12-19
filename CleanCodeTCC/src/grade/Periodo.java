/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import Jama.Matrix;
import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class Periodo {

    private Integer numeroPeriodo;
    private Disciplina[][] horarioPeriodo;
    private DisponibilidadePeriodo disponibilidadePeriodo;

    public Periodo(Integer numero, Integer quantidadeDias, Integer quantidadeHoras) {
        this.numeroPeriodo = numero;
        this.inicializaDisponibilidadePeriodo(quantidadeDias, quantidadeHoras);
        this.inicializaHorarioPeriodo(quantidadeDias, quantidadeHoras);
    }

    private void inicializaDisponibilidadePeriodo(Integer quantidadeDias, Integer quantidadeHoras) {
        double[][] disponibilidadePeriodoArray = new double[quantidadeHoras][quantidadeDias];
        this.disponibilidadePeriodo = new DisponibilidadePeriodo(new Matrix(disponibilidadePeriodoArray));
    }

    private void inicializaHorarioPeriodo(Integer quantidadeDias, Integer quantidadeHoras) {
        this.horarioPeriodo = new Disciplina[quantidadeHoras][quantidadeDias];
    }

    public Disciplina acessarDisciplinaHorario(Horario horario) {
        return acessarDisciplinaHorario(horario.dia, horario.hora);
    }

    public Disciplina acessarDisciplinaHorario(Integer dia, Integer hora) {
        return horarioPeriodo[hora][dia];
    }

    public double quantidadeProfessoresDisputandoHorario(Integer dia, Integer hora) {
        return this.quantidadeProfessoresDisputandoHorario(new Horario(dia, hora));
    }

    public double quantidadeProfessoresDisputandoHorario(Horario horario) {
        return this.disponibilidadePeriodo.quantidadeProfessoresDisputandoHorario(horario);
    }

    public ArrayList<Professor> professoresDisputandoHorario(Horario horario, ArrayList<Professor> professores) {
        ArrayList<Professor> professoresHorario = new ArrayList<>();
        for (Professor professor : professores) {
            if (professor.temAlgumaDisciplinaNoPeriodo(this.numeroPeriodo) && professor.estaDisponivelNoHorario(horario)) {
                professoresHorario.add(professor);
            }
        }

        return professoresHorario;
    }

    public void inserirDisciplinaNoHorario(Disciplina disciplina, Horario horario) {
        if (!horarioEstaOcupado(horario)) {
            this.horarioPeriodo[horario.hora][horario.dia] = disciplina;
            this.disponibilidadePeriodo.zeraDisponibilidadeNoHorario(horario);
        }
    }

    public Boolean horariosEstaoOcupados(ArrayList<Horario> horarios) {
        for (Horario horario : horarios) {
            if (this.horarioEstaOcupado(horario)) {
                return true;
            }
        }

        return false;
    }

    public Boolean horarioEstaOcupado(Horario horario) {
        return this.horarioPeriodo[horario.hora][horario.dia] != null;
    }

    public Disciplina getDisciplinaNoHorario(Horario horario) {
        return this.horarioPeriodo[horario.hora][horario.dia];
    }

    public void calcularDisponibilidadePeriodo(Integer numeroPeriodo, ArrayList<Professor> professores) {
        this.disponibilidadePeriodo.zerarDisponibilidade();

        for (Professor professor : professores) {
            if (professor.temAlgumaDisciplinaNoPeriodo(numeroPeriodo)) {
                this.disponibilidadePeriodo.somaDisponbilidade(professor.getDisponibilidadeProfessor().getDisponibilidade());
            }
        }
    }

    public Integer getNumeroPeriodo() {
        return this.numeroPeriodo;
    }

    public Integer quantidadeLocaisPossiveisInsercao() {
        return this.disponibilidadePeriodo.quantidadeDisponibilidadesDiferentesZero();
    }

    public Boolean temADisciplinaNoDia(Disciplina disciplina, Integer dia) {
        for (int hora = 0; hora < this.disponibilidadePeriodo.getQuantidadeHoras(); hora++) {
            if (this.horarioPeriodo[hora][dia] != null && disciplina.equals(this.horarioPeriodo[hora][dia])) {
                return true;
            }
        }

        return false;
    }

    public DisponibilidadePeriodo getDisponibilidadePeriodo() {
        return this.disponibilidadePeriodo;
    }

    public ArrayList<Horario> getHorariosDisponiveis(Integer quantidadeHoras) {

        ArrayList<Horario> horariosDisponiveis = new ArrayList<>();

        for (int dia = 0; dia < this.getDisponibilidadePeriodo().getQuantidadeDias(); dia++) {
            for (int hora = 0; hora < this.getDisponibilidadePeriodo().getQuantidadeHoras() - quantidadeHoras + 1; hora++) {
                if (this.horarioEstaOcupado(new Horario(dia, hora))) {
                    continue;
                }

                ArrayList<Horario> tentativaHorarios = Horario.montaListaHorarios(dia, hora, hora + quantidadeHoras - 1);

                if (!this.horariosEstaoOcupados(tentativaHorarios)) {
                    return tentativaHorarios;
                }
            }
        }

        return horariosDisponiveis;
    }

    public Disciplina[][] getHorarioPeriodo() {
        return this.horarioPeriodo;
    }

}
