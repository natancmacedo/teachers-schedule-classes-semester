package teachers.schedule;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Jama.Matrix;
import java.util.ArrayList;

/**
 *
 * @author natan
 */
public class Professor {

    private String nome;
    private Matrix disponibilidade;
    private ArrayList<Disciplina> disciplinas = new ArrayList<>();
    public Professor(String nome, Integer classe) {
        this.nome = nome;
        this.disponibilidade = TipoDisponibilidade.classe(classe);
    }

    @Override
    public String toString() {
        return "Professor{" + "nome=" + nome + ", disponibilidade=" + disponibilidade + ", disciplinas=" + disciplinas + '}';
    }

    public String getNome() {
        return nome;
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public Matrix getDisponibilidade() {
        return disponibilidade;
    }
    
    

}
