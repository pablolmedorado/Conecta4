package com.cursouam.pod.conecta4;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class C4Preference extends AppCompatActivity {
    public final static String PLAYER_1_NAME_KEY = "player1Name";
    public final static String PLAYER_1_NAME_DEFAULT = "Jugador 1";
    public final static String PLAYER_1_COLOR_KEY = "player1Color";
    public final static String PLAYER_1_COLOR_DEFAULT = "red";
    public final static String PLAYER_2_NAME_KEY = "player2Name";
    public final static String PLAYER_2_NAME_DEFAULT = "Jugador 2";
    public final static String PLAYER_2_COLOR_KEY = "player2Color";
    public final static String PLAYER_2_COLOR_DEFAULT = "orange";
    public final static String MUSIC_KEY = "music";
    public final static Boolean MUSIC_DEFAULT = false;
    public final static String MODE_KEY = "mode";
    public final static String MODE_DEFAULT = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_c4_preference);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        C4PreferenceFragment fragment = new C4PreferenceFragment();
        fragmentTransaction.replace(android.R.id.content, fragment);
        fragmentTransaction.commit();
    }
}
