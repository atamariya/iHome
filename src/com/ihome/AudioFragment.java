package com.ihome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.at.iHome.api.Context;
import com.at.ihome.R;

/**
 * Created by Anand.Tamariya on 27-Jun-15.
 */
public class AudioFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.audio_control, container, false);
    }

}