/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

import Jama.Matrix;
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
        GradeCurricular teste = new GradeCurricular(9); //9 periodos;
        teste.inicializar("../dados/Professores.ods", "../dados/DadosDisciplinas.ods");
        Matrix[] a = new Matrix[9];
        Matrix[] b = new Matrix[9];
        for(int i=1;i<=9;i++){
           
            a[i-1]=teste.mostrarDisponibilidadesPeriodo(i).copy();
        }
        teste.teste();
       
     
        for(int i=1;i<=9;i++){
            b[i-1]=teste.mostrarDisponibilidadesPeriodo(i).copy();
        }
       
        System.out.println("\n\n\n\n\n");
        
        for(int i=1;i<=9;i++){
            System.out.println(Arrays.deepToString(b[i-1].minusEquals(a[i-1]).getArray()));
        }
        
        System.out.println(teste.todasDisciplinasForamInseridas());
     teste.disciplinasAInserir();
      
        
     
        
    }

}
