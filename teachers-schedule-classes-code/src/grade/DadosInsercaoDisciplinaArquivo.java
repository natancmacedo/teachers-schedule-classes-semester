/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class DadosInsercaoDisciplinaArquivo {

    public final String codigoDisciplina;
    public final Horario horario;

    public DadosInsercaoDisciplinaArquivo(String codigoDisciplina, Horario horario) {
        this.codigoDisciplina = codigoDisciplina;
        this.horario = horario;
    }

    public String getCodigoDisciplina() {
        return codigoDisciplina;
    }

    public Horario getHorario() {
        return horario;
    }

}
