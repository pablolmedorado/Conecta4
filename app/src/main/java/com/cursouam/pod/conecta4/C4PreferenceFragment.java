package com.cursouam.pod.conecta4;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by neopablinho on 25/11/15.
 */
public class C4PreferenceFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
