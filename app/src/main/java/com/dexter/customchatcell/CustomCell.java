package com.dexter.customchatcell;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dexter.customchatcell.util.Config;
import com.dexter.customchatcell.util.MediaType;

/**
 * Created by 10644291 on 06-10-2017.
 */

public class CustomCell extends View {
    final int SELF=0;
    final int OTHERS=1;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    String mText = "";
    int drawable;
    int senderType;
    int messageType;
    TextPaint mTextPaint;
    StaticLayout mStaticLayout;
    ImageView imageView;
    int width;
    int height;
    Path path;
    Paint pathPaint;


    // use this constructor if creating CustomCell programmatically
    public CustomCell(Context context, MessageObject data) {
        super(context);
        SCREEN_HEIGHT = Config.getInstance().getScreenHeight();
        SCREEN_WIDTH = Config.getInstance().getScreenWidth();
        mText=data.getMessage();
        senderType=data.getSender();
        initView(messageType);
        initLabelView();
    }

    private void initView(int messageType) {
        switch (messageType){
            case MediaType.TEXT:
                initLabelView();
                break;
            case MediaType.IMAGE:
                initImageView();
                break;

        }
    }

    // this constructor is used when created from xml
    public CustomCell(Context context, AttributeSet attrs) {
        super(context, attrs);
        SCREEN_HEIGHT = Config.getInstance().getScreenHeight();
        SCREEN_WIDTH = Config.getInstance().getScreenWidth();
        initLabelView();
    }

    private void initLabelView() {
        pathPaint=new Paint();
        path=new Path();
        if (senderType==SELF)
            setPadding(dp(7),dp(7),dp(14),dp(7));
        else if (senderType==OTHERS)
            setPadding(dp(14),dp(7),dp(7),dp(7));

        mTextPaint = new TextPaint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(16 * getResources().getDisplayMetrics().density);
        mTextPaint.setColor(0xFF000000);

        // default to a single line of text
        int width = (int) mTextPaint.measureText(mText);
        mStaticLayout = new StaticLayout(mText, mTextPaint, (int) width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);

        // New API alternate
        //
        // StaticLayout.Builder builder = StaticLayout.Builder.obtain(mText, 0, mText.length(), mTextPaint, width)
        //        .setAlignment(Layout.Alignment.ALIGN_NORMAL)
        //        .setLineSpacing(1, 0) // multiplier, add
        //        .setIncludePad(false);
        // mStaticLayout = builder.build();
    }

    private void initImageView() {
        imageView=new ImageView(getContext());
        imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Tell the parent layout how big this view would like to be
        // but still respect any requirements (measure specs) that are passed down.
        if (senderType==SELF)
            setPadding(dp(7),dp(7),dp(14),dp(7));
        else if (senderType==OTHERS)
            setPadding(dp(14),dp(7),dp(7),dp(7));

        // determine the width
        int width;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthRequirement = MeasureSpec.getSize(widthMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthRequirement;
        } else {
            width = mStaticLayout.getWidth() + getPaddingLeft() + getPaddingRight();
            if (widthMode == MeasureSpec.AT_MOST) {
                if (width > widthRequirement) {
                    width = widthRequirement;
                    if (width>(SCREEN_WIDTH*3)/4)
                        width=(SCREEN_WIDTH*3)/4+dp(5);
                    // too long for a single line so relayout as multiline
                    if (messageType==MediaType.TEXT)
                        mStaticLayout = new StaticLayout(mText, mTextPaint, width, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0, false);
                    else if (messageType==MediaType.IMAGE)
                        imageView.setLayoutParams(new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                }
            }
        }

        // determine the height
        int height;
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightRequirement = MeasureSpec.getSize(heightMeasureSpec);
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightRequirement;
        } else {
            height = mStaticLayout.getHeight() + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightRequirement);
            }
        }
        //cell width and height
        this.width=width;
        this.height=height;
        // Required call: set width and height
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pathPaint.setColor(Color.CYAN);
        pathPaint.setAntiAlias(true);

        int offset=dp(7);
        int radius=10;

        //drawing background bubble on the canvas after adjusting for offset
        if (messageType==MediaType.TEXT) {
            if (senderType == SELF) {
                canvas.drawRoundRect(new RectF(0, 0, width - offset, height), radius, radius, pathPaint);
                path.moveTo(width - offset, (height - offset / 2));
                path.lineTo(width, height);
                path.lineTo(width - (2 * offset), height);
                path.lineTo(width - offset, height - offset / 2);
                path.close();
            } else {
                canvas.drawRoundRect(new RectF(0 + offset, 0, (width), height), radius, radius, pathPaint);
                path.moveTo(offset, height - offset / 2);
                path.lineTo(0, height);
                path.lineTo(2 * offset, height);
                path.lineTo(offset, height - offset / 2);
                path.close();
            }
            canvas.drawPath(path, pathPaint);
        }else if (messageType==MediaType.IMAGE){
            canvas.drawRoundRect(new RectF(0, 0, width , height), radius, radius, pathPaint);
        }

        // draw the text on the canvas after adjusting for padding
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        if (messageType==MediaType.TEXT)
            mStaticLayout.draw(canvas);
        else imageView.draw(canvas);
        canvas.restore();
    }

    //pixel to dp converter
    int dp(int pixel){
        return (int)(getResources().getDisplayMetrics().density*pixel);
    }
}
