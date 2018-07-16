package com.example.mariyamasud.maps;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caughtglobalexceptionlibrary.CosmosException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import transferObject.TransferObject;

//import com.jcmore2.appcrash.AppCrash;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;

    private static final String LOG_TAG = MapsActivity.class.getName();
    public TransferObject transferObject = new TransferObject();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transferObject.setCrashText("D'oh! Its Crash.."); //your error message "oops its crash" or something.
        transferObject.setDestinationActivity(StartActivity.class); //MUST BE UR STARTING ACTIVITY
        transferObject.setDetailsButonText("Details"); //showing stacktrace. change your button's text what you want
        transferObject.setRestartAppButtonText("Contiune"); //restart your app. change your button's text what you want
        transferObject.setImagePath(R.drawable.homer);
        transferObject.setBackgorundHex("#ffffff"); //ur crash activity's backgorund color.change what you want.
        transferObject.setCrashTextColor("#000000"); //CrashText's color. MUST BE HEX CODE
        Thread.setDefaultUncaughtExceptionHandler(new CosmosException(this,transferObject)); //this our girl





        if (googleServicesAvailable()) {
            Toast.makeText(this, "Perfect!!!", Toast.LENGTH_LONG).show();
            setContentView(R.layout.activity_maps);
            initMap();
        } else {
            // No Google Maps Layout
        }
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        mapFragment.getMapAsync(this);
    }

    public boolean googleServicesAvailable() {
        /**
         * Checks the availability of the specified APIs
         * If at least one of the APIs is unavailable, the task will fail with a {@Exception #AvailabilityException},
         * which can be queried for individual API availability.
         */
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        /**
         * @errorCode	error code returned by {@link #isGooglePlayServicesAvailable(Context)} call.
         *              If errorCode is SUCCESS then null is returned.
         */
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    /**
     * Called when the map is ready to be used.
     *
     * @Note that this does not guarantee that the map has undergone layout.
     *       Therefore, the map's size may not have been determined by the time the callback method is called.
     *
     * @param googleMap A non-null instance of a GoogleMap associated with the MapFragment or MapView that defines the callback.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        if(mGoogleMap != null){
            /**
             * Called when the user makes a long-press gesture on the map,
             * but only if none of the overlays of the map handled the gesture.
             *
             * @param latlng The point on the ground (projected from the screen point) that was pressed.
             */
            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MapsActivity.this.setMarker("Local", latLng.latitude, latLng.longitude);
                }
            });
            /**
             *Called when a marker has finished being dragged. The marker's location can be accessed via {@link #getPosition()}.
             *
             * @param marker = new GoogleMap.OnMarkerDragListener(),
             *               {@link #onMarkerDragstart (Marker marker)}
             *               {@link #onMarkerDrag (Marker marker)}
             *               {@link #onMarkerDragEnd (Marker marker)}
             */
            mGoogleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                /**
                 * Called when a marker starts being dragged. The marker's location can be accessed via getPosition();
                 * this position may be different to the position prior to the start of the drag because the marker is
                 * popped up above the touch point.
                 *
                 * @param marker The marker being dragged.
                 */
                @Override
                public void onMarkerDragStart(Marker marker) {}

                @Override
                public void onMarkerDrag(Marker marker) {}

                /**
                 * Called when a marker has finished being dragged. The marker's location can be accessed via getPosition().
                 *
                 * @param marker The marker that was dragged.
                 */
                @Override
                public void onMarkerDragEnd(Marker marker) {
                    /**
                     * Constructs a Geocoder whose responses will be localized for the default system Locale.
                     *
                     * @param context	Context: the Context of the calling Activity
                     */
                    Geocoder gc = new Geocoder(MapsActivity.this);
                    LatLng ll = marker.getPosition();
                    double lat = ll.latitude;
                    double lng = ll.longitude;
                    List<Address> list = null;
                    try {
                        /**
                         * Returns an array of Addresses that are known to describe the area immediately surrounding the given
                         * latitude and longitude. The returned addresses will be localized for the locale provided to this class's
                         * constructor.
                         *
                         * @param lat double: the latitude a point for the search
                         * @param lng double: the longitude a point for the search
                         * @param maxResults int: max number of addresses to return. Smaller numbers (1 to 5) are recommended
                         *
                         * @return List<Address> list: a list of Address objects. Returns null or empty list if
                         *                             no matches were found or there is no backend service available.
                         */
                        list = gc.getFromLocation(lat, lng, 1);
                    } catch (IOException e) {
                        //e.printStackTrace();
                        Log.e(LOG_TAG, "Problem building the URL ", e);
                    }
                    Address add = list.get(0);
                    marker.setTitle(add.getLocality());
                    marker.showInfoWindow();
                }
            });
            /**
             * public static interface GoogleMap.InfoWindowAdapter. Provides views for customized rendering of info windows.
             */
            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter(){
                /**
                 * Provides a custom info window for a marker.
                 * @param marker The marker for which an info window is being populated.
                 * @return A custom info window for marker, or null to use the default info window frame with custom contents.
                 */
                @Override
                public View getInfoWindow(Marker marker) {
                    return null;
                }

                /**
                 * Provides custom contents for the default info window frame of a marker.
                 * @param marker The marker for which an info window is being populated.
                 * @return A custom view (v) to display as contents in the info window for marker,
                 *           or null to use the default content rendering instead.
                 */
                @Override
                public View getInfoContents(Marker marker) {
                    View v = getLayoutInflater().inflate(R.layout.info_window, null);

                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
                    TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);
                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
                    TextView tvSnippet = (TextView) v.findViewById(R.id.tv_snippet);

                    LatLng ll = marker.getPosition();
                    tvLocality.setText(marker.getTitle());
                    tvLat.setText("Latitude: " + ll.latitude);
                    tvLng.setText("Longitude: " + ll.longitude);
                    tvSnippet.setText(marker.getSnippet());

                    return v;
                }
            });
        }
        goToLocationZoom(39.008224, -76.8984527, 15);
    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        /**
         * To obtain a CameraUpdate use the factory class CameraUpdateFactory.
         * @see CameraUpdate : Defines a camera move.
         * @see CameraUpdateFactory : A class containing methods for creating CameraUpdate objects that change a map's camera.
         * @see newLatLng( LatLng latLng): Returns a CameraUpdate that moves the center of the screen to a latitude and longitude
         *                                specified by a LatLng object.
         */
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        /**
         * To obtain a CameraUpdate use the factory class CameraUpdateFactory.
         * @see CameraUpdate : Defines a camera move.
         * @see CameraUpdateFactory : A class containing methods for creating CameraUpdate objects that change a map's camera.
         * @see newLatLngZoom( LatLng latLng, float zoom): Returns a CameraUpdate that moves the center of the screen to a latitude
         *                                          and longitude specified by a LatLng object, and moves to the given zoom level.
         */
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }

    Marker marker;

    /**
     *
     * @param view
     * @throws IllegalArgumentException	if locationName is null
     * @throws IOException	if the network is unavailable or any other I/O problem occurs
     */
    public void geoLocate(View view) throws IOException {

        EditText et = (EditText) findViewById(R.id.editText);
        et.setText(getIntent().getStringExtra("EdiTtEXTvALUE"));
        String location = et.getText().toString();
        /**
         * Constructs a Geocoder whose responses will be localized for the default system Locale.
         *
         * @param context	Context: the Context of the calling Activity
         */
        Geocoder gc = new Geocoder(this);
        /**
         * Returns an array of Addresses that are known to describe the area immediately surrounding the given
         * latitude and longitude. The returned addresses will be localized for the locale provided to this class's
         * constructor.
         *
         * @param location String: a user-supplied description of a location
         * @param maxResults int: max number of addresses to return. Smaller numbers (1 to 5) are recommended
         *
         * @return List<Address>: a list of Address objects. Returns null or empty list if no matches were found
         *                        or there is no backend service available.
         */
        try {
        List<Address> list = gc.getFromLocationName(location, 1);
        Address address = list.get(0);
        String locality = address.getLocality();

        Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

        double lat = address.getLatitude();
        double lng = address.getLongitude();
        goToLocationZoom(lat, lng, 15);

        setMarker(locality, lat, lng);
        } catch (IndexOutOfBoundsException e) {
           // e.printStackTrace();
            Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    ArrayList<Marker> markers = new ArrayList<Marker>();
    static final int POLYGON_POINTS = 5;
    Polygon shape;

    /**
     * Set Marker for a location.
     * @param locality
     * @param lat
     * @param lng
     */
    private void setMarker(String locality, double lat, double lng) {
        if(markers.size() == POLYGON_POINTS){
            removeEverything();
        }
        /**
         * @see MarkerOptions : Defines MarkerOptions for a marker.
         * @see MarkerOptions (): Creates a new set of marker options.
         * @see title(String title): Sets the title for the marker.
         * @see icon(BitmapDescriptor iconDescriptor): Sets the icon for the marker.
         * @see position( LatLng latlng): Sets the location for the marker.
         * @see snippet(String snippet): Sets the snippet for the marker.
         */
        MarkerOptions options = new MarkerOptions()
                .title(locality)
                .draggable(true)
                .icon( BitmapDescriptorFactory.fromResource(R.mipmap.ic_launcher))
                .position(new LatLng(lat, lng))
                .snippet("I am Here");

        markers.add(mGoogleMap.addMarker(options));

        if(markers.size() == POLYGON_POINTS){
            drawPolygon();
        }
    }

    /**
     * @see PolygonOptions : Defines options for a polygon.
     * @see PolygonOptions (): Creates polygon options.
     * see fillColor(int color): Specifies the polygon's fill color, as 32-bit ARGB.
     * see strokeWidth(float width): Specifies the polygon's stroke width, in display pixels.
     * see strokeColor(int color): Specifies the polygon's stroke color, as 32-bit ARGB.
     */
    private void drawPolygon() {
        PolygonOptions options = new PolygonOptions()
                .fillColor(0x330000FF)
                .strokeWidth(3)
                .strokeColor(Color.RED);

        for(int i=0; i<POLYGON_POINTS;i++){
            options.add(markers.get(i).getPosition());
        }
        shape = mGoogleMap.addPolygon(options);

    }

    /**
     * This method remove markers list and polygon shape
     */
    private void removeEverything() {
        for(Marker marker : markers) {
            marker.remove();
        }
        markers.clear();
        shape.remove();
        shape = null;

    }
    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     *
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     *
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     *
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     *
     * @return You must return true for the menu to be displayed;
     *         if you return false it will not be shown.
     *
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.mainmenubar, menu);
        return true;
    }
    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     *
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     *
     * @return boolean Return super.onOptionsItemSelected(item);
     *
     * @see #onCreateOptionsMenu
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mapTypeNone:
                mGoogleMap.setMapType( GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mGoogleMap.setMapType( GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mGoogleMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mGoogleMap.setMapType( GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mGoogleMap.setMapType( GoogleMap.MAP_TYPE_HYBRID);
                break;

            default:
                break;
        }
        //int id = item.getItemId();
        //if (id == R.id.action_search) {
            mGoogleMap.setMapType( GoogleMap.MAP_TYPE_SATELLITE);
            //startActivity(new Intent(this, MapsActivity.class));
        //}

        return super.onOptionsItemSelected(item);
    }

    LocationRequest mLocationRequest;

    /**
     * A Method from abstract class GoogleApiClient, and from interface ConnectionCallbacks
     * @param bundle
     * @see LocationRequest objects are used to request a quality of service for location updates from the FusedLocationProviderApi.
     * static LocationRequest
     * see create(): Create a location request with default parameters.
     * see setPriority(int priority): Set the priority of the request.
     * see setInterval(long millis): Set the desired interval for active location updates, in milliseconds.
     */
    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    public void requestPermissions(@NonNull String[] permissions, int requestCode)
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }
        /**
         * @see LocationServices: The activity_list_check entry point for location services integration.
         * @see FusedLocationApi: This field was deprecated. Use connectionless API (FusedLocationProviderClient) instead.
         */
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);
    }

    /**
     * Invoked when the client is disconnected from the media browser.
     * @param i
     */
    @Override
    public void onConnectionSuspended(int i) {}

    /**
     * Called when there was an error connecting the client to the service.
     * @param connectionResult
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {}

    /**
     * Called when a new user location is known.
     * @param location new location. Must not be null.
     */
    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_LONG).show();
        } else {
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);
        }
    }
}