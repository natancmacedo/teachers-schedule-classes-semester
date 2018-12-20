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
 * @author Natan
 */
public class InformacoesArquivo {

    public final String CAMINHO_ARQUIVO_DADOS;
    public final String NOME_ABA_PROFESSORES;
    public final String NOME_ABA_DISCIPLINAS;
    public final String NOME_ABA_RESULTADOS_PROFESSORES;
    public final String NOME_ABA_RESULTADOS_PERIODOS;

    private File arquivo = null;
    private SpreadSheet planilha = null;
    private HashMap<String, Sheet> dicionarioAbasPlanilha;

    public InformacoesArquivo(String CAMINHO_ARQUIVO_DADOS, String NOME_ABA_PROFESSORES, String NOME_ABA_DISCIPLINAS,
            String NOME_ABA_RESULTADOS_PROFESSORES, String NOME_ABA_RESULTADOS_PERIODOS) {
        this.CAMINHO_ARQUIVO_DADOS = CAMINHO_ARQUIVO_DADOS;
        this.NOME_ABA_PROFESSORES = NOME_ABA_PROFESSORES;
        this.NOME_ABA_DISCIPLINAS = NOME_ABA_DISCIPLINAS;
        this.NOME_ABA_RESULTADOS_PROFESSORES = NOME_ABA_RESULTADOS_PROFESSORES;
        this.NOME_ABA_RESULTADOS_PERIODOS = NOME_ABA_RESULTADOS_PERIODOS;
        preencheDicionarioAbasPlanilha();
    }

    private void preencheDicionarioAbasPlanilha() {
        dicionarioAbasPlanilha = new HashMap<>();
        dicionarioAbasPlanilha.put(NOME_ABA_PROFESSORES, null);
        dicionarioAbasPlanilha.put(NOME_ABA_DISCIPLINAS, null);
        dicionarioAbasPlanilha.put(NOME_ABA_RESULTADOS_PROFESSORES, null);
        dicionarioAbasPlanilha.put(NOME_ABA_RESULTADOS_PERIODOS, null);
    }

    public File getArquivo() {
        if (arquivo == null) {
            arquivo = new File(this.CAMINHO_ARQUIVO_DADOS);
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
