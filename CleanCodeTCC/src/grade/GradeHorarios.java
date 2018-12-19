/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class GradeHorarios {

    private Periodo[] periodos;
    private final Integer quantidadePeriodos;

    public final Integer quantidadeDias;
    public final Integer quantidadeHoras;

    private ArrayList<Professor> professores;
    private ArrayList<Disciplina> disciplinas;
    private ArquivoDados arquivo;

    public GradeHorarios(Integer quantidadePeriodos, Integer quantidadeDias, Integer quantidadeHoras) {
        this.quantidadePeriodos = quantidadePeriodos;
        this.quantidadeDias = quantidadeDias;
        this.quantidadeHoras = quantidadeHoras;
        this.periodos = new Periodo[quantidadePeriodos];

        for (int periodo = 0; periodo < quantidadePeriodos; periodo++) {
            this.periodos[periodo] = new Periodo(periodo + 1, quantidadeDias, quantidadeHoras);
        }

        this.professores = new ArrayList<>();
        this.disciplinas = new ArrayList<>();

    }

    public void inicializarProblema(String arquivoDados, String abaDisponibilidade, String abaDisciplina) {

        this.arquivo = new ArquivoDados(arquivoDados, abaDisponibilidade, abaDisciplina, this.quantidadeDias, this.quantidadeHoras);
        arquivo.LeDadosProfessoresEDisciplinas();
        this.professores = arquivo.getProfessores();
        this.disciplinas = arquivo.getDisciplinas();
        this.calcularDisponibilidadesPeriodos();
    }

    public void inserirDisciplinaNaGrade(InsercaoGradeHorarios novaInsercao) {
        if (this.ehPossivelInserirDisciplina(novaInsercao)) {

            ArrayList<Horario> horarios = novaInsercao.getHorarios();

            for (Horario horario : horarios) {
                novaInsercao.getProfessor().setDisciplinaHorarioPessoal(novaInsercao.getDisciplina(), horario);
                novaInsercao.getPeriodo().inserirDisciplinaNoHorario(novaInsercao.getDisciplina(), horario);
                novaInsercao.getDisciplina().addCreditosAlocados();
            }

        }
    }

    public Disciplina getDisciplinaPorCodigo(String codigo) {
        for (Disciplina disciplina : this.disciplinas) {
            if (disciplina.getCodigo().equals(codigo)) {
                return disciplina;
            }
        }

        return null;
    }

    public Boolean ehPossivelInserirDisciplina(InsercaoGradeHorarios tentativa) {
        Boolean disciplinaTotalmenteAlocada = tentativa.getDisciplina().estaTotalmenteAlocada();

        ArrayList<Horario> horarios = tentativa.getHorarios();

        Boolean professorDisponivelNosHorarios = tentativa.getProfessor().estaDisponivelNosHorarios(horarios);
        Boolean periodoTemDisciplinaNosHorarios = tentativa.getPeriodo().horariosEstaoOcupados(tentativa.getHorarios());

        return !disciplinaTotalmenteAlocada && professorDisponivelNosHorarios && !periodoTemDisciplinaNosHorarios;
    }

    private void calcularDisponibilidadesPeriodos() {
        for (Periodo periodo : this.periodos) {
            periodo.calcularDisponibilidadePeriodo(periodo.getNumeroPeriodo(), this.professores);
        }
    }

    public Boolean solucaoEPossivel() {
        for (Professor professor : this.professores) {
            if (professor.quantidadeDeCreditosAAlocar() > professor.quantidadeDeLocaisDePossiveisInsercao()) {
                return false;
            }
        }

        for (Periodo periodo : this.periodos) {
            if (this.quantidadeCreditosAAlocarNoPeriodo(periodo.getNumeroPeriodo()) > periodo.quantidadeLocaisPossiveisInsercao()) {
                return false;
            }
        }

        double somaQuantidadeDeCreditosAlocarPeriodo = 0.0;
        for (Periodo periodo : this.periodos) {
            for (Professor professor : this.professores) {
                if (professor.temAlgumaDisciplinaNoPeriodo(periodo.getNumeroPeriodo())) {
                    somaQuantidadeDeCreditosAlocarPeriodo += professor.quantidadeDeCreditosAAlocarNoPeriodo(periodo.getNumeroPeriodo());
                }
            }

            if (somaQuantidadeDeCreditosAlocarPeriodo > periodo.quantidadeLocaisPossiveisInsercao()) {
                return false;
            }
            somaQuantidadeDeCreditosAlocarPeriodo = 0.0;

        }

        return true;
    }

    public Boolean aSolucaoEstaPronta() {
        return this.getDisciplinasNaoTotalmenteAlocadas().isEmpty();
    }

    private Integer quantidadeCreditosAAlocarNoPeriodo(Integer numeroPeriodo) {
        Integer quantidadeCreditosAAlocar = 0;
        ArrayList<Disciplina> disciplinasDoPeriodo = this.disciplinasDoPeriodo(numeroPeriodo);

        for (Disciplina disciplina : disciplinasDoPeriodo) {
            quantidadeCreditosAAlocar += disciplina.getCreditosAAlocar();
        }

        return quantidadeCreditosAAlocar;
    }

    public ArrayList<Disciplina> disciplinasDoPeriodo(Integer numeroPeriodo) {
        ArrayList<Disciplina> disciplinasDoPeriodo = new ArrayList<>();

        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getPeriodo().equals(numeroPeriodo)) {
                disciplinasDoPeriodo.add(disciplina);
            }
        }

        return disciplinasDoPeriodo;
    }

    public Professor qualProfessorTemADisciplina(Disciplina disciplina) {
        for (Professor professor : this.professores) {
            if (professor.temADisciplina(disciplina)) {
                return professor;
            }
        }

        return null;
    }

    public Periodo getPeriodoPorNumero(Integer numeroPeriodo) {
        for (Periodo periodo : this.periodos) {
            if (periodo.getNumeroPeriodo().equals(numeroPeriodo)) {
                return periodo;
            }
        }
        return null;
    }

    public Periodo[] getPeriodos() {
        return periodos;
    }

    public Integer getQuantidadeDias() {
        return quantidadeDias;
    }

    public ArrayList<Professor> getProfessores() {
        return professores;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public ArrayList<Disciplina> getDisciplinasNaoTotalmenteAlocadas() {
        ArrayList<Disciplina> disciplinasNaoAlocadas = new ArrayList<>();

        for (Disciplina disciplina : this.disciplinas) {
            if (!disciplina.estaTotalmenteAlocada()) {
                disciplinasNaoAlocadas.add(disciplina);
            }
        }

        return disciplinasNaoAlocadas;
    }

    public void GerarSaidaArquivo() throws IOException {
        arquivo.GravaResultadosNoArquivo(this);
    }

    public ArrayList<Disciplina> getDisciplinasNaoTotalmenteAlocadasEmUmPeriodo(Integer numeroPeriodo) {
        ArrayList<Disciplina> disciplinasNaoAlocadas = this.getDisciplinasNaoTotalmenteAlocadas();

        ArrayList<Disciplina> disciplinasNaoAlocadasEmUmPeriodo = new ArrayList<>();

        for (Disciplina disciplina : disciplinasNaoAlocadas) {
            if (disciplina.getPeriodo().equals(numeroPeriodo)) {
                disciplinasNaoAlocadasEmUmPeriodo.add(disciplina);
            }
        }

        return disciplinasNaoAlocadasEmUmPeriodo;
    }

}
