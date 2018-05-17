/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

import java.io.IOException;
import java.util.Arrays;

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
               GradeCurricular.LerProfessores("../dados/Professores.ods");
        GradeCurricular.LerDisciplinas("../dados/DadosDisciplinas.ods");
        //GradeCurricular.mostrarDisciplinas();
        
       GradeCurricular.periodos(9);
       GradeCurricular.disponibilidadesPeriodo();
       GradeCurricular.mostrarDisponibilidadesPeriodo();
       // System.out.println(t);
       System.out.println(     GradeCurricular.disciplinasprofessores());
       
      
       
       
      //  GradeCurricular.mostrarProfessores();
    }
    
}
