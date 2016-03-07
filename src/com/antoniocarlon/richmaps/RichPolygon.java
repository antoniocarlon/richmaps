/*
 * Copyright 2016 ANTONIO CARLON
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.antoniocarlon.richmaps;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a polygon to be drawn using rich symbology.
 */
public class RichPolygon extends RichPolyline {
    private Shader fillShader;
    private Paint.Style style = Paint.Style.FILL_AND_STROKE;
    private Integer fillColor = Color.WHITE;
    private List<List<RichPoint>> holes = new ArrayList<>();

    RichPolygon(int zIndex,
                List<RichPoint> points,
                List<List<RichPoint>> holes,
                int strokeWidth,
                Paint.Cap strokeCap,
                Paint.Join strokeJoin,
                PathEffect pathEffect,
                MaskFilter maskFilter,
                boolean linearGradient,
                Integer strokeColor,
                boolean antialias,
                boolean closed,
                Shader strokeShader,
                Shader fillShader,
                Paint.Style style,
                Integer fillColor) {
        super(zIndex, points, strokeWidth, strokeCap, strokeJoin, pathEffect, maskFilter,
                strokeShader, linearGradient, strokeColor, antialias, closed);
        this.fillShader = fillShader;
        this.style = style;
        this.fillColor = fillColor;
        if (holes != null) {
            addHoles(holes);
        }
    }

    @Override
    public void doDraw(final Bitmap bitmap, final Projection projection) {
        if (style == Paint.Style.FILL || style == Paint.Style.FILL_AND_STROKE) {
            drawFill(bitmap, projection, points);
            for (List<RichPoint> hole : holes) {
                drawHole(bitmap, projection, hole);
            }
        }

        if (style == Paint.Style.STROKE || style == Paint.Style.FILL_AND_STROKE) {
            drawStroke(bitmap, projection, points);
            for (List<RichPoint> hole : holes) {
                drawStroke(bitmap, projection, hole);
            }
        }
    }

    public void addHoles(List<List<RichPoint>> holes) {
        if (holes != null) {
            for (List<RichPoint> hole : holes) {
                addHole(hole);
            }
        }
    }

    public void addHole(List<RichPoint> hole) {
        if (hole != null) {
            holes.add(hole);
        }
    }

    protected void drawFill(final Bitmap bitmap, final Projection projection,
                            final List<RichPoint> points2Draw) {
        Canvas canvas = new Canvas(bitmap);
        Path linePath = new Path();
        boolean firstPoint = true;
        for (RichPoint point : points2Draw) {
            LatLng position = point.getPosition();
            if (position != null) {
                Point bmpPoint = projection.toScreenLocation(position);
                if (firstPoint) {
                    linePath.moveTo(bmpPoint.x, bmpPoint.y);
                    firstPoint = false;
                } else {
                    linePath.lineTo(bmpPoint.x, bmpPoint.y);
                }
            }
        }

        Paint paint = getDefaultFillPaint();
        if (fillShader != null) {
            paint.setShader(fillShader);
        }
        canvas.drawPath(linePath, paint);
    }

    protected void drawHole(final Bitmap bitmap, final Projection projection,
                            final List<RichPoint> points2Draw) {
        Canvas canvas = new Canvas(bitmap);
        Path linePath = new Path();
        boolean firstPoint = true;
        for (RichPoint point : points2Draw) {
            LatLng position = point.getPosition();
            if (position != null) {
                Point bmpPoint = projection.toScreenLocation(position);
                if (firstPoint) {
                    linePath.moveTo(bmpPoint.x, bmpPoint.y);
                    firstPoint = false;
                } else {
                    linePath.lineTo(bmpPoint.x, bmpPoint.y);
                }
            }
        }

        Paint paint = getDefaultHolePaint();
        canvas.drawPath(linePath, paint);
    }

    private Paint getDefaultFillPaint() {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(fillColor);
        paint.setAntiAlias(antialias);

        return paint;
    }

    private Paint getDefaultHolePaint() {
        Paint paint = new Paint();

        paint.setAlpha(0);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        paint.setAntiAlias(antialias);

        return paint;
    }
}