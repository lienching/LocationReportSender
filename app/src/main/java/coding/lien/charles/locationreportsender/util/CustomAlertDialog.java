package coding.lien.charles.locationreportsender.util;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import coding.lien.charles.locationreportsender.R;


/**
 * Created by lienching on 10/5/16.
 */

public class CustomAlertDialog extends Dialog implements android.view.View.OnClickListener  {

    private Button importBtn, exportBtn;
    private IoEBookMarkHelper helper;

    protected CustomAlertDialog(IoEBookMarkHelper helper, Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.helper = helper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.buttons);
        importBtn = (Button) findViewById(R.id.importBtn);
        exportBtn = (Button) findViewById(R.id.exportBtn);
        importBtn.setOnClickListener(this);
        exportBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if ( view.equals(importBtn) ) {
            helper.importData();
            this.dismiss();
        }
        else {
            helper.exportData();
            this.dismiss();
        }
    }


}
