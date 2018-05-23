package teachers.schedule;

import Jama.Matrix;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author natan
 */
public class TipoDisponibilidade {
    
    public static Matrix classe(int classe) {
        Matrix disponibilidade = null;
        switch (classe) {
            case 0:
                disponibilidade = new Matrix(ZERO);
                break;
            case 1:
                disponibilidade = new Matrix(SEG_QUAR);
                break;
            case 2:
                disponibilidade = new Matrix(QUAR_SAB);
                break;
            case 3:
                disponibilidade = new Matrix(ALL);
                break;
        }
        
        return disponibilidade;
    }
    
    private static final double[][] ZERO={
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    private static final double[][] ALL = {
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1}
    };

    private static final double[][] SEG_QUAR = {
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0}
    };

    private static final double[][] QUAR_SAB = {
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1}
    };

}
