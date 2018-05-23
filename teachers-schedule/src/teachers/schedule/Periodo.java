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

    public Periodo(Integer numero) {
        this.numero = numero;
        this.disciplinas = new Disciplina[5][6];
        
    }

    public Integer getNumero() {
        return numero;
    }

    public Disciplina[][] getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(Disciplina[][] disciplinas) {
        this.disciplinas = disciplinas;
    }
   
}
