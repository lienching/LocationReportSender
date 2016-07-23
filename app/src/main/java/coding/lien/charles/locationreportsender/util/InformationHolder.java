package coding.lien.charles.locationreportsender.util;

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
        InformationHolder.serverip =  serverip;
        InformationHolder.partyid = partyid;
        InformationHolder.memberid = memberid;
        InformationHolder.devicesstatus = devicesstatus;
        InformationHolder.intervaltime = intervaltime;
        InformationHolder.istracking = true;
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
        if ( serverip.substring(0,7).compareTo("http://") != 0 ) {
            serverip = "http://" + serverip;
        } // if
        return serverip;
    } // getServerip()

    public static String getPartyid() {
        return partyid;
    } // getPartyid()

    public static String getMemberid() {
        return memberid;
    } // getMemberid()

    public static String getDevicesstatus() {
        return devicesstatus;
    } // getDevicesstatus()

    public static String getIntervaltime() {
        return intervaltime;
    } // getIntervaltime()

    public static boolean getIsTracking() { return istracking; } // getIsTracking()
} // class InformationHolder
