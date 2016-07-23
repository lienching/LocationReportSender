package coding.lien.charles.locationreportsender.background;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.os.SystemClock;
import android.util.Log;

import org.json.JSONObject;

import coding.lien.charles.locationreportsender.util.InformationHolder;
import coding.lien.charles.locationreportsender.util.JSONBuilder;
import coding.lien.charles.locationreportsender.util.JSONSender;
import coding.lien.charles.locationreportsender.util.LocationManager;

/**
 *
 * @Author: lienching
 * @Description: This class is a Service and it will handle thing about tracking.
 *
 */
public class TrackingService extends IntentService {

    private LocationManager locationManager;
    private JSONBuilder builder;
    private JSONSender sender;

    public TrackingService() {

        super("Tracking Service");
        builder = new JSONBuilder(InformationHolder.getPartyid(), InformationHolder.getMemberid(), InformationHolder.getDevicesstatus(), "240");
        this.sender = JSONSender.getSender();
    } // Constructor TrackService()

    @Override
    protected void onHandleIntent(Intent intent) {
        this.locationManager = LocationManager.getInstance();
        if ( locationManager != null )
            locationManager.StartUpdate();

        Log.d("TrackingService","Service Start");
        Location now;
        while( InformationHolder.getIsTracking() ) {
            try {
                now = locationManager.getUserLocation();
                if ( now == null ) {
                    Log.d("TrackingSerivce", "No Location");
                    SystemClock.sleep(5*1000);
                    continue;
                } // if

                JSONObject json = builder.BuildGeoJson(now.getLatitude(), now.getLongitude());
                sender.SendJson(json, InformationHolder.getServerip());
                SystemClock.sleep(Long.parseLong(InformationHolder.getIntervaltime()) * 1000);
            } catch (Exception e) {
                Log.e("SendingThread", e.toString());
                Thread.currentThread().interrupt();
            } // catch
        } // while

        Log.d("TrackingService","Service End");

    } // onHandleIntent( Intent )

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationManager.stopLocationUpdates();

    } // onDestroy()
} // class TrackingService
