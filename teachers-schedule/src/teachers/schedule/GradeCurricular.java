package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Natan Macedo <natancmacedo@gmail.com>
 */
public class GradeCurricular {

    private ArrayList<Professor> professores = new ArrayList<>();
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();
    private Integer qtdperiodos;
    private Periodo[] periodos;
    private Restricao[] restricoes;


    public GradeCurricular(Integer qtdperiodos) {
        this.qtdperiodos = qtdperiodos;
        this.periodos = new Periodo[this.qtdperiodos];
        this.restricoes = new Restricao[qtdperiodos];
        for (int i = 0; i < this.qtdperiodos; i++) {
            this.periodos[i] = new Periodo(i + 1);
            this.restricoes[i] = new Restricao();
        }
    }

    /**
     * Lê os arquivos passados
     * @param pathArquivoProfessor
     * @param pathArquivoDisciplinas
     * @throws IOException
     */
    public void inicializar(String pathArquivoProfessor, String pathArquivoDisciplinas) throws IOException {
        this.LerProfessores(pathArquivoProfessor);
        this.LerDisciplinas(pathArquivoDisciplinas);
    }

    private void LerDisciplinas(String fileName) throws IOException {
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

            this.disciplinas.add(new Disciplina((String) codigo.getValue(), (String) nome.getValue(),
                    this.converteNumeroDocumento(creditos), this.converteNumeroDocumento(periodo)));

            this.InsereProfessorDisciplina(this.disciplinas.get(this.disciplinas.size() - 1), (String) professor.getValue());

        }
    }

    private void LerProfessores(String fileName) throws IOException {
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
            Integer classe1 = this.converteNumeroDocumento(classe);
            this.professores.add(new Professor(nome, classe1));

        }
    }

    public void InsereProfessorDisciplina(Disciplina d, String professorNome) {
        int qtdnotfound = 0;
        for (Professor professor : this.professores) {
            if (professor.getNome().equals(professorNome)) {
                professor.getDisciplinas().add(d);
            } else {
                qtdnotfound++;
            }
        }
        if (qtdnotfound >= this.professores.size()) {
            System.out.println(d);
        }
    }

    public Integer converteNumeroDocumento(MutableCell value) {
        BigDecimal valueBD = (BigDecimal) value.getValue();
        return valueBD.intValue();
    }

    public void mostrarProfessores() {
        for (int i = 0; i < this.professores.size(); i++) {
            System.out.println(this.professores.get(i));
        }
    }

    public void mostrarDisciplinas() {
        for (Disciplina d : this.disciplinas) {
            System.out.println(d);
        }
    }

    private Integer[][] somaMatriz(Integer a[][], Integer b[][]) {
        Integer c[][] = new Integer[a.length][a[0].length];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                c[i][j] = a[i][j] + b[i][j];
            }
        }

        return c;
    }

    public Boolean disciplinasprofessores() {
        int qtd = 0;

        for (Professor p : this.professores) {
            qtd += p.getDisciplinas().size();
        }

        return qtd == this.disciplinas.size();
    }

    public Boolean professorTemDisciplinaNoPeriodo(Professor p, Integer periodo) {
        ArrayList<Disciplina> dprof = p.getDisciplinas();

        for (Disciplina d : dprof) {
            if (d.getPeriodo().equals(periodo)) {
                return true;
            }
        }
        return false;
    }

    public void disponibilidadesPeriodo() {

        for (Periodo periodo : this.periodos) {
            for (Professor p : this.professores) {
                if (this.professorTemDisciplinaNoPeriodo(p, periodo.getNumero())) {
                    System.out.println(periodo.getNumero() + " " + p.getNome());
                    this.restricoes[periodo.getNumero()].somaRestricao(p.getDisponibilidade());
                  //  double c[][] = this.somaMatriz(p.getDisponibilidade(), periodo.getDisponibilidade());
                    //periodo.setDisponibilidade(c);
                }

            }
            //System.out.println(Arrays.deepToString(periodo.getDisponibilidade())+periodo.getNumero()+" ");
            System.out.println();
        }

    }

   /* private void disponibilidadesPeriodoMostrar(int p) {
        double disponibilidade[][] = this.periodos[p].getDisponibilidade();
        System.out.println("Seg  Ter Quar  Qui  Sex  Sab");
        for (int i = 0; i < disponibilidade.length; i++) {
            for (int j = 0; j < disponibilidade[0].length; j++) {
                System.out.print(disponibilidade[i][j] + "    ");
            }
            System.out.println();
        }
    }*/

    public void mostrarDisponibilidadesPeriodo() {
        for (int i = 0; i < this.periodos.length; i++) {
            System.out.println((i + 1) + "º Periodo");
       //     this.disponibilidadesPeriodoMostrar(i);
            System.out.println();
        }
    }



}
