package coding.lien.charles.locationreportsender.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.test.ActivityUnitTestCase;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import coding.lien.charles.locationreportsender.R;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by lienching on 9/28/16.
 */

public class IoEBookMarkHelper {

    private Realm realm;
    private Activity activity;
    private int REQUEST_CODE=1;
    private String OUTPUT_FILE;
    public static String FILE_NAME = "";

    public IoEBookMarkHelper(Realm realm, Activity activity) {

        this.realm = realm;
        this.activity = activity;
    }

    public void showDialog() {
        CustomAlertDialog dialog = new CustomAlertDialog(this, activity, true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogInterface.dismiss();
            }
        });
        dialog.show();
    }

    public void importData() {

        showFileChooser();
        if ( FILE_NAME.isEmpty() ) {
            return;
        }

        try {
            FileInputStream inputStream = new FileInputStream(new File(FILE_NAME));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            while( bufferedReader.ready() ) {
                String str = bufferedReader.readLine();
                String[] data = str.split(",");
                // 0: Server IP  1: Party ID  2: User ID
                BookMarkHolder holder = new BookMarkHolder(data[0], data[1], data[2]);
                realm.insert(holder);
            }
            MessageWrapper.SendMessage(activity, "BookMark Add...");
        }
        catch ( Exception e) {
            Log.e("IoEBookMarkHelper", "File no find!");
        }

    }

    public void exportData() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd", Locale.TAIWAN);
        OUTPUT_FILE = Environment.getExternalStorageDirectory() + "/output_" + dateFormat.format(calendar.getTime()) + ".data";
        try {
            FileOutputStream inputStream = new FileOutputStream(new File(OUTPUT_FILE));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(inputStream));
            RealmResults<BookMarkHolder> results = realm.where(BookMarkHolder.class).findAll();
            for( BookMarkHolder bookmark : results ) {
                bw.write(bookmark.serverip + "," + bookmark.partyid + "," + bookmark.memberid);
                bw.newLine();
            }
            bw.flush();
            bw.close();
            MessageWrapper.SendMessage(activity, "Save at " + OUTPUT_FILE);
        }
        catch (Exception e) {
            Log.e("IoEBookMarkHelper", e.toString());
        }
    }


    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all file
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            activity.startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            MessageWrapper.SendMessage(activity, "No File browser detected.");
        }
    }



}