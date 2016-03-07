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
 * Builder for RichPolygon.
 */
public class RichPolygonOptions {
    private List<RichPoint> points = new ArrayList<>();
    private List<List<RichPoint>> holes = new ArrayList<>();
    private int zIndex = 0;
    private int strokeWidth = 1;
    private Paint.Cap strokeCap = Paint.Cap.ROUND;
    private Paint.Join strokeJoin = Paint.Join.MITER;
    private PathEffect pathEffect;
    private MaskFilter maskFilter;
    private boolean linearGradient = true;
    private Integer strokeColor = Color.BLACK;
    private boolean antialias = true;
    private boolean closed = true;
    private Shader strokeShader;
    private Shader fillShader;
    private Paint.Style style = Paint.Style.FILL_AND_STROKE;
    private Integer fillColor = Color.WHITE;

    public RichPolygonOptions(List<RichPoint> newPoints) {
        add(newPoints);
    }

    public RichPolygonOptions add(RichPoint newPoint) {
        if (newPoint != null) {
            points.add(newPoint);
        }
        return this;
    }

    public RichPolygonOptions add(List<RichPoint> newPoints) {
        if (newPoints != null) {
            for (RichPoint newPoint : newPoints) {
                add(newPoint);
            }
        }
        return this;
    }

    public RichPolygonOptions addHole(List<RichPoint> newHole) {
        if (newHole != null) {
            holes.add(newHole);
        }
        return this;
    }

    public RichPolygonOptions zIndex(int zIndex) {
        this.zIndex = zIndex;
        return this;
    }

    public RichPolygonOptions strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }

    public RichPolygonOptions strokeCap(Paint.Cap strokeCap) {
        this.strokeCap = strokeCap;
        return this;
    }

    public RichPolygonOptions strokeJoin(Paint.Join strokeJoin) {
        this.strokeJoin = strokeJoin;
        return this;
    }

    public RichPolygonOptions pathEffect(PathEffect pathEffect) {
        this.pathEffect = pathEffect;
        return this;
    }

    public RichPolygonOptions maskFilter(MaskFilter maskFilter) {
        this.maskFilter = maskFilter;
        return this;
    }

    public RichPolygonOptions linearGradient(boolean linearGradient) {
        this.linearGradient = linearGradient;
        return this;
    }

    public RichPolygonOptions strokeColor(Integer strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public RichPolygonOptions antialias(boolean antialias) {
        this.antialias = antialias;
        return this;
    }

    public RichPolygonOptions closed(boolean closed) {
        this.closed = closed;
        return this;
    }

    public RichPolygonOptions strokeShader(Shader strokeShader) {
        this.strokeShader = strokeShader;
        return this;
    }

    public RichPolygonOptions fillShader(Shader fillShader) {
        this.fillShader = fillShader;
        return this;
    }

    public RichPolygonOptions style(Paint.Style style) {
        this.style = style;
        return this;
    }

    public RichPolygonOptions fillColor(Integer fillColor) {
        this.fillColor = fillColor;
        return this;
    }

    public RichPolygon build() {
        return new RichPolygon(zIndex, points, holes, strokeWidth, strokeCap, strokeJoin,
                pathEffect, maskFilter, linearGradient, strokeColor, antialias, closed,
                strokeShader, fillShader, style, fillColor);
    }
}
