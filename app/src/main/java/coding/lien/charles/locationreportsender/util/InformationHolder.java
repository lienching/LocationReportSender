package coding.lien.charles.locationreportsender.util;

import android.util.Log;

/**
 * Author: lienching
 * Description: This class hold information that we need to send to the server.
 */
public class InformationHolder {
    private static String serverip;
    private static String partyid;
    private static String memberid;
    private static String devicesstatus;
    private static String intervaltime;
    private static boolean istracking;

    public static void setAll( String serverip, String partyid, String memberid, String devicesstatus, String intervaltime) {
        if ( !serverip.startsWith("http") ) {
            serverip = "http://" + serverip;
        } // if

        InformationHolder.serverip =  serverip;
        InformationHolder.partyid = partyid;
        InformationHolder.memberid = memberid;
        InformationHolder.devicesstatus = devicesstatus;
        InformationHolder.intervaltime = intervaltime;
        InformationHolder.istracking = true;
        Log.d("InformationHolder", InformationHolder.serverip);
        Log.d("InformationHolder", InformationHolder.partyid);
        Log.d("InformationHolder", InformationHolder.memberid);
        Log.d("InformationHolder", InformationHolder.devicesstatus);
        Log.d("InformationHolder", InformationHolder.intervaltime);

    } // setAll( String, String, String, String, String )

    public static void setServerip(String serverip) {
        InformationHolder.serverip = serverip;
    } // SetServerip( String )

    public static void setPartyid(String partyid) {
        InformationHolder.partyid = partyid;
    } // SetPartyid( String )

    public static void setMemberid(String memberid) {
        InformationHolder.memberid = memberid;
    } // SetMemberid( String )

    public static void setDevicesstatus(String devicesstatus) {
        InformationHolder.devicesstatus = devicesstatus;
    } // SetDevicesstatus( String )

    public static void setIntervaltime(String intervaltime) {
        InformationHolder.intervaltime = intervaltime;
    } // setIntervaltime( String )

    public static void stopIsTracking() {
        InformationHolder.istracking = false;
    } // stopIsTracking()

    public static String getServerip() {
        return InformationHolder.serverip;
    } // getServerip()

    public static String getPartyid() {
        return InformationHolder.partyid;
    } // getPartyid()

    public static String getMemberid() {
        return InformationHolder.memberid;
    } // getMemberid()

    public static String getDevicesstatus() {
        return InformationHolder.devicesstatus;
    } // getDevicesstatus()

    public static String getIntervaltime() {
        return InformationHolder.intervaltime;
    } // getIntervaltime()

    public static boolean getIsTracking() { return InformationHolder.istracking; } // getIsTracking()
} // class InformationHolder
