package coding.lien.charles.locationreportsender;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AutoCompleteTextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.Serializable;

import coding.lien.charles.locationreportsender.listener.StartTrackListener;
import coding.lien.charles.locationreportsender.listener.StopTrackListener;
import coding.lien.charles.locationreportsender.util.BookMarkAdapter;
import coding.lien.charles.locationreportsender.util.CompassManager;
import coding.lien.charles.locationreportsender.util.EnvironmentCheck;
import coding.lien.charles.locationreportsender.util.InformationHolder;
import coding.lien.charles.locationreportsender.util.JSONSender;
import coding.lien.charles.locationreportsender.util.LocationManager;
import coding.lien.charles.locationreportsender.util.MessageWrapper;
import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Author: lienching
 * Description: This class is MainActivity
 */

public class MainActivity extends Activity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Serializable {

    private final String TAG = "MainActivity";

    private GoogleApiClient mGoogleApiClient;
    private AutoCompleteTextView serveraddress_ET;
    private AutoCompleteTextView groupid_ET;
    private AutoCompleteTextView memberid_ET;
    private AutoCompleteTextView status_ET;
    private AutoCompleteTextView intreval_ET;
    private Button starttrack_btn;
    private Button stoptrack_btn;
    private LocationManager locationManager;
    private EnvironmentCheck checker;
    private StartTrackListener startTrackListener;
    private StopTrackListener stopTrackListener;
    private CompassManager compassManager;
    private Realm realm;
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buildGoogleApiClient();

        RealmConfiguration configuration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(configuration);

        realm = Realm.getDefaultInstance();

        LocationManager.initLocationManager(this, mGoogleApiClient);
        locationManager = LocationManager.getInstance();

        EnvironmentCheck.initEnvironmentCheck(this, mGoogleApiClient, locationManager);
        checker = EnvironmentCheck.getInstance();

        serveraddress_ET = (AutoCompleteTextView) findViewById(R.id.serveraddress_editText);
        groupid_ET = (AutoCompleteTextView) findViewById(R.id.partyid_editText);
        memberid_ET = (AutoCompleteTextView) findViewById(R.id.memberid_editText);
        status_ET = (AutoCompleteTextView) findViewById(R.id.devicestatus_editText);
        intreval_ET = (AutoCompleteTextView) findViewById(R.id.intervaltime_editText);


        starttrack_btn = (Button) findViewById(R.id.start_btn);
        stoptrack_btn = (Button) findViewById(R.id.stop_btn);


        startTrackListener = new StartTrackListener(this, realm, checker, serveraddress_ET, groupid_ET, memberid_ET, status_ET, intreval_ET, stoptrack_btn);
        starttrack_btn.setOnClickListener(startTrackListener);
        starttrack_btn.setClickable(true);

        stopTrackListener = new StopTrackListener(this, serveraddress_ET, groupid_ET, memberid_ET, status_ET, intreval_ET, starttrack_btn);
        stoptrack_btn.setOnClickListener(stopTrackListener);
        stoptrack_btn.setClickable(false);

        JSONSender.CreateSender(this);

        CompassManager.initManager(this);
        compassManager = CompassManager.getInstance();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    } // onCreate( Bundle )

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
        client.connect();
        getLocationPermission();

        if ( realm.where(InformationHolder.class).findAll().size() > 0 ) {
            onCreateDialog().show();
        }

        BookMarkAdapter bookMarkAdapter = new BookMarkAdapter(this,realm.where(InformationHolder.class).findAll());

        serveraddress_ET.setAdapter(bookMarkAdapter);
        serveraddress_ET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                serveraddress_ET.setText(realm.where(InformationHolder.class).findAll().get(i).getServerip());
            }
        });


    } // onStart()

    protected void onStop() {
        super.onStop();
        client.disconnect();
    } // onStop()

    protected void onDestroy() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

        super.onDestroy();
        realm.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compassManager.SensorResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassManager.SensorPause();
    }

    protected void getLocationPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION");

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.ACCESS_FINE_LOCATION")) {
                MessageWrapper.SendMessage(this, "Location Permission Granted!");
            } // if
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
            } // else
        } // if
    } // getLocationPermission()

    // Google official example
    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    } // buildGoogleApiClient

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        MessageWrapper.SendMessage(this, "ApiClient connect!");
    } // onConnected( Bundle )

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    } // onConnectionSuspended( int )

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    } // onConnectionFailed( ConnectionResult )


    public Dialog onCreateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        RealmResults<InformationHolder> holders = realm.where(InformationHolder.class).findAll();
        builder.setTitle("IP")
                .setAdapter(new BookMarkAdapter(this, holders), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Adapter", which + " is selected");
                        serveraddress_ET.setText(realm.where(InformationHolder.class).findAll().get(which).getServerip());
                        dialog.dismiss();
                    }
                });

        return builder.create();
    }


    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }
} // class MainActivity
