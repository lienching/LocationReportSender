package coding.lien.charles.locationreportsender;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

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
import coding.lien.charles.locationreportsender.util.BookMarkHolder;
import coding.lien.charles.locationreportsender.util.CompassManager;
import coding.lien.charles.locationreportsender.util.EnvironmentCheck;
import coding.lien.charles.locationreportsender.util.IoEBookMarkHelper;
import coding.lien.charles.locationreportsender.util.JSONSender;
import coding.lien.charles.locationreportsender.util.LocationManager;
import coding.lien.charles.locationreportsender.util.MessageWrapper;
import io.realm.Realm;
import io.realm.RealmConfiguration;

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
    private Button savebm_btn;// Save BookMark
    private Button iebm_btn; // Import / Export Bookmark Button
    private LocationManager locationManager;
    private EnvironmentCheck checker;
    private StartTrackListener startTrackListener;
    private StopTrackListener stopTrackListener;
    private CompassManager compassManager;
    private Realm realm;
    private GoogleApiClient client;
    private IoEBookMarkHelper helper;


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
        savebm_btn = (Button) findViewById(R.id.savebk_btn);
        iebm_btn = (Button) findViewById(R.id.iebk_btn);

        startTrackListener = new StartTrackListener(this, realm, checker, serveraddress_ET, groupid_ET, memberid_ET, status_ET, intreval_ET, stoptrack_btn);
        starttrack_btn.setOnClickListener(startTrackListener);
        starttrack_btn.setClickable(true);

        stopTrackListener = new StopTrackListener(this, serveraddress_ET, groupid_ET, memberid_ET, status_ET, intreval_ET, starttrack_btn);
        stoptrack_btn.setOnClickListener(stopTrackListener);
        stoptrack_btn.setClickable(false);

        savebm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String server, party, member;
                server = serveraddress_ET.getText().toString();
                party = groupid_ET.getText().toString();
                member = memberid_ET.getText().toString();

                BookMarkHolder holder = new BookMarkHolder(server, party, member);
                realm.beginTransaction();
                realm.insert(holder);
                realm.commitTransaction();
            }
        });

        helper = new IoEBookMarkHelper(realm, this);

        iebm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getIOPermission();
                helper.showDialog();
            }
        });

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

        // IP
        BookMarkAdapter serverAdapter = new BookMarkAdapter(this,realm.where(BookMarkHolder.class).findAll(),"IP");

        serveraddress_ET.setAdapter(serverAdapter);
        serveraddress_ET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                serveraddress_ET.setText(realm.where(BookMarkHolder.class).findAll().get(i).serverip);
            }
        });

        // GROUP
        BookMarkAdapter groupAdapter = new BookMarkAdapter(this,realm.where(BookMarkHolder.class).findAll(),"GROUP");

        groupid_ET.setAdapter(groupAdapter);
        groupid_ET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                groupid_ET.setText(realm.where(BookMarkHolder.class).findAll().get(i).partyid);
            }
        });

        // USER
        BookMarkAdapter userAdapter = new BookMarkAdapter(this,realm.where(BookMarkHolder.class).findAll(),"USER");

        memberid_ET.setAdapter(userAdapter);
        memberid_ET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                memberid_ET.setText(realm.where(BookMarkHolder.class).findAll().get(i).memberid);
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

    protected void getIOPermission() {
        int readpermissionCheck = ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
        int writepermissionCheck = ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");

        if (readpermissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                MessageWrapper.SendMessage(this, "Location Permission Granted!");
            } // if
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
            } // else
        } // if

        if (writepermissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
                MessageWrapper.SendMessage(this, "Location Permission Granted!");
            } // if
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 1);
            } // else
        } // if

    } // getIOPermission()

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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == 1 ) {
            if ( resultCode == RESULT_OK ) {
                IoEBookMarkHelper.FILE_NAME = data.getDataString();
            }
        }
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
