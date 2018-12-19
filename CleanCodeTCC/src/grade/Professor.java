package grade;

import java.util.ArrayList;

public class Professor {

    private String nome;
    private DisponibilidadeProfessor disponibilidade;
    private ArrayList<Disciplina> disciplinas;
    private Disciplina[][] horarioPessoal;

    public Professor(String nome, DisponibilidadeProfessor disponibilidade,
            Integer quantidadeDias, Integer quantidadeHoras) {
        this.nome = nome;
        this.disponibilidade = disponibilidade;
        this.horarioPessoal = new Disciplina[quantidadeHoras][quantidadeDias];
        this.disciplinas = new ArrayList<>();

    }

    public void addDisciplina(Disciplina disciplina) {
        this.disciplinas.add(disciplina);
    }

    public String getNome() {
        return nome;
    }

    public Boolean temADisciplina(Disciplina disciplina) {
        return this.disciplinas.contains(disciplina);
    }

    public Boolean temAlgumaDisciplinaNoPeriodo(Integer numeroPeriodo) {
        for (Disciplina disciplinaIterador : this.disciplinas) {
            if (disciplinaIterador.getPeriodo().equals(numeroPeriodo)) {
                return true;
            }
        }

        return false;
    }

    public void setDisciplinaHorarioPessoal(Disciplina disciplina, Horario horario) {
        if (this.temADisciplina(disciplina) && this.estaDisponivelNoHorario(horario)) {
            this.horarioPessoal[horario.hora][horario.dia] = disciplina;
            this.disponibilidade.zeraDisponibilidadeNoHorario(horario);
        } else {
            System.err.println("Profesor não tem a disciplina ou o horário não está disponível");
        }
    }

    public Boolean estaDisponivelNosHorarios(ArrayList<Horario> horarios) {
        for (Horario horario : horarios) {
            if (!this.estaDisponivelNoHorario(horario)) {
                return false;
            }
        }

        return true;
    }

    public Boolean estaDisponivelNoHorario(Horario horario) {
        return this.getDisciplinaHorarioPessoal(horario) == null && this.disponibilidade.horarioEstaDisponivel(horario);
    }

    private Disciplina getDisciplinaHorarioPessoal(Horario horario) {
        return this.horarioPessoal[horario.hora][horario.dia];
    }

    public DisponibilidadeProfessor getDisponibilidadeProfessor() {
        return this.disponibilidade;
    }

    public double quantidadeDeCreditosAAlocar() {
        double quantidadeCreditosAAlocar = 0.0;
        for (Disciplina disciplina : this.disciplinas) {
            quantidadeCreditosAAlocar += disciplina.getCreditosAAlocar();
        }

        return quantidadeCreditosAAlocar;
    }

    public double quantidadeDeCreditosAAlocarNoPeriodo(Integer numeroPeriodo) {
        double quantidadeCreditosAAlocarNoPeriodo = 0.0;
        for (Disciplina disciplina : this.disciplinas) {
            if (disciplina.getPeriodo().equals(numeroPeriodo)) {
                quantidadeCreditosAAlocarNoPeriodo += disciplina.getCreditosAAlocar();
            }
        }

        return quantidadeCreditosAAlocarNoPeriodo;
    }

    public double quantidadeDeLocaisDePossiveisInsercao() {
        return this.disponibilidade.somaDasDisponibilidade();
    }

    public Disciplina getDisciplinaNaoTotalmenteAlocadaEmUmPeriodo(Integer numeroPeriodo) {
        for (Disciplina disciplina : disciplinas) {
            if (!disciplina.estaTotalmenteAlocada() && disciplina.getPeriodo().equals(numeroPeriodo)) {
                return disciplina;
            }
        }

        return null;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public Disciplina[][] getHorarioPessoal() {
        return this.horarioPessoal;
    }
}
