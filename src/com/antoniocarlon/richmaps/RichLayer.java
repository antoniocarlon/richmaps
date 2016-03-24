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
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Represents a layer on the map that will be drawn using rich symbology.
 * It's drawn as a GroundOverlay.
 */
public class RichLayer {
    private static final float MINIMUM_ZOOM_LEVEL = 5f;
    private View view;
    private GoogleMap map;
    private float zIndex = 0;
    private GroundOverlay overlay;
    private SortedMap<Integer, List<RichShape>> shapes = new TreeMap<>();
    private Bitmap bitmap;

    private RichLayer(View view, GoogleMap map, float zIndex) {
        if (view == null || map == null) {
            throw new IllegalArgumentException("View and GoogleMap cannot be null");
        }

        this.view = view;
        this.map = map;
        this.zIndex = zIndex;

        map.getUiSettings().setTiltGesturesEnabled(false); // For now, tilt gestures are not allowed when using RichLayer
    }

    public void refresh() {
        CameraPosition cameraPosition = map.getCameraPosition();
        if (cameraPosition.zoom >= MINIMUM_ZOOM_LEVEL) {
            Projection projection = map.getProjection();

            prepareBitmap();
            draw(bitmap, projection);

            float mapWidth = (float) SphericalUtil.computeDistanceBetween(
                    projection.getVisibleRegion().nearLeft,
                    projection.getVisibleRegion().nearRight);

            if (overlay == null) {
                GroundOverlayOptions background = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .position(cameraPosition.target, mapWidth)
                        .bearing(cameraPosition.bearing)
                        .zIndex(zIndex);
                overlay = map.addGroundOverlay(background);
            } else {
                overlay.setImage(BitmapDescriptorFactory.fromBitmap(bitmap));
                overlay.setPosition(cameraPosition.target);
                overlay.setDimensions(mapWidth);
                overlay.setBearing(cameraPosition.bearing);
            }
        } else {
            if (overlay != null) {
                overlay.remove();
                overlay = null;
            }
        }
    }

    public void addShape(RichShape shape) {
        if (shape != null) {
            if (!shapes.containsKey(shape.getZIndex())) {
                shapes.put(shape.getZIndex(), new ArrayList<RichShape>());
            }

            List<RichShape> shapesZIndez = shapes.get(shape.getZIndex());
            shapesZIndez.add(shape);
        }
    }

    public void removeShape(RichShape shape) {
        if (shape != null) {
            Set<Integer> zIndices = shapes.keySet();
            for (Integer zIndex : zIndices) {
                List<RichShape> shapesZIndex = shapes.get(zIndex);
                shapesZIndex.remove(shape);
            }
        }
    }

    private void prepareBitmap() {
        if (bitmap == null || bitmap.getWidth() != view.getWidth() || bitmap.getHeight() != view.getHeight()) {
            bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            bitmap.eraseColor(Color.TRANSPARENT);
        }
    }

    private Bitmap draw(Bitmap bitmap, Projection projection) {
        Set<Integer> zIndices = shapes.keySet();
        for (Integer zIndex : zIndices) {
            draw(bitmap, projection, shapes.get(zIndex));
        }

        return bitmap;
    }

    private Bitmap draw(Bitmap bitmap, Projection projection, List<RichShape> shapes) {
        for (RichShape shape : shapes) {
            shape.draw(bitmap, projection);
        }
        return bitmap;
    }

    public static class Builder {
        private View view;
        private GoogleMap map;
        private float zIndex;

        public Builder(View view, GoogleMap map) {
            this.view = view;
            this.map = map;
        }

        public Builder zIndex(float zIndex) {
            this.zIndex = zIndex;
            return this;
        }

        public RichLayer build() {
            return new RichLayer(view, map, zIndex);
        }
    }
}