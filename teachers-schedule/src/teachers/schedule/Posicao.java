/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

/**
 *
 * @author natan
 */
public class Posicao {
    private Integer dia;
    private Integer hora;

    public Posicao(Integer dia, Integer hora) {
        this.dia = dia;
        this.hora = hora;
    }

    public Integer getDia() {
        return dia;
    }

    public void setDia(Integer dia) {
        this.dia = dia;
    }

    public Integer getHora() {
        return hora;
    }

    public void setHora(Integer hora) {
        this.hora = hora;
    }
    
    
}
