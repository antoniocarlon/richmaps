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
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.Shader;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLngBounds;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a shape to be drawn using rich symbology.
 */
public abstract class RichShape {
    protected int zIndex = 0;

    protected List<RichPoint> points = new ArrayList<>();

    protected int strokeWidth = 1;
    protected Paint.Cap strokeCap = Paint.Cap.ROUND;
    protected Paint.Join strokeJoin = Paint.Join.MITER;
    protected PathEffect pathEffect;
    protected MaskFilter maskFilter;
    protected Shader strokeShader;
    protected boolean linearGradient = true;
    protected Integer strokeColor = Color.BLACK;
    protected boolean antialias = true;
    protected boolean closed = false;

    RichShape(final int zIndex,
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
        this.zIndex = zIndex;
        this.strokeWidth = strokeWidth;
        this.strokeCap = strokeCap;
        this.strokeJoin = strokeJoin;
        this.pathEffect = pathEffect;
        this.maskFilter = maskFilter;
        this.strokeShader = strokeShader;
        this.linearGradient = linearGradient;
        this.strokeColor = strokeColor;
        this.antialias = antialias;
        this.closed = closed;
        if (points != null) {
            for (RichPoint point : points) {
                add(point);
            }
        }
    }

    public RichShape add(final RichPoint point) {
        if (point != null) {
            if (point.getColor() == null) {
                point.color(strokeColor);
            }
            points.add(point);
        }
        return this;
    }

    public int getZIndex() {
        return this.zIndex;
    }

    public LatLngBounds getBounds() {
        if (points.isEmpty()) {
            return null;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (RichPoint point : points) {
            if (point.getPosition() != null) {
                builder.include(point.getPosition());
            }
        }
        return builder.build();
    }

    protected abstract void doDraw(final Bitmap bitmap, final Projection projection,
                                   final int paddingLeft, final int paddingTop,
                                   final int paddingRight, final int paddingBottom);

    public void draw(final Bitmap bitmap, final Projection projection,
                     final int paddingLeft, final int paddingTop,
                     final int paddingRight, final int paddingBottom) {
        if (bitmap == null || projection == null) {
            throw new IllegalStateException("Bitmap and Projection cannot be null");
        }

        if (boundsIntersects(projection.getVisibleRegion().latLngBounds)) {
            doDraw(bitmap, projection, paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
    }

    public boolean boundsIntersects(final LatLngBounds test) {
        LatLngBounds bounds = getBounds();
        if (bounds == null || test == null) {
            return false;
        }

        return RichUtils.intersectsRectangle(test.southwest.longitude, test.southwest.latitude,
                test.northeast.longitude, test.northeast.latitude,
                bounds.southwest.longitude, bounds.southwest.latitude,
                bounds.northeast.longitude, bounds.northeast.latitude);
    }
}
