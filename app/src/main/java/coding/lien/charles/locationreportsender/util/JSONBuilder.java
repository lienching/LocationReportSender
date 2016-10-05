package coding.lien.charles.locationreportsender.util;

import android.util.Log;

import com.cocoahero.android.geojson.Feature;
import com.cocoahero.android.geojson.Point;

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
    private JSONObject json_properties;
    private CompassManager compassManager;


    public JSONBuilder( String groupid, String memberid, String status ) {
        this.s_group = groupid;
        this.s_member = memberid;
        this.s_status = status;
        this.compassManager = CompassManager.getInstance();
        InitJson();
    } // Constructor JSONBuilder

    private void InitJson() {
        Map<String,String> tmp = new HashMap<>();
        tmp.put("group_id", s_group );
        tmp.put("user_id", s_member );
        tmp.put("status", s_status );
        json_properties = new JSONObject(tmp);
    } // InitJson()

    public JSONObject BuildGeoJson( double lat, double lon ) {
        try {
            JSONObject result;
            json_properties.put("azimuth", Double.toString(compassManager.getCurrentdegree()));
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
