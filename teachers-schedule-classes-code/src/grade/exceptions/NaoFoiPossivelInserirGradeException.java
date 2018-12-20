/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package grade.exceptions;

/**
 *
 * @author Natan Macedo<natancmacedo@gmail.com>
 */
public class NaoFoiPossivelInserirGradeException extends Exception {

    public NaoFoiPossivelInserirGradeException() {
    }

    /**
     * @param msg the detail message.
     */
    public NaoFoiPossivelInserirGradeException(String msg) {
        super(msg);
    }
}
