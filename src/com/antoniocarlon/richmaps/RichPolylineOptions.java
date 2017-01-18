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

import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder for RichPolyline.
 */
public class RichPolylineOptions {
    private List<RichPoint> points = new ArrayList<>();
    private int zIndex = 0;
    private int strokeWidth = 1;
    private Paint.Cap strokeCap = Paint.Cap.ROUND;
    private Paint.Join strokeJoin = Paint.Join.MITER;
    private PathEffect pathEffect;
    private MaskFilter maskFilter;
    private Shader strokeShader;
    private boolean linearGradient = true;
    private Integer strokeColor = Color.BLACK;
    private boolean antialias = true;
    private boolean closed = false;

    public RichPolylineOptions(final List<RichPoint> newPoints) {
        add(newPoints);
    }

    public RichPolylineOptions add(final RichPoint newPoint) {
        if (newPoint != null) {
            points.add(newPoint);
        }
        return this;
    }

    public RichPolylineOptions add(final List<RichPoint> newPoints) {
        if (newPoints != null) {
            for (RichPoint newPoint : newPoints) {
                add(newPoint);
            }
        }
        return this;
    }

    public RichPolylineOptions zIndex(final int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public RichPolylineOptions strokeWidth(final int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public RichPolylineOptions strokeCap(final Paint.Cap strokeCap) {
        this.strokeCap = strokeCap;
        return this;
    }

    public RichPolylineOptions strokeJoin(final Paint.Join strokeJoin) {
        this.strokeJoin = strokeJoin;
        return this;
    }

    public RichPolylineOptions pathEffect(final PathEffect pathEffect) {
        this.pathEffect = pathEffect;
        return this;
    }

    public RichPolylineOptions maskFilter(final MaskFilter maskFilter) {
        this.maskFilter = maskFilter;
        return this;
    }

    public RichPolylineOptions strokeShader(final Shader strokeShader) {
        this.strokeShader = strokeShader;
        return this;
    }

    public RichPolylineOptions linearGradient(final boolean linearGradient) {
        this.linearGradient = linearGradient;
        return this;
    }

    public RichPolylineOptions strokeColor(final Integer strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public RichPolylineOptions antialias(final boolean antialias) {
        this.antialias = antialias;
        return this;
    }

    public RichPolylineOptions closed(final boolean closed) {
        this.closed = closed;
        return this;
    }

    public RichPolyline build() {
        return new RichPolyline(zIndex, points, strokeWidth, strokeCap, strokeJoin, pathEffect,
                maskFilter, strokeShader, linearGradient, strokeColor, antialias, closed);
    }
}
