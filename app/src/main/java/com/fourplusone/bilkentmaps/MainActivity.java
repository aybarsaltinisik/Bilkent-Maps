package com.fourplusone.bilkentmaps;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.LocationComponentOptions;
import com.mapbox.mapboxsdk.location.OnCameraTrackingChangedListener;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private MapView mapView;
    private RouteDrawer drawer;
    private MaterialSearchView searchView;
    private ListView firstView;
    private LocationComponent locationComponent;

    String[] firstSrc = {
            "B building",
            "FF building",
            "FC building",
            "MA building",
            "A building",
            "SA building",
            "SB building"
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create all components for Map
        createComponents();

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull MapboxMap mapboxMap) {
                mapboxMap.setStyle(Style.MAPBOX_STREETS, new Style.OnStyleLoaded() {
                    @Override
                    public void onStyleLoaded(@NonNull Style style) {
                        // creation of Location components
                        createLocationComponents( mapboxMap , style );

                        // creating a drawer object
                        drawer = new RouteDrawer( mapboxMap , mapView , style );

                        // Create route, route marker, start navi button and details
                        // via long click on map.
                        mapboxMap.addOnMapLongClickListener(new MapboxMap.OnMapLongClickListener() {
                            @Override
                            public boolean onMapLongClick(@NonNull LatLng point) {
                                clearDestination();
                                setDestination( mapboxMap , style , point);
                                return true;
                            }
                        });


                    }
                });
            }

        });
    }
    // a method that creates the components we will use in view
    private void createComponents(){
        Mapbox.getInstance(this, "pk.eyJ1IjoiZGVuZW1lMSIsImEiOiJjanZheDIya3gxMzdhNGVtajQ4OXZzNnJhIn0.eVnUggGd3FWxuv_emUB5Vw");
        setContentView(R.layout.activity_main);

        //set start_navi layout invisible
        findViewById(R.id.navi_control_panel).setVisibility(View.INVISIBLE);
        findViewById(R.id.navi_run_panel).setVisibility(View.INVISIBLE);

        findViewById(R.id.cancel_navi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearDestination();
            }
        });

        //searchbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Search for a destination");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));

        //menu
        firstView = (ListView)findViewById(R.id.first_view);
        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,
                firstSrc);
        firstView.setAdapter(adapter);
        firstView.setVisibility(View.INVISIBLE);

        // adding menu to the searchview with a listener
        searchView = (MaterialSearchView)findViewById(R.id.search_view);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {

                firstView.setVisibility(View.VISIBLE);

            }

            @Override
            public void onSearchViewClosed() {
                //if closed searchview 1stview will return default
                firstView.setVisibility(View.INVISIBLE);
            }
        });

        // Search box listener to control menu
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText != null && !newText.isEmpty()){
                    List<String> firstFound = new ArrayList<String>();
                    for(String item: firstSrc){
                        if(item.contains(newText))
                            firstFound.add(item);
                    }

                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1, firstFound);
                    firstView.setAdapter(adapter);
                }
                else{
                    ArrayAdapter adapter = new ArrayAdapter(MainActivity.this,
                            android.R.layout.simple_list_item_1, firstSrc);
                    firstView.setAdapter(adapter);
                }
                return true;
            }
        });
        // end of the searchbar and menu.
        Log.i("test","test");


        // map view and map being added to the layout
        mapView = findViewById(R.id.mapView);
    }

    // Location gatherer with the permission checker listener
    // @param MapboxMap - the map , Style - style of map on Creation
    private void createLocationComponents( MapboxMap mapboxMap, Style style ){

        LocationComponentOptions locationComponentOptions = LocationComponentOptions.builder(MainActivity.this)
                .build();


        // Check if permissions are enabled and if not request
        if (PermissionsManager.areLocationPermissionsGranted(MainActivity.this)) {

            // Get an instance of the component
            locationComponent = mapboxMap.getLocationComponent();

            // Activate with a built LocationComponentActivationOptions object
            locationComponent.activateLocationComponent(LocationComponentActivationOptions.builder(MainActivity.this, style).build());

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
            FloatingActionButton fab = findViewById(R.id.center_button);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    locationComponent.setCameraMode(CameraMode.TRACKING);
                }
            });

        } else {

            //PermissionsManager permissionsManager = new PermissionsManager( new MyPermissionsListener( mapboxMap , style, MainActivity.this) );
            //permissionsManager.requestLocationPermissions(MainActivity.this );

        }
    }

    // set the destination, creation of contents in the start_navi layout
    private void setDestination( MapboxMap mapboxMap , Style style , LatLng point ){
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastKnown = locationComponent.getLastKnownLocation();

        // Map adjustments : drawing a simple route
        drawer.draw( new LatLng( lastKnown.getLatitude() , lastKnown.getLongitude() ),
                point );
        findViewById(R.id.navi_control_panel).setVisibility(View.VISIBLE);
        findViewById(R.id.toolbar).setVisibility(View.INVISIBLE);
        findViewById(R.id.search_view).setVisibility(View.INVISIBLE);
    }

    // clear the previous destination
    private void clearDestination(){
        drawer.clearAll();
        findViewById(R.id.navi_control_panel).setVisibility(View.INVISIBLE);
        findViewById(R.id.toolbar).setVisibility(View.VISIBLE);
        findViewById(R.id.search_view).setVisibility(View.VISIBLE);
    }

    // lifecycle methods of android
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
    // end of the lifecycle methods
}