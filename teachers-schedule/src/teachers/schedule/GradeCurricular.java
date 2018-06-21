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

    /**
     * Lista de todos os professores do curso
     */
    private ArrayList<Professor> professores = new ArrayList<>();
    /**
     * Lista das disciplinas do curso
     */
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();
    /**
     * Quantidade de periodos do curso
     */
    private Integer qtdperiodos;
    /**
     * Vetor que guarda os periodos (número e horário das disciplinas)
     */
    private Periodo[] periodos;
    /**
     * Vetor que guarda as restrições, as possibilidades de inserção das
     * matérias estão no vetor
     */
    private Restricao[] restricoes;

    /**
     * Inicializa a grade curricular considerando a quantidade de periodos
     *
     * @param qtdperiodos
     */
    public GradeCurricular(Integer qtdperiodos) {

        this.qtdperiodos = qtdperiodos;
        this.periodos = new Periodo[this.qtdperiodos]; //novo vetor com qtdperidos casas
        this.restricoes = new Restricao[qtdperiodos];
        for (int i = 0; i < this.qtdperiodos; i++) { // a cada periodo inicializa as restrições, fazendo clone dos objetos para não referenciarem ao mesmo objeto
            this.periodos[i] = new Periodo(i + 1);
            this.restricoes[i] = new Restricao(TipoDisponibilidade.classe(0).copy());
        }
    }

    /**
     * Lê os arquivos passados, verifica condições básicas do problema
     *
     * @param pathArquivoProfessor
     * @param pathArquivoDisciplinas
     * @throws IOException
     */
    public void inicializar(String pathArquivoProfessor, String pathArquivoDisciplinas) throws IOException {
        this.LerProfessores(pathArquivoProfessor);
        this.LerDisciplinas(pathArquivoDisciplinas);
        if (!this.todasDisciplinasEstaoAssociadasAProfessores()) {
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

            this.insereDisciplinaNoProfessor(this.disciplinas.get(this.disciplinas.size() - 1), (String) professor.getValue());

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

    /**
     * Insere a disciplina dentro da lista de disciplinas de cada professor
     *
     * @param disciplina
     * @param professorNome
     */
    private void insereDisciplinaNoProfessor(Disciplina disciplina, String professorNome) {
        int qtdnotfound = 0;
        for (Professor professor : this.professores) {
            if (professor.getNome().equals(professorNome)) { //verifica se o professor passado é o professor encontrado na lista, 
                professor.getDisciplinas().add(disciplina);
            } else {
                qtdnotfound++; //caso não seja, é incrementado
            }
        }
        if (qtdnotfound >= this.professores.size()) { //para n professores, é possível errar apenas n-1 vezes, se o erro for n ou maior, há algum erro
            System.out.println(disciplina);
        }
    }

    /**
     * Converte o mutable cell para inteiro
     *
     * @param mutableCell
     * @return
     */
    private Integer converteNumeroDocumento(MutableCell mutableCell) {
        BigDecimal bigDecimalMutableCell = (BigDecimal) mutableCell.getValue();
        return bigDecimalMutableCell.intValue();
    }

    /**
     * Zera todas as restrições de disponibilidade
     */
    private void zeraRestricoes() {
        for (Restricao resticao : this.restricoes) {
            resticao.zeraRestricao();
        }
    }

    /**
     * Soma as disponibilidades de todos os professores que tem matéria em cada
     * período, por período caso suas matérias do periodo não tenham sido
     * alocadas
     */
    private void calculaRestricoes() {

        for (Periodo periodo : this.periodos) {
            this.restricoes[periodo.getNumero() - 1].zeraRestricao();
            for (Professor p : this.professores) {
                if (p.temDisciplinaNoPeriodo(periodo.getNumero()) && !p.todasAsDisciplinasForamAlocadas()) {
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
    private Boolean todasDisciplinasEstaoAssociadasAProfessores() {
        int qtd = 0;

        for (Professor p : this.professores) {
            qtd += p.getDisciplinas().size();
        }

        return qtd == this.disciplinas.size();
    }

    /**
     * Acha um horário disponivel em um periodo em um periodo
     *
     * @param periodo o periodo
     * @return horario
     */
    private Horario achaHorarioDisponivelNoPeriodo(Integer periodo) {
        double[][] restricao_periodo = this.restricoes[periodo - 1].getDisponibilidade().getArray();

        for (int dia = 0; dia < restricao_periodo[0].length; dia++) {
            for (int hora = 0; hora < restricao_periodo.length; hora++) {
                if (restricao_periodo[hora][dia] == 1.0 && restricao_periodo[hora][dia] > 0) {
                    return new Horario(dia, hora, periodo);
                }
            }
        }
        return null;
    }

    /**
     * Acha horários disponiveis em um periodo
     *
     * @param periodo o periodo
     * @param quantidadeHorarios quantos horários são buscados
     * @return ArrayList<horario>
     */
    private ArrayList<Horario> achaHorariosDisponiveisNoPeriodo(Integer periodo, Integer quantidadeHorarios) {
        double[][] restricao_periodo = this.restricoes[periodo - 1].getDisponibilidade().getArray();

        for (int dia = 0; dia < restricao_periodo[0].length; dia++) {
            for (int hora = 0; hora < restricao_periodo.length; hora++) {
                if (restricao_periodo[hora][dia] == 1.0 && restricao_periodo[hora][dia] > 0) {
                    // return new Horario(dia, hora, periodo);
                }
            }
        }
        return null;
    }

    /**
     * Lista todos os professores que tem disciplinas em um periodo
     *
     * @param periodo
     * @return
     */
    private ArrayList<Professor> professoresComDisciplinasNoPeriodo(Integer periodo) {
        ArrayList<Professor> professores = new ArrayList<>();
        for (Professor professor : this.professores) {
            if (professor.temDisciplinaNoPeriodo(periodo)) {
                professores.add(professor);
            }
        }
        return professores;
    }

    /**
     * Acha o professor com a disponibilidade em um dado horário
     *
     * @param horario
     * @return
     */
    private Professor professorComADisponibilidade(Horario horario) {
        ArrayList<Professor> professoresAulaPeriodo = this.professoresComDisciplinasNoPeriodo(horario.getPeriodo());

        for (Professor professor : professoresAulaPeriodo) {
            double disponibilidade = professor.getDisponibilidade().getArray()[horario.getHora()][horario.getDia()];
            if (disponibilidade == 1) {
                ArrayList<Disciplina> disciplinas = professor.disciplinasDoProfessorNoPeriodo(horario.getPeriodo());
                horario.setDisciplina(disciplinas.get(0));
                return professor;
            }
        }
        return null;
    }

    public Professor qualProfessorTemADisciplina(Disciplina disciplina) {
        for (Professor professor : this.professores) {
            if (professor.getDisciplinas().contains(disciplina)) {
                return professor;
            }
        }
        return null;
    }

    /**
     * Verifica se um horário está disponível olhando se há alguma matéria
     * colocada no horário e se as restrições permitem
     *
     * @param dia
     * @param hora
     * @return true ou false
     */
    public Boolean horarioEstaDisponivel(Integer periodo, Integer dia, Integer hora) {
        return this.periodos[periodo - 1].horarioEstaDisponivel(dia, hora) && this.restricoes[periodo - 1].horarioEstaDisponivel(dia, hora);
    }

    /**
     * Verifica se horários em sequencia no mesmo dia estão disponíveis
     *
     * @param dia
     * @param horaInicio
     * @param horaFinal
     * @return
     */
    public Boolean horariosEstaoDisponiveis(Professor professor, Integer periodo, Integer dia, Integer horaInicio, Integer horaFinal) {
        for (int i = horaInicio; i <= horaFinal; i++) {

            if (!professor.horarioEstaDisponivel(dia, i) && !this.horarioEstaDisponivel(periodo, dia, i)) {
                return false;
            }
        }
        return true;
    }

    public void teste() {

        
        this.insercaoSucessiva();

    }

    public ArrayList<Horario> horariosDisponiveis(Professor professor, Disciplina disciplina, Integer qtdHorarios) {

        Integer dias = this.restricoes[0].getDisponibilidade().getColumnDimension();
        Integer horas = this.restricoes[0].getDisponibilidade().getRowDimension();
        Integer hora = 0;

        for (Periodo periodo : this.periodos) {
            for (int dia = 0; dia < dias; dia++) {
                if (periodo.temADisciplinaNoDia(disciplina, dia)) {
                    continue;
                }
                while (hora + qtdHorarios <= horas) {

                    if (this.horariosEstaoDisponiveis(professor, periodo.getNumero(), dia, hora, hora + qtdHorarios - 1)) {
                        //System.out.println("horario disponivel");
                        return Horario.montaHorario(periodo.getNumero(), dia, hora, hora + qtdHorarios - 1, disciplina);
                    }
                    hora++;
                }
                hora = 0;
            }
        }

        return null;
    }

    public void insereHorarios(Professor professor, ArrayList<Horario> horarios) {
        for (Horario horario : horarios) {
            this.insereDisciplina(professor, horario);
        }
    }

    /**
     * Insere uma disciplina na grade
     *
     * @param horario
     */
    private void insereDisciplina(Professor professor, Horario horario) {
        Disciplina[][] disciplinasPeriodo;
        disciplinasPeriodo = this.periodos[horario.getPeriodo() - 1].getDisciplinas();
        disciplinasPeriodo[horario.getHora()][horario.getDia()] = horario.getDisciplina(); //coloca a disciplina no vetor de periodos
        horario.getDisciplina().incrementaCreditosAlocados(); //aumenta o credito alocado da disciplina
        professor.insereDisciplinaDisponibilidadeProfessor(horario); //zera a disponibilidade do professor no horário
    }

    public Integer tentativaInsercao(Integer creditos, Integer creditosAlocados) {
        Integer tentativaHorarios = 0;
        if (creditosAlocados == 0) {
            switch (creditos) {
                case 5:
                    tentativaHorarios = 3;
                    break;
                case 4:
                    tentativaHorarios = 2;
                    break;
                case 3:
                    tentativaHorarios = 3;
                    break;
                case 2:
                    tentativaHorarios = 2;
                    break;
                case 1:
                    tentativaHorarios = 1;
                    break;
            }
        } else {
            //tentativaHorarios = creditos - creditosAlocados;
            tentativaHorarios = 1;
        }
        return tentativaHorarios;
    }

    public void insercaoSucessiva() {
        Integer tentativaHorarios = 0;
        ArrayList<Horario> horariosDisponiveisInsercao = new ArrayList<>();
        if (!this.aSolucaoEPossivel()) {
            System.out.println("A solução não é possível");
        } else {
            Boolean gradeResolvida = false;
            ArrayList<Disciplina> disciplinasInserir = this.disciplinasAInserir();
            while (!gradeResolvida) {
                for (Disciplina disciplina : disciplinasInserir) {
                    if(disciplina.disciplinaTotalmenteInserida()){
                        continue;
                    }
                    Professor professor = this.qualProfessorTemADisciplina(disciplina);

                    tentativaHorarios = this.tentativaInsercao(disciplina.getCreditos(), disciplina.getCreditosAlocados());
                    horariosDisponiveisInsercao = this.horariosDisponiveis(professor, disciplina, tentativaHorarios);
                    if (horariosDisponiveisInsercao != null) {
                        this.insereHorarios(professor, horariosDisponiveisInsercao);
                    } else {
                        System.out.println("Não foi encontrado horário disponível");
                    }

                    this.zeraRestricoes();
                    this.calculaRestricoes();
                    this.mostrarDisponibilidadesPeriodo(disciplina.getPeriodo());
                }
                gradeResolvida = this.todasDisciplinasForamInseridas();
                disciplinasInserir = this.disciplinasAInserir();
            }
        }

    }

    public Boolean aSolucaoEPossivel() {
        double[] solucaoPossivelPeriodo = new double[qtdperiodos];
        for (Disciplina disciplina : this.disciplinas) {
            solucaoPossivelPeriodo[disciplina.getPeriodo() - 1] += disciplina.getCreditos();
        }
        for (Periodo periodo : this.periodos) {
            System.out.println("Periodo: " + periodo.getNumero() + " creditos totais: " + solucaoPossivelPeriodo[periodo.getNumero() - 1] + " horarios possiveis: " + this.restricoes[periodo.getNumero() - 1].quantidadeDeHorariosDisponiveis());

            if (solucaoPossivelPeriodo[periodo.getNumero() - 1] > this.restricoes[periodo.getNumero() - 1].quantidadeDeHorariosDisponiveis()) {
           
                return false;
            }
        }
  
        return true;
    }

    public Boolean todasDisciplinasForamInseridas() {
        for (Disciplina disciplina : this.disciplinas) {
            if (!disciplina.disciplinaTotalmenteInserida()) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Disciplina> disciplinasAInserir() {
        ArrayList<Disciplina> disciplinasParaInserir = new ArrayList<Disciplina>();
        for (Disciplina disciplina : this.disciplinas) {
            if (!disciplina.disciplinaTotalmenteInserida()) {
                disciplinasParaInserir.add(disciplina);
                System.out.println(disciplina.getCreditos() + " " + disciplina.getCreditosAlocados() + " " + disciplina.getNome());
            }
        }
        return disciplinasParaInserir;
    }

    public Matrix mostrarDisponibilidadesPeriodo(Integer periodo) {
        System.out.println((periodo) + "º Periodo");
        this.restricoes[periodo - 1].mostrarRestricao();
        System.out.println();
        return this.restricoes[periodo - 1].getDisponibilidade().copy();
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
