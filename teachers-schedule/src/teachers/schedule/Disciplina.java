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
public class Disciplina {

    private String codigo;
    private String nome;
    private String apelido;
    private Integer creditos;
    private Integer periodo;
    private Integer creditosAlocados = 0;

    public Disciplina() {
    }

    
    public Disciplina(String codigo, String nome,  Integer creditos, Integer periodo) {
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

    private void setApelido() {
        String nomevetor[] = this.nome.split(" ");
        String apelido1 = "";
        for (String nomevetor1 : nomevetor) {
            if ("da".equals(nomevetor1) || "de".equals(nomevetor1) || "e".equals(nomevetor1) || "por".equals(nomevetor1) || "à".equals(nomevetor1)) {
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

    public void setCreditosAlocados(Integer creditosAlocados) {
        this.creditosAlocados = creditosAlocados;
    }

    @Override
    public String toString() {
        return "Disciplina{" + "codigo=" + codigo + ", nome=" + nome + ", apelido=" + apelido + ", creditos=" + creditos + ", periodo=" + periodo + ", creditosAlocados=" + creditosAlocados + '}';
    }


    
    

}
