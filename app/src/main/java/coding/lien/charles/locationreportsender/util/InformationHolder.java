package coding.lien.charles.locationreportsender.util;

/**
 * Created by lienching on 6/22/16.
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
    }

    public static void setServerip(String serverip) {
        InformationHolder.serverip = serverip;
    }

    public static void setPartyid(String partyid) {
        InformationHolder.partyid = partyid;
    }

    public static void setMemberid(String memberid) {
        InformationHolder.memberid = memberid;
    }

    public static void setDevicesstatus(String devicesstatus) {
        InformationHolder.devicesstatus = devicesstatus;
    }

    public static void setIntervaltime(String intervaltime) {
        InformationHolder.intervaltime = intervaltime;
    }

    public static void stopIsTracking() {
        InformationHolder.istracking = false;
    }

    public static String getServerip() {
        if ( serverip.substring(0,7).compareTo("http://") != 0 ) {
            serverip = "http://" + serverip;
        }
        return serverip;
    }

    public static String getPartyid() {
        return partyid;
    }

    public static String getMemberid() {
        return memberid;
    }

    public static String getDevicesstatus() {
        return devicesstatus;
    }

    public static String getIntervaltime() {
        return intervaltime;
    }

    public static boolean getIsTracking() { return istracking; }
}
