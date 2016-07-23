package coding.lien.charles.locationreportsender.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import coding.lien.charles.locationreportsender.R;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Author: lienching
 * Description: This class handle all JSON sending process and this class use singleton to
 *              avoid more than one sender.
 */
public class JSONSender {

    private Context user_context;

    private static JSONSender sender;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client;

    private JSONSender(Context context ) {
        this.user_context = context;
        client = new OkHttpClient();
    } // Constructor JSONSender( context )

    public static void CreateSender( Context context ) {
        sender = new JSONSender( context);
    }

    public static JSONSender getSender() {
        if ( sender == null ) return null;
        return sender;
    } // Singleton JSONSender

    public boolean SendJson(JSONObject json, String url) throws IOException {

        boolean isSuccess;
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        Log.d("JsonSender", response.message());
        isSuccess = response.isSuccessful();

        if ( response.code() == 404 ) {
            MessageWrapper.SendMessage(this.user_context, user_context.getResources().getString(R.string.SiteNoFindError));
        } // if

        response.body().close();
        return isSuccess;
    } // SendJson( JSONObject, String )

} // class JSONSender
