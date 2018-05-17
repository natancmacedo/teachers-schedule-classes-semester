package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Jama.Matrix;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Natan Macedoo <natancmacedo@gmail.com>
 */
public class GradeCurricular {

    public static ArrayList<Professor> professores = new ArrayList<>();
    public static ArrayList<Disciplina> disciplinas = new ArrayList<>();
    public static Integer qtdperiodos;
    public static Periodo[] periodos;

    public static void LerDisciplinas(String fileName) throws IOException {
        Sheet sheet;
        File file = new File(fileName);
        sheet = SpreadSheet.createFromFile(file).getFirstSheet();
        MutableCell codigo;
        MutableCell nome;
        MutableCell creditos;
        MutableCell periodo;
        MutableCell professor;

        int row = sheet.getRowCount();
        int column = sheet.getColumnCount();

        for (int i = 1; i < row; i++) {

            nome = sheet.getCellAt(0, i);
            codigo = sheet.getCellAt(1, i);
            periodo = sheet.getCellAt(2, i);
            creditos = sheet.getCellAt(3, i);
            professor = sheet.getCellAt(4, i);

            if (codigo.getValue() == "") {
                break;
            }

            GradeCurricular.disciplinas.add(new Disciplina((String) codigo.getValue(), (String) nome.getValue(),
                    GradeCurricular.converteNumeroDocumento(creditos), GradeCurricular.converteNumeroDocumento(periodo)));

            GradeCurricular.InsereProfessorDisciplina(GradeCurricular.disciplinas.get(GradeCurricular.disciplinas.size() - 1), (String) professor.getValue());

        }
    }

    public static void InsereProfessorDisciplina(Disciplina d, String professorNome) {
        int qtdnotfound = 0;
        for (Professor professor : GradeCurricular.professores) {
            if (professor.getNome().equals(professorNome)) {
                professor.getDisciplinas().add(d);
            } else {
                qtdnotfound++;
            }
        }
        if (qtdnotfound >= GradeCurricular.professores.size()) {
            System.out.println(d);
        }
    }

    public static Integer converteNumeroDocumento(MutableCell value) {
        BigDecimal valueBD = (BigDecimal) value.getValue();
        return valueBD.intValue();
    }

    public static void LerProfessores(String fileName) throws IOException {
        Sheet sheet;
        File file = new File(fileName);
        sheet = SpreadSheet.createFromFile(file).getFirstSheet();
        MutableCell professor = null;
        MutableCell classe = null;

        int row = sheet.getRowCount();
        int column = sheet.getColumnCount();

        for (int i = 1; i < row; i++) {
            professor = sheet.getCellAt(0, i);
            classe = sheet.getCellAt(1, i);

            if (professor.getValue() == "") {
                break;
            }

            // System.out.println(classe.getValueType());
            String nome = (String) professor.getValue();
            Integer classe1 = GradeCurricular.converteNumeroDocumento(classe);
            GradeCurricular.professores.add(new Professor(nome, classe1));

        }
    }

    public static void mostrarProfessores() {
        for (int i = 0; i < GradeCurricular.professores.size(); i++) {
            System.out.println(GradeCurricular.professores.get(i));
        }
    }

    public static void mostrarDisciplinas() {
        for (Disciplina d : GradeCurricular.disciplinas) {
            System.out.println(d);
        }
    }

    public static Integer[][] somaMatriz(Integer a[][], Integer b[][]) {
        Integer c[][] = new Integer[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }

        return c;
    }
    


    public static Boolean disciplinasprofessores() {
        int qtd = 0;

        for (Professor p : GradeCurricular.professores) {
            qtd += p.getDisciplinas().size();
        }

        return qtd == GradeCurricular.disciplinas.size();
    }

    public static Boolean professorTemDisciplinaNoPeriodo(Professor p, Integer periodo) {
        ArrayList<Disciplina> dprof = p.getDisciplinas();

        for (Disciplina d : dprof) {
            if(d.getPeriodo().equals(periodo)){
                return true;
            }
        }
        return false;
    }

    public static void disponibilidadesPeriodo() {
        
        for (Periodo periodo : GradeCurricular.periodos) {
            for (Professor p : GradeCurricular.professores) {
               if (GradeCurricular.professorTemDisciplinaNoPeriodo(p, periodo.getNumero())) {
                    System.out.println(periodo.getNumero() + " " +p.getNome());
                    Integer c[][] = GradeCurricular.somaMatriz(p.getDisponibilidade(), periodo.getDisponibilidade());
                    periodo.setDisponibilidade(c);
                }
                
            }
            //System.out.println(Arrays.deepToString(periodo.getDisponibilidade())+periodo.getNumero()+" ");
            System.out.println();
        }

        
    }
    
    private static void disponibilidadesPeriodoMostrar(int p){
       Integer disponibilidade[][] = GradeCurricular.periodos[p].getDisponibilidade();
        System.out.println("Seg  Ter Quar  Qui  Sex  Sab");
        for (int i = 0; i < disponibilidade.length; i++) {
            for(int j=0;j< disponibilidade[0].length;j++){
                System.out.print(disponibilidade[i][j]+"    ");
            }
            System.out.println();
        }
    }
    
    public static void mostrarDisponibilidadesPeriodo(){
        for(int i=0;i<GradeCurricular.periodos.length;i++){
            System.out.println((i+1) + "º Periodo");
            GradeCurricular.disponibilidadesPeriodoMostrar(i);
            System.out.println();
        }
    }

    public static void periodos(int n) {
        GradeCurricular.periodos = new Periodo[n];
        for (int i = 0; i < n; i++) {
            GradeCurricular.periodos[i] = new Periodo(i + 1);
        }
    }

}
