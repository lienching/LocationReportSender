package coding.lien.charles.locationreportsender.listener;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import coding.lien.charles.locationreportsender.background.TrackingService;
import coding.lien.charles.locationreportsender.util.EnvironmentCheck;
import coding.lien.charles.locationreportsender.util.InformationHolder;
import coding.lien.charles.locationreportsender.util.MessageWrapper;

/**
 * Created by lienching on 6/22/16.
 */
public class StartTrackListener implements View.OnClickListener {

    private Activity myActivity;
    private static Intent serviceIntent;
    private EnvironmentCheck checker;
    private EditText serveraddress_ET;
    private EditText groupid_ET;
    private EditText memberid_ET;
    private EditText status_ET;
    private EditText intreval_ET;
    private Button stop_BTN;

    public StartTrackListener(Activity activity, EnvironmentCheck check, EditText server,
                              EditText group, EditText member, EditText status, EditText intreval, Button stopbtn) {
        this.myActivity = activity;
        this.checker = check;
        this.serveraddress_ET = server;
        this.groupid_ET = group;
        this.memberid_ET = member;
        this.status_ET = status;
        this.intreval_ET = intreval;
        this.stop_BTN = stopbtn;
    } // StartTrackListener

    @Override
    public void onClick(View v) {
        if ( checker.CheckUserEnviroment() ) {
            this.status_ET.setText("ok");
        } // if
        else {
            this.status_ET.setText("error");
            return;
        } // if

        InformationHolder.setAll(this.serveraddress_ET.getText().toString(), this.groupid_ET.getText().toString(),
                                 this.memberid_ET.getText().toString(), this.status_ET.getText().toString(), this.intreval_ET.getText().toString());
        TurnoffEditable();
        serviceIntent = new Intent(myActivity, TrackingService.class);
        myActivity.startService( serviceIntent );
        MessageWrapper.SendMessage(v.getContext(), "Start Tracking!");
        v.setClickable(false);
    } // onClick( View )

    public static Intent getServiceIntent() {
        return serviceIntent;
    } // getServiceIntent()

    private void TurnoffEditable() {
        this.serveraddress_ET.setInputType(0);
        this.groupid_ET.setInputType(0);
        this.memberid_ET.setInputType(0);
        this.intreval_ET.setInputType(0);
        this.stop_BTN.setClickable( true );
    }

} // class StartTrackListener
