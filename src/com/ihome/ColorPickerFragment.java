package com.ihome;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.at.ihome.R;

/**
 * Created by Anand.Tamariya on 27-Jun-15.
 */
public class ColorPickerFragment extends Fragment {

    private Bitmap bMap;

    public interface Handler {
        void setColor(int color);
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
        final View view = inflater.inflate(R.layout.color_picker, container, false);
        if (handler != null)
        {
            final ImageView marker = (ImageView) view.findViewById(R.id.marker);
            final View colorPanel = view.findViewById(R.id.colormap);

            int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
            colorPanel.measure(specWidth, specWidth);
            bMap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.colormap);

            View.OnTouchListener flt = new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    float x = event.getX();
                    float y = event.getY();
                    if (x < 0) x = 0;
                    if (x > colorPanel.getWidth())
                        x = colorPanel.getWidth() - marker.getWidth();
                    if (y < 0) y = 0;
                    if (y > colorPanel.getHeight())
                        y = colorPanel.getHeight() - marker.getHeight();

                    int pixel = bMap.getPixel((int) x, (int) y); //the background of the layout goes here...

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            marker.setX(x);
                            marker.setY(y);
                            // Write your code to perform an action on down
                            Log.d("down ", x + " " + y);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            Log.d("move ", x + " " + y);
                            marker.setX(x);
                            marker.setY(y);
                            int inRed = Color.red(pixel);
                            int inBlue = Color.blue(pixel);
                            int inGreen = Color.green(pixel);
                            Log.d("Colors", "R:" + inRed + " G:" + inGreen + " B:" + inBlue);
                            // Write your code to perform an action on continuous touch move
                            break;
                        case MotionEvent.ACTION_UP:
                            handler.setColor(pixel);
                            Log.d("up ", x + " " + y);
                            // Write your code to perform an action on touch up
                            break;
                    }
                    return true;
                }
            };

            colorPanel.setOnTouchListener(flt);

        }
        return view;
    }

}