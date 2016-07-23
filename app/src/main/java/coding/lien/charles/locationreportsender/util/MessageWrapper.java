package coding.lien.charles.locationreportsender.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Author: lienching
 * Description: This class is message wrapper.
 */
public class MessageWrapper {

    public static void SendMessage(Context context, String msg ) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    } // SendMessage( String )
} // class MessageWrapper
