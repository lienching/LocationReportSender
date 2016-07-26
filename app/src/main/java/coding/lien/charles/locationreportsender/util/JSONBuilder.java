package coding.lien.charles.locationreportsender.util;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Author: lienching
 * Description: This class handle JSON building process.
 */
public class JSONBuilder {

    private String s_group;
    private String s_member;
    private String s_status;
    private String s_azimuth;
    private JSONObject json_properties;

    public JSONBuilder( String groupid, String memberid, String status, String azimuth ) {
        this.s_group = groupid;
        this.s_member = memberid;
        this.s_status = status;
        this.s_azimuth = azimuth;
        InitJson();
    } // Constructor JSONBuilder

    private void InitJson() {
        Map<String,String> tmp = new HashMap<String,String>();
        tmp.put("group_id", s_group );
        tmp.put("user_id", s_member );
        tmp.put("status", s_status );
        // tmp.put("azimuth", s_azimuth );
        json_properties = new JSONObject(tmp);
    } // InitJson()

    public JSONObject BuildGeoJson( double lat, double lon ) {
        try {
            JSONObject result;
            Point nowpoint = new Point(lat, lon);
            Feature feature = new Feature(nowpoint);
            feature.setProperties(json_properties);
            result = feature.toJSON();
            return result;
        } // try
        catch ( Exception e ) {
            Log.e("JSONBuilder", e.toString());
        } // catch( Exception )

        return null;
    } // BuildGeoJson()

} // class JSONBuilder
