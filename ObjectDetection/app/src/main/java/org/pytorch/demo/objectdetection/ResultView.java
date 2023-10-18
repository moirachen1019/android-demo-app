// Copyright (c) 2020 Facebook, Inc. and its affiliates.
// All rights reserved.
//
// This source code is licensed under the BSD-style license found in the
// LICENSE file in the root directory of this source tree.

package org.pytorch.demo.objectdetection;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;


public class ResultView extends View {

    private final static int TEXT_X = 40;
    private final static int TEXT_Y = 35;
    private final static int TEXT_WIDTH = 260;
    private final static int TEXT_HEIGHT = 50;

    private Paint mPaintRectangle;
    private Paint mPaintText;
    private ArrayList<Result> mResults;

    private ResultView mResultView;
    private String location = "";
    private int centerThreshold = 200;

    public ResultView(Context context) {
        super(context);
    }

    public ResultView(Context context, AttributeSet attrs){
        super(context, attrs);
        mPaintRectangle = new Paint();
        mPaintRectangle.setColor(Color.YELLOW);
        mPaintText = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mResults == null) return;
        for (Result result : mResults) {

            mResultView = findViewById(R.id.resultView);
            int centerX = mResultView.getWidth() / 2;
            int centerY = mResultView.getHeight() / 2;

            int rectCenterX = (result.rect.left + result.rect.right) / 2;
            int rectCenterY = (result.rect.top + result.rect.bottom) / 2;

            int offsetX = centerX - rectCenterX;
            int offsetY = centerY - rectCenterY;

            if (Math.abs(offsetX) <= centerThreshold && Math.abs(offsetY) <= centerThreshold) {
                location = "找到！";
            } else if (Math.abs(offsetX) > Math.abs(offsetY)){
                if (offsetX > centerThreshold) {
                    location = "往左移";
                } else if (offsetX < -centerThreshold) {
                    location = "往右移";
                }
            } else {
                if (offsetY > centerThreshold) {
                    location = "往上移";
                } else if (offsetY < -centerThreshold) {
                    location = "往下移";
                }
            }

            mPaintRectangle.setStrokeWidth(5);
            mPaintRectangle.setStyle(Paint.Style.STROKE);
            canvas.drawRect(result.rect, mPaintRectangle);

            Path mPath = new Path();
            RectF mRectF = new RectF(result.rect.left, result.rect.top, result.rect.left + TEXT_WIDTH,  result.rect.top + TEXT_HEIGHT);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPaintText.setColor(Color.MAGENTA);
            canvas.drawPath(mPath, mPaintText);

            mPaintText.setColor(Color.WHITE);
            mPaintText.setStrokeWidth(0);
            mPaintText.setStyle(Paint.Style.FILL);
            mPaintText.setTextSize(50);
            canvas.drawText(String.format("%s %.2f %s", PrePostProcessor.mClasses[result.classIndex], result.score, location), result.rect.left + TEXT_X, result.rect.top + TEXT_Y, mPaintText);
        }
    }

    public void setResults(ArrayList<Result> results) {
        mResults = results;
    }
}
