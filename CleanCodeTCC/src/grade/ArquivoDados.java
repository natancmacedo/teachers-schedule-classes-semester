/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Natan
 */
public class ArquivoDados {

    private String arquivoDados;

    private String abaDisponibilidade;
    private String abaDisciplinas;

    private final int QUANTIDADE_DIAS;
    private final int QUANTIDADE_HORAS;

    private final ArrayList<Professor> professores = new ArrayList<>();
    private final ArrayList<Disciplina> disciplinas = new ArrayList<>();

    private final String[] DIAS_SEMANA = {"SEG", "TER", "QUAR", "QUI", "SEXT", "SAB"};

    public ArquivoDados(String arquivoDados, String abaDisponibilidade, String abaDisciplinas, int quantidadeDias, int quantidadeHoras) {
        this.arquivoDados = arquivoDados;
        this.abaDisponibilidade = abaDisponibilidade;
        this.abaDisciplinas = abaDisciplinas;
        this.QUANTIDADE_DIAS = quantidadeDias;
        this.QUANTIDADE_HORAS = quantidadeHoras;
    }

    public void GravaResultadosNoArquivo(GradeHorarios grade) throws IOException {
        //GravaResultadoGeral();
        GravaResultadoPorProfessor(grade);
        GravaResultadoPorPeriodo(grade);
    }

    private void GravaResultadoGeral() {
        File arquivo = new File(this.arquivoDados);
        SpreadSheet planilha;
        Sheet planilhaDisponibilidade = null;
        try {
            planilha = SpreadSheet.createFromFile(arquivo);
            planilhaDisponibilidade = planilha.getSheet("ResultadosGerais");
        } catch (IOException ex) {
            System.out.printf("Arquivo %s não foi encontrado", arquivoDados);
            System.exit(0);
        }

    }

    private void GravaResultadoPorProfessor(GradeHorarios grade) throws IOException {

        int linha = 0;
        //escrever dias da semana

        for (Professor professor : grade.getProfessores()) {
            File arquivo = new File(this.arquivoDados);
            SpreadSheet planilha = null;
            Sheet resultadoProfessor = null;
            try {
                planilha = SpreadSheet.createFromFile(arquivo);
                resultadoProfessor = planilha.getSheet("ResultadoProfessor");
            } catch (IOException ex) {
                System.out.printf("Arquivo %s não foi encontrado", arquivoDados);
                System.exit(0);
            }

            resultadoProfessor.getCellAt(0, linha).setValue(professor.getNome());
            if (professor.getNome().equals("Inglês")) {
                System.out.println("pare");
            }
            for (int dia = 0; dia < DIAS_SEMANA.length; dia++) {
                MutableCell cell = resultadoProfessor.getCellAt(dia + 1, linha);
                cell.setValue(DIAS_SEMANA[dia]);
            }

            linha++;

            Disciplina[][] horarioProfessor = professor.getHorarioPessoal();
            DisponibilidadeProfessor disponibilidadeProfessor = professor.getDisponibilidadeProfessor();

            for (int hora = 0; hora < this.QUANTIDADE_HORAS; hora++) {
                for (int dia = 0; dia < this.QUANTIDADE_DIAS; dia++) {
                    if (horarioProfessor[hora][dia] != null) {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(horarioProfessor[hora][dia].getCodigo());
                    } else {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(disponibilidadeProfessor.getDisponibilidadeNoHorario(dia, hora));
                    }
                }

            }

            linha = linha + this.QUANTIDADE_HORAS + 2;
            File outputFile = new File(this.arquivoDados);
            planilha.saveAs(outputFile);
        }

    }

    private void GravaResultadoPorPeriodo(GradeHorarios grade) throws IOException {
        File arquivo = new File(this.arquivoDados);
        SpreadSheet planilha = null;
        Sheet resultadoProfessor = null;
        try {
            planilha = SpreadSheet.createFromFile(arquivo);
            resultadoProfessor = planilha.getSheet("ResultadoPeriodo");
        } catch (IOException ex) {
            System.out.printf("Arquivo %s não foi encontrado", arquivoDados);
            System.exit(0);
        }

        if (resultadoProfessor == null) {
            planilha.addSheet("ResultadoPeriodo");
            resultadoProfessor = planilha.getSheet("ResultadoPeriodo");
        }

        int linha = 0;
        //escrever dias da semana

        for (Periodo periodo : grade.getPeriodos()) {
            resultadoProfessor.getCellAt(0, linha).setValue(periodo.getNumeroPeriodo() + "º");
            for (int dia = 0; dia < DIAS_SEMANA.length; dia++) {
                resultadoProfessor.getCellAt(dia + 1, linha).setValue(DIAS_SEMANA[dia]);
            }

            linha++;

            Disciplina[][] horarioPeriodo = periodo.getHorarioPeriodo();
            DisponibilidadePeriodo disponibilidadePeriodo = periodo.getDisponibilidadePeriodo();

            for (int hora = 0; hora < this.QUANTIDADE_HORAS; hora++) {
                for (int dia = 0; dia < this.QUANTIDADE_DIAS; dia++) {
                    if (horarioPeriodo[hora][dia] != null) {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(horarioPeriodo[hora][dia].getCodigo());
                    } else {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(disponibilidadePeriodo.getDisponibilidadeNoHorario(dia, hora));
                    }
                }

            }

            linha = linha + this.QUANTIDADE_HORAS + 2;
        }

        File outputFile = new File(this.arquivoDados);
        planilha.saveAs(outputFile);

    }

