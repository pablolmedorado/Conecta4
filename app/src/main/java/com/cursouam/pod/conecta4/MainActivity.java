package com.cursouam.pod.conecta4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final int SOLITARIO = 1;
    public static final int VS = 2;

    private Game game;
    private int modo;

    private int currentPlayer;

    private SharedPreferences sp;

    private int fichaJugador1, fichaJugador2;

    private final int[][] ids = {
            {R.id.f0c0, R.id.f0c1, R.id.f0c2, R.id.f0c3, R.id.f0c4, R.id.f0c5, R.id.f0c6},
            {R.id.f1c0, R.id.f1c1, R.id.f1c2, R.id.f1c3, R.id.f1c4, R.id.f1c5, R.id.f1c6},
            {R.id.f2c0, R.id.f2c1, R.id.f2c2, R.id.f2c3, R.id.f2c4, R.id.f2c5, R.id.f2c6},
            {R.id.f3c0, R.id.f3c1, R.id.f3c2, R.id.f3c3, R.id.f3c4, R.id.f3c5, R.id.f3c6},
            {R.id.f4c0, R.id.f4c1, R.id.f4c2, R.id.f4c3, R.id.f4c4, R.id.f4c5, R.id.f4c6},
            {R.id.f5c0, R.id.f5c1, R.id.f5c2, R.id.f5c3, R.id.f5c4, R.id.f5c5, R.id.f5c6}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        this.game = new Game();
        this.sp = PreferenceManager.getDefaultSharedPreferences(this);
        if(this.sp.contains(C4Preference.MODE_KEY)){
            String modo = this.sp.getString(C4Preference.MODE_KEY, C4Preference.MODE_DEFAULT);
            setModo(Integer.parseInt(modo));
        }else{
            setModo(SOLITARIO);
        }

        if(this.sp.contains(C4Preference.PLAYER_1_COLOR_KEY) && this.sp.contains(C4Preference.PLAYER_2_COLOR_KEY)){
            String colorp1 = this.sp.getString(C4Preference.PLAYER_1_COLOR_KEY, C4Preference.PLAYER_1_COLOR_DEFAULT);
            this.fichaJugador1 = this.getIdDrawable(colorp1);
            String colorp2 = this.sp.getString(C4Preference.PLAYER_2_COLOR_KEY, C4Preference.PLAYER_2_COLOR_DEFAULT);
            this.fichaJugador2 = this.getIdDrawable(colorp2);

        }else{
            this.fichaJugador1 = R.drawable.c4_button_p1;
            this.fichaJugador2 = R.drawable.c4_button_p2;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        this.sp = PreferenceManager.getDefaultSharedPreferences(this);

        if(sp.contains(C4Preference.MODE_KEY)){
            String modo_pref = sp.getString(C4Preference.MODE_KEY, C4Preference.MODE_DEFAULT);
            if(this.modo != Integer.parseInt(modo_pref)){
                cambiarModo();
            }
        }

        if(this.sp.contains(C4Preference.PLAYER_1_COLOR_KEY) && this.sp.contains(C4Preference.PLAYER_2_COLOR_KEY)){
            String colorp1 = this.sp.getString(C4Preference.PLAYER_1_COLOR_KEY, C4Preference.PLAYER_1_COLOR_DEFAULT);
            String colorp2 = this.sp.getString(C4Preference.PLAYER_2_COLOR_KEY, C4Preference.PLAYER_2_COLOR_DEFAULT);
            if(this.fichaJugador1 != this.getIdDrawable(colorp1) || this.fichaJugador2 != this.getIdDrawable(colorp2)){
                this.fichaJugador1 = this.getIdDrawable(colorp1);
                this.fichaJugador2 = this.getIdDrawable(colorp2);
                this.dibujarTablero();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.reiniciar_menu:
                String title = getResources().getString(R.string.reiniciar);
                String message = getResources().getString(R.string.reiniciar_confirm);
                RestartDialog rd = new RestartDialog(title, message);
                rd.show(getFragmentManager(), "DIALOG");
                return true;
            case R.id.ajustes_menu:
                startActivity(new Intent(this, C4Preference.class));
                return true;
            case R.id.about_menu:
                AboutDialog ad = new AboutDialog();
                ad.show(getFragmentManager(), "DIALOG");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private int deIdentificadorAFila(int id) {
        for (int i = 0; i < this.game.HEIGHT; i++) {
            for (int j = 0; j < this.game.WIDTH; j++) {
                if (this.ids[i][j] == id) {
                    return i;
                }
            }
        }

        return -1;
    }

    private int deIdentificadorAColumna(int id) {
        for (int i = 0; i < this.game.HEIGHT; i++) {
            for (int j = 0; j < this.game.WIDTH; j++) {
                if (this.ids[i][j] == id) {
                    return j;
                }
            }
        }

        return -1;
    }

    public void pulsado(View v) {
        String title, message, nombreJugador;
        RestartDialog rd;

        if (!this.game.isPartidaTerminada()) {
            ImageButton button = (ImageButton) v;
            int row = deIdentificadorAFila(button.getId());
            int column = deIdentificadorAColumna(button.getId());

            if (this.game.sePuedeColocarFicha(row, column)) {
                this.game.juegaJugador(row, column, this.getCurrentPlayer());
                dibujarTablero();
                if (this.game.comprobarCuatro(this.getCurrentPlayer())) {
                    this.sp = PreferenceManager.getDefaultSharedPreferences(this);
                    nombreJugador = new String();

                    if(this.getCurrentPlayer() == Game.P1){
                        if(this.sp.contains(C4Preference.PLAYER_1_NAME_KEY)) {
                            nombreJugador = this.sp.getString(C4Preference.PLAYER_1_NAME_KEY, C4Preference.PLAYER_1_NAME_DEFAULT);
                        }else{
                            nombreJugador = C4Preference.PLAYER_1_NAME_DEFAULT;
                        }
                    } else if(this.getCurrentPlayer() == Game.P2){
                        if(this.sp.contains(C4Preference.PLAYER_2_NAME_KEY)) {
                            nombreJugador = this.sp.getString(C4Preference.PLAYER_2_NAME_KEY, C4Preference.PLAYER_2_NAME_DEFAULT);
                        }else{
                            nombreJugador = C4Preference.PLAYER_2_NAME_DEFAULT;
                        }
                    }

                    title = nombreJugador + " " + getResources().getString(R.string.ganaLaPartida);
                    message = getResources().getString(R.string.reiniciar_confirm);
                    rd = new RestartDialog(title, message);
                    rd.show(getFragmentManager(), "DIALOG");
                }else if(this.game.isPartidaTerminada()){
                    title = getResources().getString(R.string.partidaTerminada);
                    message = getResources().getString(R.string.reiniciar_confirm);
                    rd = new RestartDialog(title, message);
                    rd.show(getFragmentManager(), "DIALOG");
                }else{
                    switch (this.modo){
                        case SOLITARIO:
                            this.game.juegaMaquina();
                            dibujarTablero();
                            if (this.game.comprobarCuatro(Game.P2)) {
                                this.sp = PreferenceManager.getDefaultSharedPreferences(this);
                                nombreJugador = new String();

                                if(this.sp.contains(C4Preference.PLAYER_2_NAME_KEY)) {
                                    nombreJugador = this.sp.getString(C4Preference.PLAYER_2_NAME_KEY, C4Preference.PLAYER_2_NAME_DEFAULT);
                                }else{
                                    nombreJugador = C4Preference.PLAYER_2_NAME_DEFAULT;
                                }

                                title = nombreJugador + " " + getResources().getString(R.string.ganaLaPartida);
                                message = getResources().getString(R.string.reiniciar_confirm);
                                rd = new RestartDialog(title, message);
                                rd.show(getFragmentManager(), "DIALOG");
                            }else if(this.game.isPartidaTerminada()){
                                title = getResources().getString(R.string.partidaTerminada);
                                message = getResources().getString(R.string.reiniciar_confirm);
                                rd = new RestartDialog(title, message);
                                rd.show(getFragmentManager(), "DIALOG");
                            }
                            break;
                        case VS:
                            this.pasarTurno();
                            break;
                    }
                }
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.posicionIlegal), Toast.LENGTH_SHORT).show();
            }
        }else{
            title = getResources().getString(R.string.partidaTerminada);
            message = getResources().getString(R.string.reiniciar_confirm);
            rd = new RestartDialog(title, message);
            rd.show(getFragmentManager(), "DIALOG");
        }
    }

    private void pasarTurno(){
        if(this.getCurrentPlayer() == Game.P1){
            this.setCurrentPlayer(Game.P2);
        } else if (this.getCurrentPlayer() == Game.P2){
            this.setCurrentPlayer(Game.P1);
        }
    }

    private void cambiarModo(){
        if(this.modo == SOLITARIO){
            setModo(VS);
        } else if (this.modo == VS){
            setModo(SOLITARIO);
        }
        this.game.initializeBoard();
        this.dibujarTablero();
    }

    private void setModo(int modo){
        TextView textView = (TextView) findViewById(R.id.modo);

        switch (modo){
            case SOLITARIO:
                this.modo = SOLITARIO;
                textView.setText(getResources().getText(R.string.modo1jugador));
                break;
            case VS:
                this.modo = VS;
                textView.setText(getResources().getText(R.string.modo2jugadores));
                break;
        }

        this.setCurrentPlayer(this.game.P1);
    }

    public void dibujarTablero() {
        int[][] board = this.game.getBoard();
        for (int i = 0; i < this.game.HEIGHT; i++) {
            for (int j = 0; j < this.game.WIDTH; j++) {
                ImageButton button = (ImageButton) findViewById(ids[i][j]);
                switch (board[i][j]) {
                    case Game.EMPTY:
                        button.setImageResource(R.drawable.c4_button);
                        break;
                    case Game.P1:
                        button.setImageResource(this.fichaJugador1);
                        break;
                    case Game.P2:
                        button.setImageResource(this.fichaJugador2);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public Game getGame() {
        return this.game;
    }

    public int getCurrentPlayer() {
        return this.currentPlayer;
    }

    public void setCurrentPlayer(int currentPlayer) {
        if(currentPlayer == Game.P1 || currentPlayer == Game.P2){
            this.currentPlayer = currentPlayer;
        }
    }

    private int getIdDrawable(String color){
        int id = -1;
        switch (color){
            case "red":
                id = R.drawable.c4_button_p1;
                break;
            case "orange":
                id = R.drawable.c4_button_p2;
                break;
        }
        return id;
    }
}
