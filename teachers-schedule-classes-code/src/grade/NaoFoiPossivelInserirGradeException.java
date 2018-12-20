/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade;

/**
 *
 * @author Natan
 */
public class NaoFoiPossivelInserirGradeException extends Exception {

    /**
     * Creates a new instance of
     * <code>NaoFoiPossivelInserirGradeException</code> without detail message.
     */
    public NaoFoiPossivelInserirGradeException() {
    }

    /**
     * Constructs an instance of
     * <code>NaoFoiPossivelInserirGradeException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public NaoFoiPossivelInserirGradeException(String msg) {
        super(msg);
    }

    public NaoFoiPossivelInserirGradeException(String nomeProfessor, String codigoDisciplina) {
        super(String.format("Não foi possível inserir a disciplina na grade", codigoDisciplina));
    }
}