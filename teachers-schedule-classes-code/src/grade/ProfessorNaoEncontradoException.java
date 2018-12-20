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
public class ProfessorNaoEncontradoException extends Exception {

    /**
     * Creates a new instance of <code>ProfessorNaoEncontratoException</code>
     * without detail message.
     */
    public ProfessorNaoEncontradoException() {
    }

    /**
     * Constructs an instance of <code>ProfessorNaoEncontratoException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ProfessorNaoEncontradoException(String msg) {
        super(msg);
    }

    public ProfessorNaoEncontradoException(String nomeProfessor, String codigoDisciplina) {
        super(String.format("Não foi possível encontrar o professor: %s para a disciplina %s", nomeProfessor, codigoDisciplina));
    }

}
