/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleancodetcc;

import CPLEX.*;
import grade.GradeHorarios;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author natan
 */
public class CleanCodeTCC extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        GradeHorarios grade = new GradeHorarios(9, 6, 5);
        grade.inicializarProblema("../dados/DadosReunidos.ods", "Disponibilidade", "Disciplinas");

        new TeacherScheduleProblem().main(grade);

        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
