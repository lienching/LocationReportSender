package coding.lien.charles.locationreportsender.util;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.io.Serializable;


/**
 * Created by lienching on 6/21/16.
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
        }
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        }
    }


    public static void initLocationManager( Activity activity, GoogleApiClient client ) {
        if ( instance != null ) return;
        instance = new LocationManager(activity,client);
    }


    public static LocationManager getInstance() {
        if ( instance != null )
            return instance;
        return null;
    }


    protected void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(7000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void SetInterval( String sec ) {
        locationRequest.setInterval( Long.parseLong(sec) * 1000 );
    }

    public void StartUpdate() {
        if (locationRequest != null) {
            startLocationUpdates();
        }
        else {
            Log.e("LocationManager", "locationRequest is null");
        }
    }

    protected void startLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    user_client, locationRequest, this);
        }
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        }
    } // startLocationUpdates

    public void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    user_client, this);
        }
        catch(SecurityException e) {
            Log.e("LocationManager", e.toString());
        }
    }

    public boolean CheckLocationStatus(Context context) {
        // Code from "http://stackoverflow.com/a/22980843"
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }

    } // CheckLocationStatus()


    public Location getUserLocation() {
        return this.user_location;
    } // getUserLocation()


    @Override
    public void onLocationChanged(Location location) {
        Log.i("LocationManager", "Location Changed: lat:" + location.getLatitude() + " , long:" + location.getLongitude());
        this.user_location = location;
    }
}
