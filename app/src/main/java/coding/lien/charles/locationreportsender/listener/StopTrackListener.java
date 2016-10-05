package coding.lien.charles.locationreportsender.listener;

import android.app.Activity;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import coding.lien.charles.locationreportsender.util.InformationHolder;
import coding.lien.charles.locationreportsender.util.MessageWrapper;

/**
 * Author: lienching
 * Description: This class had the right to stop tracking service.
 */
public class StopTrackListener implements View.OnClickListener {

    private Intent serviceIntent;
    private Activity myActivity;
    private EditText serveraddress_ET;
    private EditText groupid_ET;
    private EditText memberid_ET;
    private EditText status_ET;
    private EditText intreval_ET;
    private Button start_BTN;

    public StopTrackListener( Activity activity, EditText server,
                             EditText group, EditText member, EditText status, EditText intreval, Button startbtn) {
        this.myActivity = activity;
        this.serveraddress_ET = server;
        this.groupid_ET = group;
        this.memberid_ET = member;
        this.status_ET = status;
        this.intreval_ET = intreval;
        this.start_BTN = startbtn;
    } // Constructor StopTrackListener( Intent, Activity )

    @Override
    public void onClick(View v) {
        this.serviceIntent = StartTrackListener.getServiceIntent();
        Log.i("StopTrackListener", "Service Stopping");
        TurnonEditable();
        if ( serviceIntent == null ) return;
        InformationHolder.isTracking = false;
        this.myActivity.stopService(serviceIntent);
        Log.i("StopTrackListener", "Service Stopped!");
        v.setClickable(false);
        MessageWrapper.SendMessage(v.getContext(), "Tracking Stopped!");
    } // onClick( View )

    public void TurnonEditable() {
        this.serveraddress_ET.setInputType(InputType.TYPE_TEXT_VARIATION_URI);
        this.groupid_ET.setInputType(InputType.TYPE_CLASS_TEXT);
        this.memberid_ET.setInputType(InputType.TYPE_CLASS_TEXT);
        this.intreval_ET.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        start_BTN.setClickable(true);
    } // TurnonEditable

} // class StopTrackListener
