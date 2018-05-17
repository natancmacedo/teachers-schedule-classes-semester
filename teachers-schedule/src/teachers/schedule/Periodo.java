package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author natan
 */
public class Periodo {
    private Integer numero;
    private Disciplina disciplinas[][];
    private Integer disponibilidade[][];


    public Periodo(Integer numero) {
        this.numero = numero;
        this.disciplinas = new Disciplina[5][6];
        this.disponibilidade = TipoDisponibilidade.classe(0);
    }

    public Integer getNumero() {
        return numero;
    }

    public Integer[][] getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(Integer[][] disponibilidade) {
        this.disponibilidade = disponibilidade;
    }
    
    
     
    
}
