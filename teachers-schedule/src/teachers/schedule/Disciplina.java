package teachers.schedule;

import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author natan
 */
public class Disciplina {

    private String codigo;
    private String nome;
    private String apelido;
    private Integer creditos;
    private Integer periodo;
    private Integer creditosAlocados = 0;
    private final String[] ARTIGOS = {"da", "de", "e", "por", "à","a"};

    /**
     * Construtor padrão
     */
    public Disciplina() {
    }

    /**
     * Construtor
     *
     * @param codigo
     * @param nome
     * @param creditos
     * @param periodo
     */
    public Disciplina(String codigo, String nome, Integer creditos, Integer periodo) {
        this.codigo = codigo;
        this.nome = nome;
        this.setApelido();
        this.creditos = creditos;
        this.periodo = periodo;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
        this.setApelido();
    }

    public String getApelido() {
        return apelido;
    }

    /**
     * Compara se a string é um artigo como: a, de, do, da, por
     *
     * @param compare
     * @return
     */
    private Boolean eArtigo(String compare) {
        for (String artigo : this.ARTIGOS) {
            if (compare.equals(artigo)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Pega o nome da disciplina e gera um apelido para ela (usando as iniciais)
     */
    private void setApelido() {
        String nomevetor[] = this.nome.split(" ");
        String apelido1 = "";
        for (String nomevetor1 : nomevetor) {
            if (this.eArtigo(nomevetor1)) {
            } else {
                apelido1 += nomevetor1.substring(0, 1);
            }
        }

        this.apelido = apelido1;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Integer getCreditosAlocados() {
        return creditosAlocados;
    }

    private void setCreditosAlocados(Integer creditosAlocados) {
        if (this.disciplinaTotalmenteInserida()) {
            System.out.println("Cŕeditos totalmente alocados");
        } else {
            this.creditosAlocados = creditosAlocados;
        }
    }

    @Override
    public String toString() {
        return "Disciplina{" + "codigo=" + codigo + ", nome=" + nome + ", apelido=" + apelido + ", creditos=" + creditos + ", periodo=" + periodo + ", creditosAlocados=" + creditosAlocados + '}';
    }

    public void incrementaCreditosAlocados() {
        this.setCreditosAlocados(this.creditosAlocados + 1);
    }

    public Boolean disciplinaTotalmenteInserida(){
        return this.creditosAlocados.equals(this.creditos);
    }
}
