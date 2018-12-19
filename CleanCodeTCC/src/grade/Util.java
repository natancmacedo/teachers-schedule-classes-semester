package grade;

import Jama.Matrix;
import java.math.BigDecimal;
import org.jopendocument.dom.spreadsheet.MutableCell;

public class Util {

    public static DisponibilidadeProfessor CriaDisponibilidade(MutableCell[] rowDisponibilidade, int quantidadeDias, int quantidadeHoras) {
        double[][] disponibilidadeDouble = converteArray1DParaArray2d(converteParaVetorDouble(rowDisponibilidade), quantidadeHoras, quantidadeDias);

        return new DisponibilidadeProfessor(new Matrix(disponibilidadeDouble));
    }

    private static double[][] converteArray1DParaArray2d(double[] array1d, int horas, int dias) {
        double[][] array2d = new double[horas][dias];

        for (int i = 0; i < array1d.length; i++) {
            Horario horario = Util.transformaNumeroEmHorario(i, dias);
            array2d[horario.hora][horario.dia] = array1d[i];
        }
        return array2d;
    }

    private static double[] converteParaVetorDouble(MutableCell[] rowDisponibilidade) {

        double[] vetorDouble = new double[rowDisponibilidade.length];

        for (int i = 0; i < rowDisponibilidade.length; i++) {
            vetorDouble[i] = converteParaDouble(rowDisponibilidade[i]);
        }

        return vetorDouble;
    }

    public static double converteParaDouble(MutableCell toNumber) {
        BigDecimal numberFromMutableCell = (BigDecimal) toNumber.getValue();
        return numberFromMutableCell.doubleValue();
    }

    public static Integer converteParaInteiro(MutableCell toNumber) {
        BigDecimal numberFromMutableCell = (BigDecimal) toNumber.getValue();
        return numberFromMutableCell.intValue();
    }

    public static Horario transformaNumeroEmHorario(int numero, int quantidadeDias) {
        int dia = (int) Math.floor(numero / (quantidadeDias - 1));
        int hora = numero % (quantidadeDias - 1);
        return new Horario(dia, hora);
    }

    public static int transformaHorarioEmNumero(Horario horario, int quantidadeDias) {
        return horario.hora + (horario.dia) * (quantidadeDias - 1);
    }
}
