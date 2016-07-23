package coding.lien.charles.locationreportsender.background;

import android.location.Location;
import android.util.Log;

import org.json.JSONObject;

import coding.lien.charles.locationreportsender.listener.StopTrackListener;
import coding.lien.charles.locationreportsender.util.InformationHolder;
import coding.lien.charles.locationreportsender.util.JSONBuilder;
import coding.lien.charles.locationreportsender.util.JSONSender;
import coding.lien.charles.locationreportsender.util.LocationManager;

/**
 * Created by lienching on 6/22/16.
 */

// Deprecated!!!
public class SendingThread extends Thread {

    private JSONBuilder builder;
    private JSONSender sender;

    public SendingThread() {
        builder = new JSONBuilder(InformationHolder.getPartyid(), InformationHolder.getMemberid(), InformationHolder.getDevicesstatus(), "240");
        this.sender = JSONSender.getSender();
    } // constructor SendingThread

    @Override
    public synchronized void run() {
        StopTrackListener.setThread(this);
        while ( !this.isInterrupted() ) {
            try {
                Location now = LocationManager.getInstance().getUserLocation();
                if ( now == null ) {
                    Thread.sleep(10000);
                    continue;
                }
                JSONObject json = builder.BuildGeoJson(now.getLatitude(), now.getLongitude());
                sender.SendJson(json, InformationHolder.getServerip());
                Thread.sleep(Long.parseLong(InformationHolder.getIntervaltime()) * 1000);
            } catch (Exception e) {
                Log.e("SendingThread", e.toString());
                Thread.currentThread().interrupt();
            } // catch
        }
        LocationManager.getInstance().stopLocationUpdates();
    }
} // class Thread
