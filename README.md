# richmaps

Advanced rendering options for polygons and polylines on Google Maps Android API v2

### Current version
v0.1.0

### Objectives
 - Improve the GoogleMaps Android API v2 rendering options for Polygons and Polylines

### Easy peasy
```
// ...

// Create a new RichLayer tied to a map
RichLayer richLayer = new RichLayer.Builder(mMap).zIndex(0).build(); // zIndex represents the position of the RichLayer on the map

// ...

@Override
public void onCameraChange(CameraPosition cameraPosition) {
    // Refresh the RichLayer each time the camera changes
    richLayer.refresh();
}
```

### Tutorial
```
RichPolylineOptions polylineOpts = new RichPolylineOptions(null)
        .zIndex(3) // zIndex represents the position of the polyline on the RichLayer
        .strokeWidth(15)
        .strokeColor(Color.YELLOW) // Set the polyline base color
        .linearGradient(true)
        .add(new RichPoint(new LatLng(40.22987, -3.95931)).color(Color.RED)) // Set color for some vertices
        .add(new RichPoint(new LatLng(40.23109, -3.95926)))
        .add(new RichPoint(new LatLng(40.23063, -3.95837)).color(Color.RED))
        .add(new RichPoint(new LatLng(40.23169, -3.95809)))
        .add(new RichPoint(new LatLng(40.23093, -3.95705)))
        .add(new RichPoint(new LatLng(40.23023, -3.95626)));
RichPolyline polyline = polylineOpts.build();
polyline.add(new RichPoint(new LatLng(40.23163, -3.95602)).color(Color.CYAN)); // RichPoint added after the creation of the RichPolyline
richLayer.addShape(polyline);

RichPolygonOptions polygonOpts = new RichPolygonOptions(null)
        .zIndex(1) // zIndex represents the position of the polyline on the RichLayer
        .strokeWidth(15)
        .strokeColor(Color.YELLOW)
        .linearGradient(true)
        .fillShader(new BitmapShader(
                BitmapFactory.decodeResource(getResources(), R.mipmap.dot),
                Shader.TileMode.REPEAT, Shader.TileMode.REPEAT))
        .pathEffect(new DashPathEffect(new float[] {10,20}, 0))
        .add(new RichPoint(new LatLng(40.22861, -3.95567)))
        .add(new RichPoint(new LatLng(40.22977, -3.95338)).color(Color.RED))
        .add(new RichPoint(new LatLng(40.22736, -3.95132)))
        .add(new RichPoint(new LatLng(40.22644, -3.95533)).color(Color.CYAN))
        .add(new RichPoint(new LatLng(40.22884, -3.95342)));
List<RichPoint> hole = new ArrayList<>();
hole.add(new RichPoint(new LatLng(40.22813, -3.95289)));
hole.add(new RichPoint(new LatLng(40.22751, -3.95244)).color(Color.RED));
hole.add(new RichPoint(new LatLng(40.22707, -3.95415)).color(Color.CYAN));
polygonOpts.addHole(hole);
richLayer.addShape(polygonOpts.build());

RichPolylineOptions polylineOpts2 = new RichPolylineOptions(null)
        .zIndex(3) // zIndex represents the position of the polyline on the RichLayer
        .strokeWidth(15)
        .strokeColor(Color.CYAN)
        .linearGradient(true)
        .add(new RichPoint(new LatLng(40.23087, -3.96031)))
        .add(new RichPoint(new LatLng(40.23209, -3.96026)))
        .add(new RichPoint(new LatLng(40.23163, -3.95937)))
        .add(new RichPoint(new LatLng(40.23269, -3.95909)))
        .add(new RichPoint(new LatLng(40.23193, -3.95805)))
        .add(new RichPoint(new LatLng(40.23123, -3.95726)));
RichPolyline polyline2 = polylineOpts2.build();
richLayer.addShape(polyline2);
richLayer.removeShape(polyline2); // This RichPolyline will not be drawn as it is added and removed

mMap.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(new LatLng(40.22905, -3.95389), 18, 0, 0)));
```

### Example
[[https://github.com/antoniocarlon/richmaps/blob/master/example.jpg]]

### Limitations
- For now, rotate and tilt gestures must be disabled to avoid undesired behaviour (this will change in future implementations).
- Entities are not clickable
- Drawing is not geodesic

### Future work
Improve drawing to allow rotate and tilt gestures.

### License
Copyright 2015 ANTONIO CARLON

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.