    public void LeDadosProfessoresEDisciplinas() {
        File arquivo = new File(this.arquivoDados);
        SpreadSheet planilha;
        Sheet planilhaDisponibilidade = null;
        try {
            planilha = SpreadSheet.createFromFile(arquivo);
            planilhaDisponibilidade = planilha.getSheet(this.abaDisponibilidade);
        } catch (IOException ex) {
            System.out.printf("Arquivo %s não foi encontrado", arquivoDados);
            System.exit(0);
        }
        int linhas = planilhaDisponibilidade.getRowCount();

        for (int linha = 1; linha < linhas; linha++) {
            MutableCell nomeProfessor = planilhaDisponibilidade.getCellAt(0, linha);
            String nomeProfessorString = (String) nomeProfessor.getValue();
            planilhaDisponibilidade.getRange(arquivoDados);

            MutableCell[] disponibilidade = new MutableCell[QUANTIDADE_DIAS * QUANTIDADE_HORAS];

            for (int coluna = 1; coluna <= QUANTIDADE_DIAS * QUANTIDADE_HORAS; coluna++) {
                disponibilidade[coluna - 1] = planilhaDisponibilidade.getCellAt(coluna, linha);
            }

            if (nomeProfessorString.isEmpty()) {
                break;
            }

            DisponibilidadeProfessor disponibilidadeProfessor = Util.CriaDisponibilidade(disponibilidade, QUANTIDADE_DIAS, QUANTIDADE_HORAS);
            professores.add(new Professor(nomeProfessorString, disponibilidadeProfessor, QUANTIDADE_DIAS, QUANTIDADE_HORAS));
        }

        LeDisciplinas();
    }

    private void LeDisciplinas() {
        File arquivo = new File(this.arquivoDados);
        SpreadSheet planilha;
        Sheet primeiraPlanilha = null;
        try {
            planilha = SpreadSheet.createFromFile(arquivo);
            primeiraPlanilha = planilha.getSheet(abaDisciplinas);
        } catch (IOException ex) {
            System.out.printf("Arquivo %s não foi encontrado", arquivoDados);
            System.exit(0);
        }

        int linhas = primeiraPlanilha.getRowCount();
        int colunas = primeiraPlanilha.getColumnCount();

        for (int linha = 1; linha < linhas; linha++) {
            MutableCell nomeCell = primeiraPlanilha.getCellAt(0, linha);
            MutableCell codigoCell = primeiraPlanilha.getCellAt(1, linha);
            MutableCell periodoCell = primeiraPlanilha.getCellAt(2, linha);
            MutableCell creditosCell = primeiraPlanilha.getCellAt(3, linha);
            MutableCell professorCell = primeiraPlanilha.getCellAt(4, linha);

            String nome = (String) nomeCell.getValue();

            if (nome.isEmpty()) {
                break;
            }

            String codigo = (String) codigoCell.getValue();
            Integer periodo = Util.converteParaInteiro(periodoCell);
            Integer creditos = Util.converteParaInteiro(creditosCell);

            String nomeProfessor = (String) professorCell.getValue();

            Disciplina disciplinaInserir = new Disciplina(codigo, nome, creditos, periodo);

            disciplinas.add(disciplinaInserir);
            this.associaDisciplinaAProfessor(disciplinaInserir, nomeProfessor);
        }
    }

    private void associaDisciplinaAProfessor(Disciplina disciplina, String nomeProfessor) {
        for (Professor professor : this.professores) {
            if (professor.getNome().equals(nomeProfessor)) {
                professor.addDisciplina(disciplina);
                return;
            }
        }
        System.out.printf("O professor: %s não foi encontrado para a disciplina: %s", nomeProfessor, disciplina.getNome());
        System.exit(0);
    }

    public ArrayList<Professor> getProfessores() {
        return this.professores;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

}
