package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import Jama.Matrix;
import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class Professor {

    private String nome;
    private Matrix disponibilidade;
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();

    public Professor(String nome, Integer classe) {
        this.nome = nome;
        this.disponibilidade = TipoDisponibilidade.classe(classe);
    }

    @Override
    public String toString() {
        return "Professor{" + "nome=" + nome + ", disponibilidade=" + disponibilidade + ", disciplinas=" + disciplinas + '}';
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public Matrix getDisponibilidade() {
        return disponibilidade;
    }

    /**
     * Verifica se um horário está disponível
     *
     * @param dia
     * @param hora
     * @return true ou false
     */
    public Boolean horarioEstaDisponivel(Integer dia, Integer hora) {
        return this.disponibilidade.getArray()[hora][dia] > 0;
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
    
    public void insereDisciplinaDisponibilidadeProfessor(Horario horario){
        this.disponibilidade.getArray()[horario.getHora()][horario.getDia()]=0;
    }

    /**
     * Verifica se o professor tem disciplina no periodo passado
     *
     * @param periodo
     * @return
     */
    public Boolean temDisciplinaNoPeriodo(Integer periodo) {
        for (Disciplina disciplinaIterator : this.disciplinas) {
            if (disciplinaIterator.getPeriodo().equals(periodo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lista as disciplinas de um professor em um periodo dado
     *
     * @param periodo
     * @return
     */
    public ArrayList<Disciplina> disciplinasDoProfessorNoPeriodo(Integer periodo) {
        ArrayList<Disciplina> disciplinasPeriodo = new ArrayList<>();

        for (Disciplina disciplina : this.disciplinas) {
            if (disciplina.getPeriodo().equals(periodo)) {
                disciplinasPeriodo.add(disciplina);
            }
        }
        return disciplinasPeriodo;
    }

    /**
     * Verifica se todas as disciplinas do professor foram alocadas
     *
     * @return
     */
    public Boolean todasAsDisciplinasForamAlocadas() {
        for (Disciplina disciplina : this.disciplinas) {
            if (disciplina.getCreditosAlocados() < disciplina.getCreditos()) {
                return false;
            }
        }
        return true;
    }
}
