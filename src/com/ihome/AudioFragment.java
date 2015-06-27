package com.ihome;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.at.iHome.api.Context;
import com.at.ihome.R;

/**
 * Created by Anand.Tamariya on 27-Jun-15.
 */
public class AudioFragment extends Fragment {
    public interface Handler {
        void mute();
        void unmute();
        void up();
        void down();
    }

    Handler handler;
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof Handler) {
            handler = (Handler) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.audio_control, container, false);
        if (handler != null) {
            ImageButton button = (ImageButton) view.findViewById(R.id.upButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.up();
                }
            });

            button = (ImageButton) view.findViewById(R.id.downButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.down();
                }
            });

            button = (ImageButton) view.findViewById(R.id.mutedButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.mute();
                }
            });

            button = (ImageButton) view.findViewById(R.id.onButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.unmute();
                }
            });
        }
        return view;
    }

}