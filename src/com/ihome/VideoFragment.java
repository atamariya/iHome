package com.ihome;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.at.ihome.R;

/**
 * Created by Anand.Tamariya on 27-Jun-15.
 */
public class VideoFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_control, container, false);
    }
}