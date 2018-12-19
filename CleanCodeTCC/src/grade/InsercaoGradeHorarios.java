/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class InsercaoGradeHorarios {

    private Professor professor;
    private Disciplina disciplina;
    private ArrayList<Horario> horarios;
    private Periodo periodo;

    public InsercaoGradeHorarios() {

    }

    public InsercaoGradeHorarios(Professor professor, Disciplina disciplina, ArrayList<Horario> horarios, Periodo periodo) {
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

    public ArrayList<Horario> getHorarios() {
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

    public void setHorarios(ArrayList<Horario> horarios) {
        this.horarios = horarios;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    @Override
    public String toString() {

        String horariosStr = "(Hora,Dia)";

        for (Horario horario : horarios) {
            horariosStr += "(" + horario.hora + ", " + horario.dia + ")";
        }
        return " \t professor=" + professor.getNome() + ", disciplina=" + disciplina.getCodigo() + ", periodo=" + periodo.getNumeroPeriodo() + ", horarios=" + horariosStr;
    }

}
