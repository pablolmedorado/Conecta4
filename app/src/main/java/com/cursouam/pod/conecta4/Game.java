package com.cursouam.pod.conecta4;

import java.util.Random;

/**
 * Created by neopablinho on 29/10/15.
 */
public class Game {
    public static final int EMPTY = 0;
    public static final int P1 = 1;
    public static final int P2 = 2;

    public final int HEIGHT = 6;
    public final int WIDTH = 7;

    private int board[][];

    private int lastRow;
    private int lastColumn;

    private boolean partidaTerminada = false;

    private int emptyFields = HEIGHT*WIDTH;

    public Game() {
        board = new int[HEIGHT][WIDTH];
        initializeBoard();
    }

    public boolean sePuedeColocarFicha(int i, int j){
        if(this.board[i][j] != EMPTY){
            return false;
        }else{
            for(int curr_row=i+1; curr_row<HEIGHT; curr_row++){
                if(board[curr_row][j] == EMPTY){
                    return false;
                }
            }

            return true;
        }
    }

    public void juegaJugador(int row, int column, int player){
        if(player == P1 || player == P2 && getEmptyFields()>0){
            board[row][column]=player;
            setUltimoMovimiento(row,column);
            decrementEmptyFields();

            if(getEmptyFields()<1){
                setPartidaTerminada(true);
            }
        }
    }

    public void juegaMaquina(){
        if(getEmptyFields()>0){
            Random rand;
            int randomi, randomj;

            do {
                rand = new Random(System.currentTimeMillis());
                randomi = rand.nextInt(((HEIGHT-1) - 0) + 1) + 0;
                randomj = rand.nextInt(((WIDTH-1) - 0) + 1) + 0;
            }while (!sePuedeColocarFicha(randomi, randomj));

            board[randomi][randomj]=P2;
            setUltimoMovimiento(randomi,randomj);
            decrementEmptyFields();

            if(getEmptyFields()<1){
                setPartidaTerminada(true);
            }
        }
    }

    boolean comprobarFila(int row, int player){
        int contador = 0;
        for (int j = 0; j < WIDTH; j++) {
            if (this.board[row][j] == player) {
                contador++;
            } else {
                contador = 0;
            }
            if (contador == 4) {
                setPartidaTerminada(true);
                return true;
            }
        }

        return false;
    }

    boolean comprobarColumna(int col, int player){
        int contador = 0;
        for (int i = 0; i < HEIGHT; i++) {
            if (this.board[i][col] == player) {
                contador++;
            } else {
                contador = 0;
            }
            if (contador == 4) {
                setPartidaTerminada(true);
                return true;
            }
        }


        return false;
    }

    boolean comprobarDiagonal(int row, int col, int player){
        int auxrow = row;
        int auxcol = col;

        while(auxrow>0 && auxcol>0){
            auxrow--;
            auxcol--;
        }

        int contador = 0;
        while(auxrow<HEIGHT && auxcol<WIDTH){
            if (this.board[auxrow][auxcol] == player) {
                contador++;
            } else {
                contador = 0;
            }
            if (contador == 4) {
                setPartidaTerminada(true);
                return true;
            }
            auxrow++;
            auxcol++;
        }

        auxrow = row;
        auxcol = col;

        while(auxrow>0 && auxcol<(WIDTH-1)){
            auxrow--;
            auxcol++;
        }

        contador = 0;
        while(auxrow<HEIGHT && auxcol>0){
            if (this.board[auxrow][auxcol] == player) {
                contador++;
            } else {
                contador = 0;
            }
            if (contador == 4) {
                setPartidaTerminada(true);
                return true;
            }
            auxrow++;
            auxcol--;
        }


        return false;
    }

    boolean comprobarCuatro(int player){
        return comprobarFila(lastRow, player) || comprobarColumna(lastColumn, player) || comprobarDiagonal(lastRow, lastColumn, player);
    }

    public void initializeBoard(){
        for(int i=0; i<HEIGHT; i++){
            for(int j=0; j<WIDTH; j++){
                this.board[i][j] = EMPTY;
            }
        }
        this.emptyFields=HEIGHT*WIDTH;
        setPartidaTerminada(false);
    }

    public int[][] getBoard() {
        return board;
    }

    public int getEmptyFields() {
        return emptyFields;
    }

    public void decrementEmptyFields() {
        this.emptyFields--;
    }

    public boolean isPartidaTerminada() {
        return partidaTerminada;
    }

    private void setPartidaTerminada(boolean partidaTerminada) {
        this.partidaTerminada = partidaTerminada;
    }

    private void setUltimoMovimiento(int row, int column) {
        this.lastRow = row;
        this.lastColumn = column;
    }

}
