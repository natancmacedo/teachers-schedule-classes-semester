package grade;

import Jama.Matrix;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jopendocument.dom.spreadsheet.MutableCell;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class Conversor {

    private final ConfiguracaoGrade config;

    public Conversor(ConfiguracaoGrade config) {
        this.config = config;
    }

    public DisponibilidadeProfessor criaDisponibilidade(MutableCell[] rowDisponibilidade,
            List<DadosInsercaoDisciplinaArquivo> disciplinasAInserir) {

        double[] vetorDisponibilidadeDouble = converteVetorMutableParaDouble(rowDisponibilidade, disciplinasAInserir);
        double[][] disponibilidadeDouble = converteArray1DParaArray2d(vetorDisponibilidadeDouble);

        return new DisponibilidadeProfessor(new Matrix(disponibilidadeDouble));
    }

    private double[][] converteArray1DParaArray2d(double[] array1d) {
        double[][] array2d = new double[config.quantidadeHoras][config.quantidadeDias];

        for (int i = 0; i < array1d.length; i++) {
            Horario horario = converteIndiceEmHorario(i);
            try{
                 array2d[horario.hora][horario.dia] = array1d[i];
            }
           catch(Exception e)
           {
               Logger.getLogger(Conversor.class.getName()).log( Level.WARNING, "A conversão de vetor para matriz deu errado");
           }
            
        }
        return array2d;
    }

    private double[] converteVetorMutableParaDouble(MutableCell[] rowDisponibilidade,
            List<DadosInsercaoDisciplinaArquivo> disciplinasAInserir) {

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
        return cell.getValue().toString();
    }

    public Integer conveteMutableCellParaInteiro(MutableCell toNumber) {
        BigDecimal numberFromMutableCell = (BigDecimal) toNumber.getValue();
        return numberFromMutableCell.intValue();
    }

    public Horario converteIndiceEmHorario(int numero) {
        int dia = numero / (config.quantidadeDias - 1);
        int hora = numero % (config.quantidadeDias - 1);
        return new Horario(dia, hora);
    }

    public int converteHorarioEmIndice(Horario horario) {
        return horario.hora + (horario.dia) * (config.quantidadeDias - 1);
    }
}
