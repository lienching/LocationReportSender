package coding.lien.charles.locationreportsender.util;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;


/**
 * Author: lienching
 * Description: This class handle Location stuff and this class use singleton.
 */
public class LocationManager implements LocationListener, Serializable {

    private GoogleApiClient user_client;
    private Location user_location;
    private Activity my_activiity;
    private LocationRequest locationRequest;
    private static LocationManager instance;
    public static boolean success;

    private LocationManager(Activity activity, GoogleApiClient client ) {
        this.user_client = client;
        this.my_activiity = activity;
        try {
            this.user_location = LocationServices.FusedLocationApi.getLastLocation(client);
        } // try
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        } // catch
    } // LocationManager( Activity, GoogleApiClient )


    public static void initLocationManager( Activity activity, GoogleApiClient client ) {
        if ( instance != null ) return;
        instance = new LocationManager(activity,client);
    } // initLocationManager( Activity, GoogleApiClient )


    public static LocationManager getInstance() {
        if ( instance != null )
            return instance;
        return null;
    } // getInstance()


    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(7000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    } // createLocationRequest()

    // Not used yet...
    public void SetInterval( String sec ) {
        locationRequest.setInterval( Long.parseLong(sec) * 1000 );
    } // SetInterval( String )

    public void StartUpdate() {
        if (locationRequest != null) {
            startLocationUpdates();
        } // if
        else {
            Log.e("LocationManager", "locationRequest is null");
        } // else
    } // StartUpdate()

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    user_client, locationRequest, this);
            Log.d("LocationManager", "Start Location Update");
        } // try
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        } // catch
    } // startLocationUpdates

    public void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    user_client, this);
            Log.d("LocationManager", "Stop Location Update");
        } // try
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        } // catch
    } // stopLocationUpdates()

    public boolean CheckLocationStatus(Context context) {
        // Code from "http://stackoverflow.com/a/22980843"
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } // try
            catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            } // catch

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;
        } // if
        else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        } // else

    } // CheckLocationStatus(Context)


    public Location getUserLocation() {
        try {
            this.user_location = LocationServices.FusedLocationApi.getLastLocation(user_client);
        }
        catch( SecurityException e ) {
            Log.e("LocationManager", e.toString());
        }
        return this.user_location;
    } // getUserLocation()


    @Override
    public void onLocationChanged(Location location) {
        Log.i("LocationManager", "Location Changed: lat:" + location.getLatitude() + " , long:" + location.getLongitude());
        this.user_location = location;
    } // onLocationChanged(Location)

} // class LocationManager
