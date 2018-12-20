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
public class ConfiguracaoGrade {

    public final Integer quantidadePeriodos;
    public final Integer quantidadeDias;
    public final Integer quantidadeHoras;

    public ConfiguracaoGrade(Integer quantidadePeriodos, Integer quantidadeDias, Integer quantidadeHoras) {
        this.quantidadePeriodos = quantidadePeriodos;
        this.quantidadeDias = quantidadeDias;
        this.quantidadeHoras = quantidadeHoras;
    }

    public Integer getQuantidadeHorarios() {
        return quantidadeDias * quantidadeHoras;
    }
}
