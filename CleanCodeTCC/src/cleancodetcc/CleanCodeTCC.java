/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cleancodetcc;

import CPLEX.*;
import grade.ConfiguracaoGrade;
import grade.GradeHorarios;
import grade.InformacoesArquivo;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author natan
 */
public class CleanCodeTCC extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        ConfiguracaoGrade config = new ConfiguracaoGrade(9, 6, 5);
        GradeHorarios grade = new GradeHorarios(config);

        InformacoesArquivo infoFile = new InformacoesArquivo("../dados/DadosReunidos.ods",
                "Disponibilidade",
                "Disciplinas",
                "ResultadoProfessor",
                "ResultadoPeriodo");

        grade.preencherDadosProblema(infoFile);

        new TeacherScheduleProblem(grade).begin();

        System.exit(0);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
