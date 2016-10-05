package coding.lien.charles.locationreportsender.util;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.api.GoogleApiClient;

import java.io.Serializable;

import coding.lien.charles.locationreportsender.R;

/**
 * Author: lienching
 * Description: This class is use to check do user had all requirement for start tracking service.
 */
public class EnvironmentCheck implements Serializable {

    private Context user_context;
    private GoogleApiClient user_client;
    private LocationManager locationManager;
    private static EnvironmentCheck instance;


    private EnvironmentCheck( Context context, GoogleApiClient client, LocationManager lm  ) {
        this.user_context = context;
        this.user_client = client;
        this.locationManager = LocationManager.getInstance();
    } // Constructor EnviromentCheck( Context )

    public static void initEnvironmentCheck(Context context, GoogleApiClient client, LocationManager lm) {
        if ( instance != null) return;
        instance = new EnvironmentCheck(context,client,lm);
    } // initEnvironmentCheck( Context, GoogleApiClient, LocationManager )

    public static EnvironmentCheck getInstance() {
        if ( instance != null ) return instance;
        return null;
    } // getInstance()

    public boolean CheckUserEnviroment() {
        if ( !CheckInternetStatus() ) {
            MessageWrapper.SendMessage(user_context, user_context.getResources().getString(R.string.NetworkError));
            return false;
        } // if
        if ( !locationManager.CheckLocationStatus(user_context) ) {
            MessageWrapper.SendMessage(user_context, user_context.getResources().getString(R.string.LocationError));
            return false;
        } // if
        locationManager.createLocationRequest();
        return true;
    } // CheckUserEnviroment()

    private boolean CheckInternetStatus() {
        ConnectivityManager conMgr = (ConnectivityManager)user_context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo user_status = conMgr.getActiveNetworkInfo();

        return user_status != null && user_status.isConnected();
    } // CheckInternetStatus()


} // class EnvironmentCheck
