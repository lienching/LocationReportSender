package coding.lien.charles.locationreportsender.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by lienching on 6/21/16.
 */
public class MessageWrapper {

    public static void SendMessage(Context context, String msg ) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    } // SendMessage( String )
}
