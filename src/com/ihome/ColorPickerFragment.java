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
        final View colorPanel = inflater.inflate(R.layout.color_picker, container, false);
//        if (handler != null)
        {
            final ImageView marker = (ImageView) colorPanel.findViewById(R.id.marker);

            int specWidth = View.MeasureSpec.makeMeasureSpec(0 /* any */, View.MeasureSpec.UNSPECIFIED);
            colorPanel.measure(specWidth, specWidth);
            final Bitmap bMap = Bitmap.createBitmap(colorPanel.getMeasuredWidth(), colorPanel.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

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

                    int pixel = bMap.getPixel((int) 50, (int) 50); //the background of the layout goes here...

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
                            // Write your code to perform an action on contineus touch move
                            break;
                        case MotionEvent.ACTION_UP:
                            Log.d("up ", x + " " + y);
                            // Write your code to perform an action on touch up
                            break;
                    }
                    return true;
                }
            };

            colorPanel.setOnTouchListener(flt);

        }
        return colorPanel;
    }

}