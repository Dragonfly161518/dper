package com.pornattapat.dper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class CustomImageView extends android.support.v7.widget.AppCompatImageView implements View.OnTouchListener {



    private Bitmap bitmap;

    private OnClickListener listener;

    private boolean clicking;


    private int pressedColor;



    public CustomImageView(Context context) {

        super(context);

        init();

    }



    public CustomImageView(Context context, AttributeSet attrs) {

        super(context, attrs);

        init();

    }



    public CustomImageView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        init();

    }



    @Override

    public void setOnClickListener(OnClickListener l) {

        listener = l;

        setOnTouchListener(this);

    }



    @Override

    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (isInClickArea(motionEvent)) {

            if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {

                clicking = true;

                setColorFilter(pressedColor, PorterDuff.Mode.MULTIPLY);

            } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {

                if (clicking) {

                    clicking = false;

                    setColorFilter(null);

                    listener.onClick(view);

                }

            }

            return true;

        }

        clicking = false;

        setColorFilter(null);



        return false;

    }



    private boolean isInClickArea(MotionEvent event) {

        int clickX = (int)(event.getX() * ((double) bitmap.getWidth() / (double) getWidth()));

        int clickY = (int)(event.getY() * ((double) bitmap.getHeight() / (double) getHeight()));

        if (bitmap != null) {

            if (clickX >= 0 && clickX < bitmap.getWidth() && clickY >= 0 && clickY < bitmap.getHeight()) {

                if (Color.alpha(bitmap.getPixel(clickX, clickY)) > 0) {

                    return true;

                }

            }

        }

        return false;

    }



    private void init() {

        int color = (int)(255 * 0.96f);

        pressedColor = Color.rgb(color, color, color);

        Drawable drawable = getDrawable();

        if (drawable != null) {

            if (drawable instanceof BitmapDrawable) {

                bitmap = ((BitmapDrawable) drawable).getBitmap();

            } else if (drawable instanceof LayerDrawable) {

                bitmap = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);

                drawable.setBounds(0, 0, 128, 128);

                drawable.draw(new Canvas(bitmap));

            }

        }

    }



}