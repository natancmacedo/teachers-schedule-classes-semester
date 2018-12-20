package grade;

import Jama.Matrix;
import java.math.BigDecimal;
import java.util.ArrayList;
import org.jopendocument.dom.spreadsheet.MutableCell;

public class Conversor {

    private final ConfiguracaoGrade config;

    public Conversor(ConfiguracaoGrade config) {
        this.config = config;
    }

    public DisponibilidadeProfessor CriaDisponibilidade(MutableCell[] rowDisponibilidade,
            ArrayList<DadosInsercaoDisciplinaArquivo> disciplinasAInserir) {

        double[] vetorDisponibilidadeDouble = converteVetorMutableParaDouble(rowDisponibilidade, disciplinasAInserir);
        double[][] disponibilidadeDouble = converteArray1DParaArray2d(vetorDisponibilidadeDouble);

        return new DisponibilidadeProfessor(new Matrix(disponibilidadeDouble));
    }

    private double[][] converteArray1DParaArray2d(double[] array1d) {
        double[][] array2d = new double[config.QUANTIDADE_HORAS][config.QUANTIDADE_DIAS];

        for (int i = 0; i < array1d.length; i++) {
            Horario horario = converteIndiceEmHorario(i);
            array2d[horario.hora][horario.dia] = array1d[i];
        }
        return array2d;
    }

    private double[] converteVetorMutableParaDouble(MutableCell[] rowDisponibilidade,
            ArrayList<DadosInsercaoDisciplinaArquivo> disciplinasAInserir) {

        double[] vetorDouble = new double[rowDisponibilidade.length];

        for (int i = 0; i < rowDisponibilidade.length; i++) {
            try {
                vetorDouble[i] = converteMultableCellParaDouble(rowDisponibilidade[i]);
            } catch (ClassCastException e) {
                vetorDouble[i] = 1.0;
                Horario horario = converteIndiceEmHorario(i);
                String codigoDisciplina = converteMultableCellParaString(rowDisponibilidade[i]);
                disciplinasAInserir.add(new DadosInsercaoDisciplinaArquivo(codigoDisciplina, horario));
            }
        }
        return vetorDouble;
    }

    private double converteMultableCellParaDouble(MutableCell toNumber) {
        BigDecimal numberFromMutableCell = (BigDecimal) toNumber.getValue();
        return numberFromMutableCell.doubleValue();
    }

    private String converteMultableCellParaString(MutableCell cell) {
        String stringMultableCell = cell.getValue().toString();
        return stringMultableCell;
    }

    public Integer conveteMutableCellParaInteiro(MutableCell toNumber) {
        BigDecimal numberFromMutableCell = (BigDecimal) toNumber.getValue();
        return numberFromMutableCell.intValue();
    }

    public Horario converteIndiceEmHorario(int numero) {
        int dia = (int) Math.floor(numero / (config.QUANTIDADE_DIAS - 1));
        int hora = numero % (config.QUANTIDADE_DIAS - 1);
        return new Horario(dia, hora);
    }

    public int converteHorarioEmIndice(Horario horario) {
        return horario.hora + (horario.dia) * (config.QUANTIDADE_DIAS - 1);
    }
}
