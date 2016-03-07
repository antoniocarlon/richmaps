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
    RichPolyline(int zIndex,
                 List<RichPoint> points,
                 int strokeWidth,
                 Paint.Cap strokeCap,
                 Paint.Join strokeJoin,
                 PathEffect pathEffect,
                 MaskFilter maskFilter,
                 Shader strokeShader,
                 boolean linearGradient,
                 Integer strokeColor,
                 boolean antialias,
                 boolean closed) {
        super(zIndex, points, strokeWidth, strokeCap, strokeJoin, pathEffect, maskFilter,
                strokeShader, linearGradient, strokeColor, antialias, closed);
    }

    @Override
    public void doDraw(final Bitmap bitmap, final Projection projection) {
        drawStroke(bitmap, projection, points);
    }

    protected void drawStroke(final Bitmap bitmap, final Projection projection,
                              final List<RichPoint> points2Draw) {
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
                    drawSegment(canvas, paint, projection, lastPoint, point);
                }
                lastPoint = point;
            }
        }

        if (closed && firstPoint != null && lastPoint != null) {
            drawSegment(canvas, paint, projection, lastPoint, firstPoint);
        }
    }

    private void drawSegment(final Canvas canvas, final Paint paint,
                             final Projection projection,
                             RichPoint from, RichPoint to) {
        Point toScreenPoint = projection.toScreenLocation(to.getPosition());
        Point fromScreenPoint = projection.toScreenLocation(from.getPosition());

        if (linearGradient) {
            int[] colors = new int[]{from.getColor(), to.getColor()};
            paint.setShader(new LinearGradient(fromScreenPoint.x, fromScreenPoint.y,
                    toScreenPoint.x, toScreenPoint.y,
                    colors, null, Shader.TileMode.CLAMP));
        } else {
            paint.setColor(from.getColor());
        }

        if (strokeShader != null) {
            paint.setShader(strokeShader);
        }

        canvas.drawLine(fromScreenPoint.x, fromScreenPoint.y,
                toScreenPoint.x, toScreenPoint.y,
                paint);
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
