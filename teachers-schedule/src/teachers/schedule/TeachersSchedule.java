/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

import java.io.IOException;

/**
 *
 * @author natan
 */
public class TeachersSchedule {

    /**
     * @param args the command line arguments
     * @throws java.io.IOException
     */
    public static void main(String[] args) throws IOException {
        GradeCurricular teste = new GradeCurricular(9); //9 periodos;
        teste.inicializar("../dados/Professores.ods", "../dados/DadosDisciplinas.ods");
        teste.mostrarDisponibilidadesPeriodo();
       
        teste.procuraEInsercao();

    }

}
