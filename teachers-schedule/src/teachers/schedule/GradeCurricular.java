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
            this.restricoes[i] = new Restricao(TipoDisponibilidade.classe(0).copy());
        }
    }

    /**
     * Lê os arquivos passados
     *
     * @param pathArquivoProfessor
     * @param pathArquivoDisciplinas
     * @throws IOException
     */
    public void inicializar(String pathArquivoProfessor, String pathArquivoDisciplinas) throws IOException {
        this.LerProfessores(pathArquivoProfessor);
        this.LerDisciplinas(pathArquivoDisciplinas);
        if (!this.disciplinasprofessores()) {
            System.out.println("Há disciplinas sem professores associados!");
        }
        this.calculaRestricoes();
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

            this.InsereDisciplinaNoProfessor(this.disciplinas.get(this.disciplinas.size() - 1), (String) professor.getValue());

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

    private void InsereDisciplinaNoProfessor(Disciplina d, String professorNome) {
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

    private Integer converteNumeroDocumento(MutableCell value) {
        BigDecimal valueBD = (BigDecimal) value.getValue();
        return valueBD.intValue();
    }

    private void zeraRestricoes() {
        for (Restricao r : this.restricoes) {
            r.zeraRestricao();
        }
    }

    private Boolean professorTemDisciplinaNoPeriodo(Professor p, Integer periodo) {
        ArrayList<Disciplina> dprof = p.getDisciplinas();

        for (Disciplina d : dprof) {
            if (d.getPeriodo().equals(periodo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Soma as disponibilidades de todos os professores que tem matéria em cada
     * período, por período
     */
    private void calculaRestricoes() {
        for (Periodo periodo : this.periodos) {
            this.restricoes[periodo.getNumero()-1].zeraRestricao();
            for (Professor p : this.professores) {
                if (this.professorTemDisciplinaNoPeriodo(p, periodo.getNumero())) {
                    // System.out.println(periodo.getNumero() + " " + p.getNome());
                    this.restricoes[periodo.getNumero() - 1].somaRestricao(p.getDisponibilidade());
                }
            }
        }

    }

    /**
     * Verifica se todas as disciplinas tem um professor associado
     *
     * @return
     */
    private Boolean disciplinasprofessores() {
        int qtd = 0;

        for (Professor p : this.professores) {
            qtd += p.getDisciplinas().size();
        }

        return qtd == this.disciplinas.size();
    }

    private Posicao acheDisponibilidade(Integer periodo) {
        double[][] restricao_periodo = this.restricoes[periodo-1].getDisponibilidade().getArray();

        for (int dia = 0; dia < restricao_periodo[0].length; dia++) {
            for (int hora = 0; hora < restricao_periodo.length; hora++) {
                if (restricao_periodo[hora][dia] == 1.0 && restricao_periodo[hora][dia] > 0) {
                    return new Posicao(dia, hora, periodo);
                }
            }
        }
        return null;
    }


    private Professor professorComADisponibilidade(Posicao posicao) {
        ArrayList<Professor> professorAulaPeriodo = new ArrayList<>();
        for (Professor p : this.professores) {
            if (p.professorDaAulaEmPeriodo(posicao.getPeriodo())) {
                professorAulaPeriodo.add(p);
            }
        }

        for (Professor p : professorAulaPeriodo) {
            if(p.getDisponibilidade().getArray()[posicao.getHora()][posicao.getDia()]==1){
                for(Disciplina d: p.getDisciplinas()){
                    if(d.getPeriodo().equals(posicao.getPeriodo())){
                        posicao.setDisciplina(d);
                    }
                }
                return p;
            }
        }
        return null;
    }

    private void insereDisciplina(Posicao p) {
        Disciplina[][] disciplinasP;
        disciplinasP = this.periodos[p.getPeriodo()-1].getDisciplinas();
        disciplinasP[p.getHora()][p.getDia()]=p.getDisciplina();
        p.getDisciplina().setCreditosAlocados(p.getDisciplina().getCreditosAlocados()+1);
       
    }

    public void procuraEInsercao() {
        Integer periodo = 1;
        Posicao insere = this.acheDisponibilidade(periodo);
        Professor professor = this.professorComADisponibilidade(insere);
        this.insereDisciplina(insere);
        professor.getDisponibilidade().getArray()[insere.getHora()][insere.getDia()]=0;
        this.calculaRestricoes();

        //insereDisciplina(insere);
    }

    public void mostrarDisponibilidadesPeriodo() {
        for (int i = 0; i < this.periodos.length; i++) {
            System.out.println((i + 1) + "º Periodo");
            this.restricoes[i].mostrarRestricao();
            System.out.println();
        }
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

}
