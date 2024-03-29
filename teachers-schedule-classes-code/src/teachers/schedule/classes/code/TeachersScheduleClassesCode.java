/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule.classes.code;

import cplex.TeacherScheduleProblem;
import grade.*;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class TeachersScheduleClassesCode {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ConfiguracaoGrade config = new ConfiguracaoGrade(9, 6, 5);
        GradeHorarios grade = new GradeHorarios(config);

        InformacoesArquivo infoFile = new InformacoesArquivo("../dados/DadosReunidos.ods",
                "Disponibilidade",
                "Disciplinas",
                "ResultadoProfessor",
                "ResultadoPeriodo");

        grade.preencherDadosProblema(infoFile);

        new TeacherScheduleProblem(grade).begin();
    }

}
