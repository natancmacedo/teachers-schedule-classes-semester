package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author natan
 */
public class Periodo {

    private Integer numero;
    private Disciplina disciplinas[][];

    public Periodo(Integer numero) {
        this.numero = numero;
        this.disciplinas = new Disciplina[5][6];

    }

    public Integer getNumero() {
        return numero;
    }

    public Disciplina[][] getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(Disciplina[][] disciplinas) {
        this.disciplinas = disciplinas;
    }

    /**
     * Verifica se um horário está disponível
     *
     * @param dia
     * @param hora
     * @return true ou false
     */
    public Boolean horarioEstaDisponivel(Integer dia, Integer hora) {
        return this.disciplinas[hora][dia] != null;
    }

    /**
     * Verifica se horários em sequencia no mesmo dia estão disponíveis
     *
     * @param dia
     * @param horaInicio
     * @param horaFinal
     * @return
     */
    public Boolean horariosEstaoDisponiveis(Integer dia, Integer horaInicio, Integer horaFinal) {
        for (int i = horaInicio; i <= horaFinal; i++) {
            if (!this.horarioEstaDisponivel(dia, i)) {
                return false;
            }
        }
        return true;
    }

    public Boolean temADisciplinaNoDia(Disciplina disciplina, Integer dia) {
        for (int hora = 0; hora < this.disciplinas.length; hora++) {
            if (this.disciplinas[hora][dia] != null) {
                if (this.disciplinas[hora][dia].equals(disciplina)) {
                    return true;
                }
            }

        }
        return false;
    }

}
