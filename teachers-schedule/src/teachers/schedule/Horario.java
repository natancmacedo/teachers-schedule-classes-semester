/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class Horario {

    private Integer dia;
    private Integer hora;
    private Integer periodo;
    private Disciplina disciplina;

    public Horario(Integer dia, Integer hora, Integer periodo) {
        this.dia = dia;
        this.hora = hora;
        this.periodo = periodo;
    }

    public Horario(Integer dia, Integer hora, Integer periodo, Disciplina disciplina) {
        this.dia = dia;
        this.hora = hora;
        this.periodo = periodo;
        this.disciplina = disciplina;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Disciplina getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(Disciplina disciplina) {
        this.disciplina = disciplina;
    }

    public static ArrayList<Horario> montaHorario(Integer periodo, Integer dia, Integer horaInicio, Integer horaFinal, Disciplina disciplina) {
        ArrayList<Horario> horarios = new ArrayList<>();
        for (int i = horaInicio; i <= horaFinal; i++) {
            horarios.add(new Horario(dia, i, periodo,disciplina));
        }
        return horarios;
    }

}
