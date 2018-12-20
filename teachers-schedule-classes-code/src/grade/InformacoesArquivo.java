/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jopendocument.dom.spreadsheet.Sheet;
import org.jopendocument.dom.spreadsheet.SpreadSheet;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class InformacoesArquivo {

    public final String caminhoArquivoDados;
    public final String nomeAbaProfessores;
    public final String nomeAbaDisciplinas;
    public final String nomeAbaResultadosProfessores;
    public final String nomeAbaResultadosPeriodos;

    private File arquivo = null;
    private SpreadSheet planilha = null;
    private HashMap<String, Sheet> dicionarioAbasPlanilha;

    public InformacoesArquivo(String caminhoArquivoDados, String nomeAbaProfessores, String nomeAbaDisciplinas, String nomeAbaResultadosProfessores, String nomeAbaResultadosPeriodos) {
        this.caminhoArquivoDados = caminhoArquivoDados;
        this.nomeAbaProfessores = nomeAbaProfessores;
        this.nomeAbaDisciplinas = nomeAbaDisciplinas;
        this.nomeAbaResultadosProfessores = nomeAbaResultadosProfessores;
        this.nomeAbaResultadosPeriodos = nomeAbaResultadosPeriodos;
        preencheDicionarioAbasPlanilha();
    }

    private void preencheDicionarioAbasPlanilha() {
        dicionarioAbasPlanilha = new HashMap<>();
        dicionarioAbasPlanilha.put(nomeAbaProfessores, null);
        dicionarioAbasPlanilha.put(nomeAbaDisciplinas, null);
        dicionarioAbasPlanilha.put(nomeAbaResultadosProfessores, null);
        dicionarioAbasPlanilha.put(nomeAbaResultadosPeriodos, null);
    }

    public File getArquivo() {
        if (arquivo == null) {
            arquivo = new File(this.caminhoArquivoDados);
        }
        return arquivo;
    }

    public SpreadSheet getPlanilha() {
        File arquivoLocal = getArquivo();
        if (planilha == null) {
            try {
                planilha = SpreadSheet.createFromFile(arquivoLocal);
            } catch (IOException ex) {
                Logger.getLogger(ArquivoDados.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return planilha;
    }

    public Sheet getAbaPlanilha(String aba) {
        SpreadSheet planilhaLocal = getPlanilha();
        if (dicionarioAbasPlanilha.get(aba) == null) {
            dicionarioAbasPlanilha.replace(aba, planilhaLocal.getSheet(aba));
        }
        return dicionarioAbasPlanilha.get(aba);
    }

}
