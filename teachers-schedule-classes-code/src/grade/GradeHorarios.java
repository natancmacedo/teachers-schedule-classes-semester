/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import cplex.NaoFoiPossivelInserirGradeException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author natan
 */
public class GradeHorarios {

    public ConfiguracaoGrade config;
    private Periodo[] periodos;

    private ArrayList<Professor> professores;
    private ArrayList<Disciplina> disciplinas;
    private ArquivoDados arquivo;

    public GradeHorarios(ConfiguracaoGrade config) {
        this.config = config;
        this.periodos = new Periodo[config.quantidadePeriodos];

        for (int periodo = 0; periodo < config.quantidadePeriodos; periodo++) {
            this.periodos[periodo] = new Periodo(periodo + 1, config);
        }

        this.professores = new ArrayList<>();
        this.disciplinas = new ArrayList<>();
    }

    public void preencherDadosProblema(InformacoesArquivo informacoesArquivo) {

        this.arquivo = new ArquivoDados(informacoesArquivo, config);
        arquivo.leDadosProfessoresEDisciplinas();
        this.professores = arquivo.getProfessores();
        this.disciplinas = arquivo.getDisciplinas();

        insereDisciplinasDoArquivo();

        this.calcularDisponibilidadesPeriodos();
    }

    private void insereDisciplinasDoArquivo() {
        List<DadosInsercaoDisciplinaArquivo> disciplinasInserir = arquivo.getInsercaoDiretaDoArquivo();

        for (DadosInsercaoDisciplinaArquivo dadosDisciplina : disciplinasInserir) {
            InsercaoGradeHorarios novaInsercao = converterDadosArquivoParaInsercao(dadosDisciplina);
            this.inserirDisciplinaNaGrade(novaInsercao);
        }
    }

    private InsercaoGradeHorarios converterDadosArquivoParaInsercao(DadosInsercaoDisciplinaArquivo dadosDisciplina) {
        InsercaoGradeHorarios novaInsercao = new InsercaoGradeHorarios();

        Disciplina disciplina = this.getDisciplinaPorCodigo(dadosDisciplina.codigoDisciplina);
        Periodo periodo = this.getPeriodoPorNumero(disciplina.getPeriodo());
        Professor professor = this.getProfessorDaDisciplina(disciplina);

        ArrayList<Horario> horarios = new ArrayList<>();
        horarios.add(dadosDisciplina.horario);

        novaInsercao.setDisciplina(disciplina);
        novaInsercao.setPeriodo(periodo);
        novaInsercao.setHorarios(horarios);
        novaInsercao.setProfessor(professor);

        return novaInsercao;
    }

    public void inserirDisciplinaNaGrade(InsercaoGradeHorarios novaInsercao) {
        if (this.ehPossivelInserirDisciplina(novaInsercao)) {

            List<Horario> horarios = novaInsercao.getHorarios();

            for (Horario horario : horarios) {
                novaInsercao.getProfessor().setDisciplinaHorarioPessoal(novaInsercao.getDisciplina(), horario);
                novaInsercao.getPeriodo().inserirDisciplinaNoHorario(novaInsercao.getDisciplina(), horario);
                novaInsercao.getDisciplina().addCreditosAlocados();
            }

        } else {
            try {
                throw new NaoFoiPossivelInserirGradeException("Não é possível inserir " + novaInsercao);
            } catch (NaoFoiPossivelInserirGradeException ex) {
                Logger.getLogger(GradeHorarios.class.getName()).log(Level.SEVERE, null, ex);
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

        List<Horario> horarios = tentativa.getHorarios();

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
        List<Disciplina> disciplinasDoPeriodo = this.disciplinasDoPeriodo(numeroPeriodo);

        for (Disciplina disciplina : disciplinasDoPeriodo) {
            quantidadeCreditosAAlocar += disciplina.getCreditosAAlocar();
        }

        return quantidadeCreditosAAlocar;
    }

    public List<Disciplina> disciplinasDoPeriodo(Integer numeroPeriodo) {
        ArrayList<Disciplina> disciplinasDoPeriodo = new ArrayList<>();

        for (Disciplina disciplina : disciplinas) {
            if (disciplina.getPeriodo().equals(numeroPeriodo)) {
                disciplinasDoPeriodo.add(disciplina);
            }
        }

        return disciplinasDoPeriodo;
    }

    public Professor getProfessorDaDisciplina(Disciplina disciplina) {
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

    public List<Professor> getProfessores() {
        return professores;
    }

    public List<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public List<Disciplina> getDisciplinasNaoTotalmenteAlocadas() {
        ArrayList<Disciplina> disciplinasNaoAlocadas = new ArrayList<>();

        for (Disciplina disciplina : this.disciplinas) {
            if (!disciplina.estaTotalmenteAlocada()) {
                disciplinasNaoAlocadas.add(disciplina);
            }
        }

        return disciplinasNaoAlocadas;
    }

    public void gerarSaidaArquivo() {
        arquivo.gravaResultadosNoArquivo(this);
    }

    public List<Disciplina> getDisciplinasNaoTotalmenteAlocadasEmUmPeriodo(Integer numeroPeriodo) {
        List<Disciplina> disciplinasNaoAlocadas = this.getDisciplinasNaoTotalmenteAlocadas();

        List<Disciplina> disciplinasNaoAlocadasEmUmPeriodo = new ArrayList<>();

        for (Disciplina disciplina : disciplinasNaoAlocadas) {
            if (disciplina.getPeriodo().equals(numeroPeriodo)) {
                disciplinasNaoAlocadasEmUmPeriodo.add(disciplina);
            }
        }

        return disciplinasNaoAlocadasEmUmPeriodo;
    }

}
