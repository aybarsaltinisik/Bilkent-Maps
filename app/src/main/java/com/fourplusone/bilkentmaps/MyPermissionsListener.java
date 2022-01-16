package com.fourplusone.bilkentmaps;

import android.app.Activity;
import android.content.Context;

import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.Style;

import java.util.List;

public class MyPermissionsListener implements PermissionsListener {

    private MapboxMap mapboxMap;
    private Style style;
    private Context context;

    public MyPermissionsListener(MapboxMap mapboxMap , Style style , Context context){
        super();
        this.mapboxMap = mapboxMap;
        this.style = style;
        this.context = context;
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {

            // Permission sensitive logic called here, such as activating the Maps SDK's LocationComponent to show the device's location
            // LocationComponent adjustments

            LocationComponent locationComponent;

            LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(context)
                    .build();

            // Check if permissions are enabled and if not request
            if (PermissionsManager.areLocationPermissionsGranted(context)) {

                // Get an instance of the component
                locationComponent = mapboxMap.getLocationComponent();

                // Activate with a built LocationComponentActivationOptions object
                locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(context, style).build());

                // Enable to make component visible
                locationComponent.setLocationComponentEnabled(true);

                // Set the component's camera mode
                locationComponent.setCameraMode(CameraMode.TRACKING);

                // Set the component's render mode
                locationComponent.setRenderMode(RenderMode.COMPASS);

                // test
                locationComponent.addOnCameraTrackingChangedListener(new OnCameraTrackingChangedListener() {
                    @Override
                    public void onCameraTrackingDismissed() {
                        // Tracking has been dismissed


                    }

                    @Override
                    public void onCameraTrackingChanged(int currentMode) {
                        // CameraMode has been updated


                    }
                });

            } else {

                PermissionsManager permissionsManager = new PermissionsManager( this );
                permissionsManager.requestLocationPermissions((Activity)context );

            }
        }
    }

}
