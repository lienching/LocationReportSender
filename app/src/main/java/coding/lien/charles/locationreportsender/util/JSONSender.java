package coding.lien.charles.locationreportsender.util;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lienching on 6/21/16.
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
    }

    public boolean SendJson(JSONObject json, String url) throws IOException {

        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();


        Log.d("JsonSender", response.message());
        return response.isSuccessful();
    } // SendJson( JSONObject, String )

} // class JSONSender
