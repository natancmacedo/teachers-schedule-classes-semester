/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

/**
 *
 * @author Natan
 */
public class ConfiguracaoGrade {

    public final Integer QUANTIDADE_PERIODOS;
    public final Integer QUANTIDADE_DIAS;
    public final Integer QUANTIDADE_HORAS;

    public ConfiguracaoGrade(Integer QUANTIDADE_PERIODOS, Integer QUANTIDADE_DIAS, Integer QUANTIDADE_HORAS) {
        this.QUANTIDADE_PERIODOS = QUANTIDADE_PERIODOS;
        this.QUANTIDADE_DIAS = QUANTIDADE_DIAS;
        this.QUANTIDADE_HORAS = QUANTIDADE_HORAS;
    }

    public Integer getQuantidadeHorarios() {
        return QUANTIDADE_DIAS * QUANTIDADE_HORAS;
    }
}
