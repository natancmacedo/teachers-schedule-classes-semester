/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CPLEX;

import grade.Disciplina;
import grade.GradeHorarios;
import grade.Horario;
import grade.InsercaoGradeHorarios;
import grade.Periodo;
import grade.Professor;
import grade.Util;
import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloNumVarType;
import ilog.cplex.IloCplex;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Natan
 */
public class TeacherScheduleProblem {

    GradeHorarios grade;
    Map<String, Integer> disciplinaHorarios = new HashMap<>();

    public void main(GradeHorarios grade) throws IloException, Exception {
        this.grade = grade;
        begin();
    }
    private IloCplex cplex;
    private IloNumVar XDH[][];
    private double IDHdata[][];

    private void begin() throws IloException, Exception {

        cplex = new IloCplex();
        cplex.setParam(IloCplex.BooleanParam.PreInd, false);

        XDH = new IloNumVar[grade.getDisciplinas().size()][grade.quantidadeDias * grade.quantidadeHoras];
        IDHdata = new double[grade.getDisciplinas().size()][grade.quantidadeDias * grade.quantidadeHoras];
        preencheDicionario();
        instanciaXDH(XDH);
        instanciaIDHedataPreenche(IDHdata);

        IloLinearNumExpr objetive = cplex.linearNumExpr();

        for (int j = 0; j < grade.getDisciplinas().size(); j++) {
            for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                objetive.addTerm(1, XDH[j][k]);
            }
        }

        cplex.addMinimize(objetive);

        //constraints
        //restricao n v u
        int n, v, u, lb, ub;

        for (n = 0; n < 4; n++) {
            for (v = 1; v < 6; v++) {
                u = 1 + (v - 1) * n;
                lb = u;
                ub = u + 2 * n;
                System.out.printf("n: %d v:%d lb: %d ub: %d\n", n, v, lb, ub);
            }
        }

        //restricao B
        for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
            for (Professor professor : grade.getProfessores()) {
                IloLinearNumExpr restricaoDisciplinasDoProfessorMesmoHorario = cplex.linearNumExpr();
                for (Disciplina disciplina : professor.getDisciplinas()) {
                    restricaoDisciplinasDoProfessorMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                }
                cplex.addLe(restricaoDisciplinasDoProfessorMesmoHorario, 1).setName(professor.getNome() + "MesmoHorario");
            }
        }
        //final restricao B

        //restricao C
        for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
            for (Periodo periodo : grade.getPeriodos()) {
                IloLinearNumExpr restricaoDisciplinasDoPeriodoMesmoHorario = cplex.linearNumExpr();
                for (Disciplina disciplina : grade.disciplinasDoPeriodo(periodo.getNumeroPeriodo())) {
                    restricaoDisciplinasDoPeriodoMesmoHorario.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
                }
                cplex.addLe(restricaoDisciplinasDoPeriodoMesmoHorario, 1).setName(periodo.getNumeroPeriodo() + "MesmoHorario");
            }
        }
        //final restricao C

        //restricao f
        for (Disciplina disciplina : grade.getDisciplinas()) {
            IloLinearNumExpr restricaoAlocacaoTotal = cplex.linearNumExpr();
            for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                restricaoAlocacaoTotal.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], 1);
            }
            cplex.addEq(restricaoAlocacaoTotal, disciplina.getCreditos()).setName(disciplina.getCodigo() + "AlocacaoTotal");
        }
        //final restricao f

        IloLinearNumExpr restricaoA = cplex.linearNumExpr();
        for (Disciplina disciplina : grade.getDisciplinas()) {
            for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                restricaoA.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][k], IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k]);
            }
        }
        cplex.addEq(restricaoA, 0).setName("IX");

        //final restricao A
        //restricao mesmo dia aula
        for (Disciplina disciplina : grade.getDisciplinas()) {

            for (int dia = 0; dia < grade.quantidadeDias; dia++) {
                IloLinearNumExpr restricaoAulaMesmoDia = cplex.linearNumExpr();
                ArrayList<Horario> horariosMesmoDia = Horario.montaListaHorarios(dia, 0, grade.quantidadeHoras - 1);
                for (Horario horario : horariosMesmoDia) {
                    restricaoAulaMesmoDia.addTerm(XDH[disciplinaHorarios.get(disciplina.getCodigo())][Util.transformaHorarioEmNumero(horario, grade.quantidadeDias)], 1);
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
                if (key.equals("COM043")) {
                    System.out.println("pare");
                }
                for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                    int indexAcessoDisciplina = disciplinaHorarios.get(key);
                    if (cplex.getValue(XDH[indexAcessoDisciplina][k]) == 1.0) {
                        ArrayList<Horario> horarios = new ArrayList<>();
                        horarios.add(Util.transformaNumeroEmHorario(k, grade.quantidadeDias));

                        Disciplina disciplina = grade.getDisciplinaPorCodigo(key);
                        Integer numero = disciplina.getPeriodo();
                        Periodo periodo = grade.getPeriodoPorNumero(disciplina.getPeriodo());
                        Professor professor = grade.qualProfessorTemADisciplina(disciplina);

                        InsercaoGradeHorarios tentativa = new InsercaoGradeHorarios(professor, disciplina, horarios, periodo);
                        if (grade.ehPossivelInserirDisciplina(tentativa)) {
                            grade.inserirDisciplinaNaGrade(new InsercaoGradeHorarios(professor, disciplina, horarios, periodo));
                        } else {
                            System.err.println("Não é possivel inserir" + tentativa);
                        }
                    }
                }
            }

            if (grade.aSolucaoEstaPronta()) {
                grade.GerarSaidaArquivo();
            }
        } else {
            throw new Exception("Não foi possível solucionar");
        }
    }

    private void preencheDicionario() {
        Integer i = 0;
        for (Disciplina disciplina : grade.getDisciplinas()) {
            disciplinaHorarios.put(disciplina.getCodigo(), i++);
        }
    }

    private void instanciaXDH(IloNumVar[][] XDH) throws IloException {
        for (Disciplina disciplina : grade.getDisciplinas()) {
            for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                XDH[disciplinaHorarios.get(disciplina.getCodigo())][k] = cplex.numVar(0, 1, IloNumVarType.Int, "x." + disciplina.getCodigo() + "." + k);
            }
        }
    }

    private void instanciaIDHedataPreenche(double[][] IDHdata) {

        for (Professor professor : grade.getProfessores()) {
            for (Disciplina disciplina : professor.getDisciplinas()) {
                for (int k = 0; k < grade.quantidadeDias * grade.quantidadeHoras; k++) {
                    if (professor.getDisponibilidadeProfessor().horarioEstaDisponivel(Util.transformaNumeroEmHorario(k, grade.quantidadeDias))) {
                        IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k] = 0;
                    } else {
                        IDHdata[disciplinaHorarios.get(disciplina.getCodigo())][k] = 1;
                    }
                }
            }
        }
    }

}
