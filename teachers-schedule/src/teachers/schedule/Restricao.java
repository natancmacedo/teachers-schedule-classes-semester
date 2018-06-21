/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teachers.schedule;

import Jama.Matrix;

/**
 *
 * @author natan
 */
public class Restricao {

    private Matrix disponibilidade;

    public Restricao(Matrix dispoMatrix) {
        this.disponibilidade = dispoMatrix;
    }

    public Restricao(double[][] dispoMatrix) {
        this.disponibilidade = new Matrix(dispoMatrix);
    }

    public Matrix getDisponibilidade() {
        return disponibilidade;
    }

    public void somaRestricao(Matrix disponibilidade) {
        this.disponibilidade.plusEquals(disponibilidade);
    }

    public void zeraRestricao() {
        this.disponibilidade.timesEquals(0);
    }
    
    /**
     * Verifica se um horário está disponível
     * @param dia
     * @param hora
     * @return true ou false
     */
    public Boolean horarioEstaDisponivel(Integer dia, Integer hora){
        return this.disponibilidade.getArray()[hora][dia]>0;
    }
    
    /**
     * Verifica se horários em sequencia no mesmo dia estão disponíveis
     * @param dia
     * @param horaInicio
     * @param horaFinal
     * @return 
     */
    public Boolean horariosEstaoDisponiveis(Integer dia, Integer horaInicio, Integer horaFinal){
        for(int i=horaInicio; i<=horaFinal;i++){
            if(!this.horarioEstaDisponivel(dia, i)){
                return false;
            }
        }
        return true;
    }
    
    
    public void mostrarRestricao() {

        double disponibilidadeDouble[][] = this.disponibilidade.getArray();
        System.out.println("Seg   Ter    Quar    Qui    Sex    Sab");
        for (int i = 0; i < disponibilidadeDouble.length; i++) {
            for (int j = 0; j < disponibilidadeDouble[0].length; j++) {
                System.out.print(disponibilidadeDouble[i][j] + "    ");
            }
            System.out.println();
        }
    }
    
    public double quantidadeDeHorariosDisponiveis(){
        double contadorHorarios=0;
        for(int i=0;i<this.disponibilidade.getColumnDimension();i++){
            for(int j=0;j<this.disponibilidade.getRowDimension();j++){
                if(this.disponibilidade.get(j, i)>0){
                    contadorHorarios++;
                }
            }
        }
        return contadorHorarios;
    }
    
}
