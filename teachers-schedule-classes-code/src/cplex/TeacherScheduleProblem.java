/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CPLEX;

import grade.ConfiguracaoGrade;
import grade.Conversor;
import grade.Disciplina;
import grade.GradeHorarios;
import grade.Horario;
import grade.InsercaoGradeHorarios;
import grade.Periodo;
import grade.Professor;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Natan
 */
public class TeacherScheduleProblem {

    private GradeHorarios grade;
    private ConfiguracaoGrade config;
    private Conversor conversor;
    private Map<String, Integer> disciplinaHorarios = new HashMap<>();

    public TeacherScheduleProblem(GradeHorarios grade) {
        this.grade = grade;
        this.config = grade.config;
        this.conversor = new Conversor(config);
    }

    private IloCplex cplex;
    private IloNumVar XDH[][];
    private double IDHdata[][];

    public void begin() {

        preencheDicionario();
        instanciaCplex();
        setaParametroCplexPreSolucao();
        criaInstanciaVariaveis();
        criaInstanciaIndisponibilidade();
        criaFuncaoObjetivo();
        criaRestricoes();

        exportaModelo();
        exportaResultado();
    }

    private void preencheDicionario() {
        Integer i = 0;
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            disciplinaHorarios.put(disciplina.getCodigo(), i++);
        }
    }

    private void instanciaXDH() {
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                try {
                    XDH[disciplinaHorarios.get(disciplina.getCodigo())][k] = cplex.numVar(0, 1, IloNumVarType.Int, "x." + disciplina.getCodigo() + "." + k);
                } catch (IloException ex) {
                    Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void instanciaIDHedataPreenche() {

        for (Professor professor : grade.getProfessores()) {
            for (Disciplina disciplina : professor.getDisciplinasNaoTotalmenteAlocadas()) {
                for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                    if (professor.getDisponibilidadeProfessor().horarioEstaDisponivel(conversor.converteIndiceEmHorario(k))) {
                        IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k] = 0;
                    } else {
                        IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k] = 1;
                    }
                }
            }
        }
    }

    private void instanciaCplex() {
        try {
            cplex = new IloCplex();
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setaParametroCplexPreSolucao() {
        try {
            cplex.setParam(IloCplex.BooleanParam.PreInd, false);
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criaInstanciaVariaveis() {
        XDH = new IloNumVar[grade.getDisciplinasNaoTotalmenteAlocadas().size()][config.getQuantidadeHorarios()];
        instanciaXDH();
    }

    private void criaInstanciaIndisponibilidade() {
        IDHdata = new double[grade.getDisciplinasNaoTotalmenteAlocadas().size()][config.getQuantidadeHorarios()];
        instanciaIDHedataPreenche();
    }

    private void criaFuncaoObjetivo() {
        try {
            IloLinearNumExpr objetive = cplex.linearNumExpr();

            for (int j = 0; j < grade.getDisciplinasNaoTotalmenteAlocadas().size(); j++) {
                for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                    objetive.addTerm(1, XDH[j][k]);
                }
            }
            cplex.addMinimize(objetive);
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criaRestricoes() {
        criaRestricaoIndisponibilidade();
        criaRestricaoAulaMesmoHorarioPeriodo();
        criaRestricaoAulaMesmoHorarioProfessor();
        criaRestricaoAlocacaoTotalAulas();
        criaRestricaoQuantiadeAulaMaximaPorDia();
    }

    private void criaRestricaoIndisponibilidade() {
        try {
            IloLinearNumExpr restricaoA = cplex.linearNumExpr();
            for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
                for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                    restricaoA.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k]);
                }
            }
            cplex.addEq(restricaoA, 0).setName("Indisponibilidade");
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void criaRestricaoAulaMesmoHorarioPeriodo() {
        for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
            for (Periodo periodo : grade.getPeriodos()) {
                try {
                    IloLinearNumExpr restricaoDisciplinasDoPeriodoMesmoHorario = cplex.linearNumExpr();
                    for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadasEmUmPeriodo(periodo.getNumeroPeriodo())) {
                        restricaoDisciplinasDoPeriodoMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                    }
                    if (periodo.horarioEstaOcupado(conversor.converteIndiceEmHorario(k))) {
                        cplex.addEq(restricaoDisciplinasDoPeriodoMesmoHorario, 0).setName(periodo.getNumeroPeriodo() + "MesmoHorario");
                    }
                    cplex.addLe(restricaoDisciplinasDoPeriodoMesmoHorario, 1).setName(periodo.getNumeroPeriodo() + "MesmoHorario");
                } catch (IloException ex) {
                    Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void criaRestricaoAulaMesmoHorarioProfessor() {
        for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
            for (Professor professor : grade.getProfessores()) {
                try {
                    IloLinearNumExpr restricaoDisciplinasDoProfessorMesmoHorario = cplex.linearNumExpr();
                    for (Disciplina disciplina : professor.getDisciplinasNaoTotalmenteAlocadas()) {
                        restricaoDisciplinasDoProfessorMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                    }
                    cplex.addLe(restricaoDisciplinasDoProfessorMesmoHorario, 1).setName(professor.getNome() + "MesmoHorario");
                } catch (IloException ex) {
                    Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void criaNomeRestricao() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void criaRestricaoAlocacaoTotalAulas() {
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            try {
                IloLinearNumExpr restricaoAlocacaoTotal = cplex.linearNumExpr();
                for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                    restricaoAlocacaoTotal.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                }
                cplex.addEq(restricaoAlocacaoTotal, disciplina.getCreditosAAlocar()).setName(disciplina.getCodigo() + "AlocacaoTotal");
            } catch (IloException ex) {
                Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void criaRestricaoQuantiadeAulaMaximaPorDia() {
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            for (int dia = 0; dia < config.quantidadeDias; dia++) {
                try {
                    IloLinearNumExpr restricaoAulaMesmoDia = cplex.linearNumExpr();
                    List<Horario> horariosMesmoDia = Horario.montaListaHorarios(dia, 0, config.quantidadeHoras - 1);
                    for (Horario horario : horariosMesmoDia) {
                        restricaoAulaMesmoDia.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][conversor.converteHorarioEmIndice(horario)], 1);
                    }
                    Periodo periodo = grade.getPeriodoPorNumero(disciplina.getPeriodo());
                    if (periodo.temADisciplinaNoDia(disciplina, dia)) {
                        cplex.addEq(restricaoAulaMesmoDia, 0).setName(disciplina.getCodigo() + "SameDay");
                    } else {
                        cplex.addLe(restricaoAulaMesmoDia, disciplina.quantidadeHorariosTentativaInsercao()).setName(disciplina.getCodigo() + "SameDay");
                    }
                } catch (IloException ex) {
                    Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void exportaResultado() {
        try {
            if (cplex.solve()) {
                for (Map.Entry<String, Integer> pair : disciplinaHorarios.entrySet()) {
                    for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                        int indexAcessoDisciplina = pair.getValue();
                        if (cplex.getValue(XDH[indexAcessoDisciplina][k]) == 1.0) {
                            ArrayList<Horario> horarios = new ArrayList<>();
                            horarios.add(conversor.converteIndiceEmHorario(k));

                            Disciplina disciplina = grade.getDisciplinaPorCodigo(pair.getKey());
                            Periodo periodo = grade.getPeriodoPorNumero(disciplina.getPeriodo());
                            Professor professor = grade.getProfessorDaDisciplina(disciplina);

                            grade.inserirDisciplinaNaGrade(new InsercaoGradeHorarios(professor, disciplina, horarios, periodo));
                        }
                    }
                }

                if (grade.aSolucaoEstaPronta()) {
                    grade.gerarSaidaArquivo();
                }
            }
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void exportaModelo() {
        try {
            cplex.exportModel("teacher.lp");
        } catch (IloException ex) {
            Logger.getLogger(TeacherScheduleProblem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
