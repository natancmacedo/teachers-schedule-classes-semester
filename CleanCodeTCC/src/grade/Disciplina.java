/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.util.HashMap;

/**
 *
 * @author natan
 */
public class Disciplina {

    private final String codigo;
    private final String nome;
    private final Integer creditos;
    private final Integer periodo;
    private String apelido;
    private Integer creditosAlocados;

    private final static String[] ARTIGOS
            = {
                "da", "de", "e", "por", "à", "a"
            };

    private static HashMap<String, String> ROMANOS_CONVERTER;

    public Disciplina(String codigo, String nome, Integer creditos, Integer periodo) {
        this.codigo = codigo;
        this.nome = nome;
        this.creditos = creditos;
        this.periodo = periodo;
        populaAlgarismosRomanos();
        this.createApelido();
        this.creditosAlocados = 0;
    }

    private static void populaAlgarismosRomanos() {
        ROMANOS_CONVERTER = new HashMap<>();
        ROMANOS_CONVERTER.put("I", "1");
        ROMANOS_CONVERTER.put("II", "2");
        ROMANOS_CONVERTER.put("III", "3");
        ROMANOS_CONVERTER.put("IV", "4");
        ROMANOS_CONVERTER.put("V", "5");
    }

    private void createApelido() {
        String[] nomeVetor = this.nome.split(" ");
        String apelidoGerado = "";

        for (String parte : nomeVetor) {
            if (!this.ehArtigo(parte)) {
                apelidoGerado += getInicialNomeDaDisciplina(parte);
            }
        }

        this.apelido = apelidoGerado.toUpperCase();
    }

    private Boolean ehArtigo(String verificar) {
        for (String artigo : Disciplina.ARTIGOS) {
            if (verificar.equals(artigo)) {
                return true;
            }
        }
        return false;
    }

    private String getInicialNomeDaDisciplina(String parte) {
        if (ehRomano(parte)) {
            return Disciplina.ROMANOS_CONVERTER.get(parte);
        } else {
            return parte.substring(0, 1);
        }
    }

    private Boolean ehRomano(String parte) {
        return Disciplina.ROMANOS_CONVERTER.containsKey(parte);
    }

    public void addCreditosAlocados() {
        if (!estaTotalmenteAlocada()) {
            this.creditosAlocados++;
        }
    }

    public Boolean estaTotalmenteAlocada() {
        return this.creditosAlocados.equals(this.creditos);
    }

    public Integer getCreditosAAlocar() {
        return this.getCreditos() - this.getCreditosAlocados();
    }

    public Integer quantidadeHorariosTentativaInsercao() {
        Integer quantidadeTentativaInsercaoHorarios = 0;
        if (creditosAlocados.equals(0)) {
            switch (creditos) {
                case 5:
                    quantidadeTentativaInsercaoHorarios = 3;
                    break;
                case 4:
                    quantidadeTentativaInsercaoHorarios = 2;
                    break;
                case 3:
                    quantidadeTentativaInsercaoHorarios = 3;
                    break;
                case 2:
                    quantidadeTentativaInsercaoHorarios = 2;
                    break;
                case 1:
                    quantidadeTentativaInsercaoHorarios = 1;
                    break;
                default:
                    quantidadeTentativaInsercaoHorarios = 2;
                    break;
            }
        } else {
            quantidadeTentativaInsercaoHorarios = creditos - creditosAlocados;
        }
        return quantidadeTentativaInsercaoHorarios;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getApelido() {
        return apelido;
    }

    public Integer getCreditos() {
        return creditos;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public Integer getCreditosAlocados() {
        return creditosAlocados;
    }

}
