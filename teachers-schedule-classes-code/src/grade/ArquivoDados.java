package grade;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jopendocument.dom.spreadsheet.MutableCell;
import org.jopendocument.dom.spreadsheet.Sheet;

public class ArquivoDados {

    private final InformacoesArquivo informacoesArquivo;
    private final ConfiguracaoGrade config;
    private final Conversor conversor;

    private final ArrayList<Professor> professores = new ArrayList<>();
    private final ArrayList<Disciplina> disciplinas = new ArrayList<>();
    private final ArrayList<DadosInsercaoDisciplinaArquivo> disciplinasAInserir = new ArrayList<>();

    private final String[] DIAS_SEMANA = {"SEG", "TER", "QUAR", "QUI", "SEXT", "SAB"};

    public ArquivoDados(InformacoesArquivo informacoesArquivo, ConfiguracaoGrade config) {
        this.informacoesArquivo = informacoesArquivo;
        this.config = config;
        this.conversor = new Conversor(config);
    }

    public void gravaResultadosNoArquivo(GradeHorarios grade) {
        gravaResultadoPorProfessor(grade);
        gravaResultadoPorPeriodo(grade);
    }

    private void gravaResultadoPorProfessor(GradeHorarios grade) {

        int linha = 0;

        Sheet resultadoProfessor = informacoesArquivo
                .getAbaPlanilha(informacoesArquivo.nomeAbaResultadosProfessores);

        for (Professor professor : grade.getProfessores()) {

            resultadoProfessor.getCellAt(0, linha).setValue(professor.getNome());

            for (int dia = 0; dia < DIAS_SEMANA.length; dia++) {
                MutableCell cell = resultadoProfessor.getCellAt(dia + 1, linha);
                cell.setValue(DIAS_SEMANA[dia]);
            }

            linha++;

            Disciplina[][] horarioProfessor = professor.getHorarioPessoal();
            DisponibilidadeProfessor disponibilidadeProfessor = professor.getDisponibilidadeProfessor();

            for (int hora = 0; hora < config.quantidadeHoras; hora++) {
                for (int dia = 0; dia < config.quantidadeDias; dia++) {
                    if (horarioProfessor[hora][dia] != null) {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(horarioProfessor[hora][dia].getCodigo());
                    } else {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(disponibilidadeProfessor.getDisponibilidadeNoHorario(dia, hora));
                    }
                }

            }
            linha = linha + config.quantidadeHoras + 2;
        }

        File outputFile = informacoesArquivo.getArquivo();
        try {
            informacoesArquivo.getPlanilha().saveAs(outputFile);
        } catch (IOException ex) {
            Logger.getLogger(ArquivoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void gravaResultadoPorPeriodo(GradeHorarios grade) {

        Sheet resultadoProfessor = informacoesArquivo
                .getAbaPlanilha(informacoesArquivo.nomeAbaResultadosPeriodos);

        int linha = 0;

        for (Periodo periodo : grade.getPeriodos()) {
            resultadoProfessor.getCellAt(0, linha).setValue(periodo.getNumeroPeriodo() + "º");
            for (int dia = 0; dia < DIAS_SEMANA.length; dia++) {
                resultadoProfessor.getCellAt(dia + 1, linha).setValue(DIAS_SEMANA[dia]);
            }

            linha++;

            Disciplina[][] horarioPeriodo = periodo.getHorarioPeriodo();
            DisponibilidadePeriodo disponibilidadePeriodo = periodo.getDisponibilidadePeriodo();

            for (int hora = 0; hora < config.quantidadeHoras; hora++) {
                for (int dia = 0; dia < config.quantidadeDias; dia++) {
                    if (horarioPeriodo[hora][dia] != null) {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(horarioPeriodo[hora][dia].getCodigo());
                    } else {
                        resultadoProfessor.getCellAt(dia + 1, hora + linha).setValue(disponibilidadePeriodo.getDisponibilidadeNoHorario(dia, hora));
                    }
                }

            }

            linha = linha + config.quantidadeHoras + 2;
        }

        File outputFile = informacoesArquivo.getArquivo();
        try {
            informacoesArquivo.getPlanilha().saveAs(outputFile);
        } catch (IOException ex) {
            Logger.getLogger(ArquivoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void leDadosProfessoresEDisciplinas() {
        leDadosProfessores();
        leDadosDisciplinas();
    }

    private void leDadosProfessores() {
        Sheet planilhaDisponibilidade = null;
        planilhaDisponibilidade = informacoesArquivo.
                getAbaPlanilha(informacoesArquivo.nomeAbaProfessores);

        int linhas = planilhaDisponibilidade.getRowCount();

        for (int linha = 1; linha < linhas; linha++) {
            MutableCell nomeProfessor = planilhaDisponibilidade.getCellAt(0, linha);
            String nomeProfessorString = (String) nomeProfessor.getValue();
            planilhaDisponibilidade.getRange(informacoesArquivo.caminhoArquivoDados);

            MutableCell[] disponibilidade = new MutableCell[config.getQuantidadeHorarios()];

            if (nomeProfessorString.isEmpty()) {
                break;
            }

            for (int coluna = 1; coluna <= config.getQuantidadeHorarios(); coluna++) {
                disponibilidade[coluna - 1] = planilhaDisponibilidade.getCellAt(coluna, linha);
            }

            DisponibilidadeProfessor disponibilidadeProfessor = conversor
                    .criaDisponibilidade(disponibilidade, disciplinasAInserir);
            professores.add(new Professor(nomeProfessorString, disponibilidadeProfessor, config));
        }
    }

    private void leDadosDisciplinas() {

        Sheet abaDisciplinas = null;
        abaDisciplinas = informacoesArquivo
                .getAbaPlanilha(informacoesArquivo.nomeAbaDisciplinas);

        int linhas = abaDisciplinas.getRowCount();

        for (int linha = 1; linha < linhas; linha++) {
            MutableCell nomeCell = abaDisciplinas.getCellAt(0, linha);
            MutableCell codigoCell = abaDisciplinas.getCellAt(1, linha);
            MutableCell periodoCell = abaDisciplinas.getCellAt(2, linha);
            MutableCell creditosCell = abaDisciplinas.getCellAt(3, linha);
            MutableCell professorCell = abaDisciplinas.getCellAt(4, linha);

            String nome = (String) nomeCell.getValue();

            if (nome.isEmpty()) {
                break;
            }

            String codigo = (String) codigoCell.getValue();
            Integer periodo = conversor.conveteMutableCellParaInteiro(periodoCell);
            Integer creditos = conversor.conveteMutableCellParaInteiro(creditosCell);

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
        try {
            throw new ProfessorNaoEncontradoException(nomeProfessor, disciplina.getCodigo());
        } catch (ProfessorNaoEncontradoException ex) {
            Logger.getLogger(ArquivoDados.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Professor> getProfessores() {
        return this.professores;
    }

    public List<Disciplina> getDisciplinas() {
        return this.disciplinas;
    }

    public List<DadosInsercaoDisciplinaArquivo> getInsercaoDiretaDoArquivo() {
        return this.disciplinasAInserir;
    }
}
