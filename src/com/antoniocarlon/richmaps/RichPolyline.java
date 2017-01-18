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
import android.graphics.LinearGradient;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Shader;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Represents a polyline to be drawn using rich symbology.
 */
public class RichPolyline extends RichShape {
    RichPolyline(final int zIndex,
                 final List<RichPoint> points,
                 final int strokeWidth,
                 final Paint.Cap strokeCap,
                 final Paint.Join strokeJoin,
                 final PathEffect pathEffect,
                 final MaskFilter maskFilter,
                 final Shader strokeShader,
                 final boolean linearGradient,
                 final Integer strokeColor,
                 final boolean antialias,
                 final boolean closed) {
        super(zIndex, points, strokeWidth, strokeCap, strokeJoin, pathEffect, maskFilter,
                strokeShader, linearGradient, strokeColor, antialias, closed);
    }

    @Override
    public void doDraw(final Bitmap bitmap, final Projection projection,
                       final int paddingLeft, final int paddingTop,
                       final int paddingRight, final int paddingBottom) {
        drawStroke(bitmap, projection, points, paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    protected void drawStroke(final Bitmap bitmap, final Projection projection,
                              final List<RichPoint> points2Draw,
                              final int paddingLeft, final int paddingTop,
                              final int paddingRight, final int paddingBottom) {
        Canvas canvas = new Canvas(bitmap);
        Paint paint = getDefaultStrokePaint();

        RichPoint firstPoint = null;
        boolean first = true;
        RichPoint lastPoint = null;
        for (RichPoint point : points2Draw) {
            paint = getDefaultStrokePaint();
            LatLng position = point.getPosition();
            if (position != null) {
                if (first) {
                    firstPoint = point;
                    first = false;
                }

                if (point.getColor() == null) {
                    point.color(strokeColor);
                }

                if (lastPoint != null) {
                    drawSegment(canvas, paint, projection, lastPoint, point,
                            paddingLeft, paddingTop, paddingRight, paddingBottom);
                }
                lastPoint = point;
            }
        }

        if (closed && firstPoint != null && lastPoint != null) {
            drawSegment(canvas, paint, projection, lastPoint, firstPoint,
                    paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    private void drawSegment(final Canvas canvas, final Paint paint,
                             final Projection projection,
                             final RichPoint from, final RichPoint to,
                             final int paddingLeft, final int paddingTop,
                             final int paddingRight, final int paddingBottom) {
        Point toScreenPoint = projection.toScreenLocation(to.getPosition());
        Point fromScreenPoint = projection.toScreenLocation(from.getPosition());

        int fromX = fromScreenPoint.x + paddingRight / 2 - paddingLeft / 2;
        int fromY = fromScreenPoint.y + paddingBottom / 2 - paddingTop / 2;
        int toX = toScreenPoint.x + paddingRight / 2 - paddingLeft / 2;
        int toY = toScreenPoint.y + paddingBottom / 2 - paddingTop / 2;

        if (linearGradient) {
            int[] colors = new int[]{from.getColor(), to.getColor()};
            paint.setShader(new LinearGradient(fromX, fromY, toX, toY,
                    colors, null, Shader.TileMode.CLAMP));
        } else {
            paint.setColor(from.getColor());
        }

        if (strokeShader != null) {
            paint.setShader(strokeShader);
        }

        canvas.drawLine(fromX, fromY, toX, toY, paint);
    }

    protected Paint getDefaultStrokePaint() {
        Paint paint = new Paint();

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(strokeColor);
        paint.setStrokeWidth(strokeWidth);
        paint.setAntiAlias(antialias);
        paint.setStrokeCap(strokeCap);
        paint.setStrokeJoin(strokeJoin);
        paint.setPathEffect(pathEffect);
        paint.setMaskFilter(maskFilter);

        return paint;
    }
}
