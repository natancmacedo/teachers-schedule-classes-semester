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

    public void begin() throws IloException, Exception {

        cplex = new IloCplex();
        cplex.setParam(IloCplex.BooleanParam.PreInd, false);

        XDH = new IloNumVar[grade.getDisciplinasNaoTotalmenteAlocadas().size()][config.getQuantidadeHorarios()];
        IDHdata = new double[grade.getDisciplinasNaoTotalmenteAlocadas().size()][config.getQuantidadeHorarios()];
        preencheDicionario();
        instanciaXDH();
        instanciaIDHedataPreenche();

        IloLinearNumExpr objetive = cplex.linearNumExpr();

        for (int j = 0; j < grade.getDisciplinasNaoTotalmenteAlocadas().size(); j++) {
            for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                objetive.addTerm(1, XDH[j][k]);
            }
        }

        cplex.addMinimize(objetive);

        //constraints
        //restricao B
        for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
            for (Professor professor : grade.getProfessores()) {
                IloLinearNumExpr restricaoDisciplinasDoProfessorMesmoHorario = cplex.linearNumExpr();
                for (Disciplina disciplina : professor.getDisciplinasNaoTotalmenteAlocadas()) {
                    restricaoDisciplinasDoProfessorMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                }
                cplex.addLe(restricaoDisciplinasDoProfessorMesmoHorario, 1).setName(professor.getNome() + "MesmoHorario");
            }
        }
        //final restricao B

        //restricao C
        for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
            for (Periodo periodo : grade.getPeriodos()) {
                IloLinearNumExpr restricaoDisciplinasDoPeriodoMesmoHorario = cplex.linearNumExpr();
                for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadasEmUmPeriodo(periodo.getNumeroPeriodo())) {
                    restricaoDisciplinasDoPeriodoMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                }
                if (periodo.horarioEstaOcupado(conversor.converteIndiceEmHorario(k))) {
                    cplex.addEq(restricaoDisciplinasDoPeriodoMesmoHorario, 0).setName(periodo.getNumeroPeriodo() + "MesmoHorario");
                }
                cplex.addLe(restricaoDisciplinasDoPeriodoMesmoHorario, 1).setName(periodo.getNumeroPeriodo() + "MesmoHorario");
            }
        }
        //final restricao C

        //restricao f
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            IloLinearNumExpr restricaoAlocacaoTotal = cplex.linearNumExpr();
            for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                restricaoAlocacaoTotal.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
            }
            cplex.addEq(restricaoAlocacaoTotal, disciplina.getCreditosAAlocar()).setName(disciplina.getCodigo() + "AlocacaoTotal");
        }
        //final restricao f

        IloLinearNumExpr restricaoA = cplex.linearNumExpr();
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                restricaoA.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k]);
            }
        }
        cplex.addEq(restricaoA, 0).setName("IX");

        //final restricao A
        //restricao mesmo dia aula
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            for (int dia = 0; dia < config.quantidadeDias; dia++) {
                IloLinearNumExpr restricaoAulaMesmoDia = cplex.linearNumExpr();
                List<Horario> horariosMesmoDia = Horario.montaListaHorarios(dia, 0, config.quantidadeHoras - 1);
                for (Horario horario : horariosMesmoDia) {
                    restricaoAulaMesmoDia.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][conversor.converteHorarioEmIndice(horario)], 1);
                }
                cplex.addLe(restricaoAulaMesmoDia, disciplina.quantidadeHorariosTentativaInsercao()).setName(disciplina.getCodigo() + "SameDay");
            }
        }

        //restricao mesmo dia aula final
        //restricao
        cplex.exportModel("teacher.lp");

        System.out.println(cplex.solve());

        if (cplex.solve()) {
            for (String key : disciplinaHorarios.keySet()) {
                for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                    int indexAcessoDisciplina = disciplinaHorarios.get(key);
                    if (cplex.getValue(XDH[indexAcessoDisciplina][k]) == 1.0) {
                        ArrayList<Horario> horarios = new ArrayList<>();
                        horarios.add(conversor.converteIndiceEmHorario(k));

                        Disciplina disciplina = grade.getDisciplinaPorCodigo(key);
                        Integer numero = disciplina.getPeriodo();
                        Periodo periodo = grade.getPeriodoPorNumero(disciplina.getPeriodo());
                        Professor professor = grade.getProfessorDaDisciplina(disciplina);

                        InsercaoGradeHorarios tentativa = new InsercaoGradeHorarios(professor, disciplina, horarios, periodo);

                        grade.inserirDisciplinaNaGrade(new InsercaoGradeHorarios(professor, disciplina, horarios, periodo));

                    }
                }
            }

            if (grade.aSolucaoEstaPronta()) {
                grade.gerarSaidaArquivo();
            }
        } else {
            throw new Exception("Não foi possível solucionar");
        }
    }

    private void preencheDicionario() {
        Integer i = 0;
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            disciplinaHorarios.put(disciplina.getCodigo(), i++);
        }
    }

    private void instanciaXDH() throws IloException {
        for (Disciplina disciplina : grade.getDisciplinasNaoTotalmenteAlocadas()) {
            for (int k = 0; k < config.getQuantidadeHorarios(); k++) {
                XDH[disciplinaHorarios.get(disciplina.getCodigo())][k] = cplex.numVar(0, 1, IloNumVarType.Int, "x." + disciplina.getCodigo() + "." + k);
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

}
