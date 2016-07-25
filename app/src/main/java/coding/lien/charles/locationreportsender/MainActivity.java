package coding.lien.charles.locationreportsender;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;

import coding.lien.charles.locationreportsender.listener.StartTrackListener;
import coding.lien.charles.locationreportsender.listener.StopTrackListener;
import coding.lien.charles.locationreportsender.util.Constant;
import coding.lien.charles.locationreportsender.util.EnvironmentCheck;
import coding.lien.charles.locationreportsender.util.JSONSender;
import coding.lien.charles.locationreportsender.util.LocationManager;
import coding.lien.charles.locationreportsender.util.MessageWrapper;

/**
 * Author: lienching
 * Description: This class is MainActivity
 */

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Serializable {

    private GoogleApiClient mGoogleApiClient;
    private EditText serveraddress_ET;
    private EditText groupid_ET;
    private EditText memberid_ET;
    private EditText status_ET;
    private EditText intreval_ET;
    private Button starttrack_btn;
    private Button stoptrack_btn;
    private LocationManager locationManager;
    private EnvironmentCheck checker;
    private StartTrackListener startTrackListener;
    private StopTrackListener stopTrackListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Setup Goolge API Client
        if ( mGoogleApiClient == null ) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        } // if


        LocationManager.initLocationManager(this, mGoogleApiClient);
        locationManager = LocationManager.getInstance();

        EnvironmentCheck.initEnvironmentCheck(this, mGoogleApiClient, locationManager);
        checker = EnvironmentCheck.getInstance();

        serveraddress_ET = (EditText)findViewById(R.id.serveraddress_editText);
        groupid_ET = (EditText)findViewById(R.id.partyid_editText);
        memberid_ET = (EditText)findViewById(R.id.memberid_editText);
        status_ET = (EditText)findViewById(R.id.devicestatus_editText);
        intreval_ET = (EditText)findViewById(R.id.intervaltime_editText);


        starttrack_btn = (Button)findViewById(R.id.start_btn);
        stoptrack_btn = (Button)findViewById(R.id.stop_btn);

        startTrackListener = new StartTrackListener(this, checker, serveraddress_ET, groupid_ET, memberid_ET,status_ET,intreval_ET,stoptrack_btn);
        starttrack_btn.setOnClickListener(startTrackListener);
        starttrack_btn.setClickable(true);

        stopTrackListener = new StopTrackListener( this, serveraddress_ET, groupid_ET, memberid_ET,status_ET,intreval_ET,starttrack_btn);
        stoptrack_btn.setOnClickListener(stopTrackListener);
        stoptrack_btn.setClickable(false);

        JSONSender.CreateSender(this);


    } // onCreate( Bundle )

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        getLocationPermission();


    } // onStart()

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    } // onStop()


    protected void getLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");

        if ( permissionCheck != PackageManager.PERMISSION_GRANTED ) {
            if ( ActivityCompat.shouldShowRequestPermissionRationale(this,"android.permission.ACCESS_FINE_LOCATION") ) {

            } // if
            else {
                ActivityCompat.requestPermissions(this,
                        new String[] {"android.permission.ACCESS_FINE_LOCATION"}, 1);
            } // else
        } // if
    } // getLocationPermission()

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));

        //final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        switch (requestCode) {
            case Constant.REQUEST_CHECK_SETTINGS: {
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        // All required changes were successfully made
                        MessageWrapper.SendMessage(this, "Location enabled by user!");
                        break;
                    } // case Activity.RESULT_OK
                    case Activity.RESULT_CANCELED: {
                        // The user was asked to change settings, but chose not to
                        MessageWrapper.SendMessage(this, "Location not enabled, user cancelled.");
                        break;
                    } // case Activity.RESULT_CANCELED
                    default: {
                        break;
                    } // default
                } // switch
                break;
            } // case Constant.REQUEST_CHECK_SETTINGS
        } // switch
    } // onActivityResult( int, int, Intent )

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    } // onConnected( Bundle )

    @Override
    public void onConnectionSuspended(int i) {

    } // onConnectionSuspended( int )

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    } // onConnectionFailed( ConnectionResult )


} // class MainActivity
