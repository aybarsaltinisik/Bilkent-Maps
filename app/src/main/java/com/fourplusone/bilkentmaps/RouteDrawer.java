package com.fourplusone.bilkentmaps;

import android.graphics.Color;

import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;
import com.mapbox.mapboxsdk.plugins.annotation.LineManager;
import com.mapbox.mapboxsdk.plugins.annotation.LineOptions;
import com.mapbox.mapboxsdk.utils.ColorUtils;
import java.util.ArrayList;

public class RouteDrawer {
    private LineManager lineManager;

    public RouteDrawer(MapboxMap mapboxMap , MapView mapView , Style style ){
        lineManager = new LineManager( mapView, mapboxMap, style);
    }

    public void draw( LatLng a , LatLng b ){
        ArrayList<LatLng> al = new ArrayList<>();
        al.add( a );
        al.add( b );

        LineOptions lineOptions = new LineOptions()
                .withLatLngs( al )
                .withLineColor(ColorUtils.colorToRgbaString(Color.RED))
                .withLineWidth(5.0f);
        lineManager.create(lineOptions);
    }

    public void clearAll() {
        lineManager.deleteAll();
    }
}
