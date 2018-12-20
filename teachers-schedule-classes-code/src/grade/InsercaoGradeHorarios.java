/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.util.List;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class InsercaoGradeHorarios {

    private Professor professor;
    private Disciplina disciplina;
    private List<Horario> horarios;
    private Periodo periodo;

    public InsercaoGradeHorarios() {

    }

    public InsercaoGradeHorarios(Professor professor, Disciplina disciplina, List<Horario> horarios, Periodo periodo) {
        this.professor = professor;
        this.disciplina = disciplina;
        this.horarios = horarios;
        this.periodo = periodo;
    }

    public Professor getProfessor() {
        return professor;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public List<Horario> getHorarios() {
        return horarios;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public void setHorarios(List<Horario> horarios) {
        this.horarios = horarios;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("(Hora,Dia)");
        for (Horario horario : horarios) {
            stringBuilder.append("(")
                    .append(horario.hora)
                    .append(", ")
                    .append(horario.dia)
                    .append(")");
        }
        return " \t professor=" + professor.getNome()
                + ", disciplina=" + disciplina.getCodigo()
                + ", periodo=" + periodo.getNumeroPeriodo()
                + ", horarios=" + stringBuilder.toString();
    }

}
