package com.ihome;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.at.ihome.R;

/**
 * Created by Anand.Tamariya on 27-Jun-15.
 */
public class VideoFragment extends Fragment {
    public interface Handler {
        void previous();
        void play();
        void pause();
        void stop();
        void next();
        void rewind();
        void forward();
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
        View view = inflater.inflate(R.layout.video_control, container, false);
        if (handler != null) {
            ImageButton button = (ImageButton) view.findViewById(R.id.previousButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.previous();
                }
            });

            button = (ImageButton) view.findViewById(R.id.playButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.play();
                }
            });

            button = (ImageButton) view.findViewById(R.id.pauseButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.pause();
                }
            });

            button = (ImageButton) view.findViewById(R.id.stopButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.stop();
                }
            });

            button = (ImageButton) view.findViewById(R.id.nextButton);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.next();
                }
            });

            button = (ImageButton) view.findViewById(R.id.rewind);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.rewind();
                }
            });

            button = (ImageButton) view.findViewById(R.id.forward);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.forward();
                }
            });
        }
        return view;
    }
}