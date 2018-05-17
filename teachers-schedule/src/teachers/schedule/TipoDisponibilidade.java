package teachers.schedule;

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

    public static Integer[][] classe(int classe) {
        Integer disponibilidade[][] = null;
        switch (classe) {
            case 0:
                disponibilidade = ZERO;
                break;
            case 1:
                disponibilidade = SEG_QUAR;
                break;
            case 2:
                disponibilidade = QUAR_SAB;
                break;
            case 3:
                disponibilidade = ALL;
                break;
        }
        
        return disponibilidade;
    }
    
    private static final Integer[][] ZERO={
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };

    private static final Integer[][] ALL = {
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1}
    };

    private static final Integer[][] SEG_QUAR = {
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0},
        {1, 1, 1, 0, 0, 0}
    };

    private static final Integer[][] QUAR_SAB = {
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1},
        {0, 0, 1, 1, 1, 1}
    };

}
