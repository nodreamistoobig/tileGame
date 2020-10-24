package com.example.tilegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Random;

public class TileView extends View {

    Paint p1 = new Paint();
    Paint p2 = new Paint();
    int n = 3;
    float x = 100, y = 100;
    float w,h,tileW, tileH, margin = 100;
    Paint [][] colors = new Paint[n][n];
    Boolean calculated = false;

    public TileView(Context context) {
        super(context);
        p1.setColor(Color.parseColor("#00CED1"));
        p2.setColor(Color.parseColor("#B0C4DE"));
    }

    public TileView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        p1.setColor(Color.parseColor("#00CED1"));
        p2.setColor(Color.parseColor("#B0C4DE"));
    }

    public TileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p1.setColor(Color.parseColor("#00CED1"));
        p2.setColor(Color.parseColor("#B0C4DE"));
    }


    public void calcParams (Canvas c){
        h = c.getHeight();
        w = c.getWidth();
        tileH = (h-(n+1)*margin)/n;
        tileW = (w-(n+1)*margin)/n;

        Random r = new Random();
        int same = 0;
        for (int i = 0; i<n; i++){
            for (int j = 0; j<n; j++){
                if (r.nextInt(2) == 1){
                    colors[i][j] = p1;
                    same++;
                }
                else
                    colors[i][j] = p2;
            }
        }
        if (same == n*n || same == 0)
            calcParams(c);
        calculated = true;
    }

    public void drawTiles(Canvas canvas){
        for (int i = 1; i<=n; i++){
            for (int j = 1; j<=n; j++){
                float x0 = i*margin + tileW*(i-1);
                float y0 = j*margin + tileH*(j-1);
                float x1 = i*margin + tileW*(i);
                float y1 = j*margin + tileH*(j);
                canvas.drawRect(x0, y0, x1, y1, colors[i-1][j-1]);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!calculated)
            calcParams(canvas);
        drawTiles(canvas);
    }

    public void negateColor(int row, int column){
        if (colors[row][column] == p1)
            colors[row][column]=p2;
        else
            colors[row][column]=p1;
    }

    public void changeColor(float x, float y){
        int row=0, column=0;
        while(x-margin>tileW){
            x=x-margin-tileW;
            row++;
        }
        while(y-margin>tileH){
            y=y-margin-tileH;
            column++;
        }

        if (x-margin>0 && y-margin>0) {
            for (int i = 0; i < n; i++) {
                negateColor(row, i);
                negateColor(i, column);
            }
            negateColor(row,column);
        }
    }

    public boolean isVictory(){
        int same = 0;
        for (int i = 0; i<n; i++) {
            for (int j = 0; j < n; j++) {
                if (colors[i][j]==p1)
                    same++;
            }
        }
        if (same == n*n || same == 0)
            return true;
        else
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isVictory()){
            x = event.getX();
            y = event.getY();
            changeColor(x,y);
            invalidate();
        }
        return super.onTouchEvent(event);
    }
}
