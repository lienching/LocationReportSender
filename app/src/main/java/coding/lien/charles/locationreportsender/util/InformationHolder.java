package coding.lien.charles.locationreportsender.util;

import android.util.Log;

import java.io.Serializable;

/**
 * Author: lienching
 * Description: This class hold information that we need to send to the server.
 */
public class InformationHolder implements Serializable{
    private String serverip;
    private String partyid;
    private String memberid;
    private String devicesstatus;
    private String intervaltime;
    public static boolean isTracking;

    public void setAll( String serverip, String partyid, String memberid) {
        if ( !serverip.startsWith("http") ) {
            serverip = "http://" + serverip;
        } // if

        this.serverip =  serverip;
        this.partyid = partyid;
        this.memberid = memberid;

        Log.d("InformationHolder", this.serverip);
        Log.d("InformationHolder", this.partyid);
        Log.d("InformationHolder", this.memberid);

    } // setAll( String, String, String, String, String )

    public void setAll( String serverip, String partyid, String memberid, String devicesstatus, String intervaltime) {
        if ( !serverip.startsWith("http") ) {
            serverip = "http://" + serverip;
        } // if

        this.serverip =  serverip;
        this.partyid = partyid;
        this.memberid = memberid;
        this.devicesstatus = devicesstatus;
        this.intervaltime = intervaltime;

        Log.d("InformationHolder", this.serverip);
        Log.d("InformationHolder", this.partyid);
        Log.d("InformationHolder", this.memberid);
        Log.d("InformationHolder", this.devicesstatus);
        Log.d("InformationHolder", this.intervaltime);

        InformationHolder.isTracking = true;
    } // setAll( String, String, String, String, String )

    public void setServerip(String serverip) {
        this.serverip = serverip;
    } // SetServerip( String )

    public void setPartyid(String partyid) {
        this.partyid = partyid;
    } // SetPartyid( String )

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    } // SetMemberid( String )

    public void setDevicesstatus(String devicesstatus) {
        this.devicesstatus = devicesstatus;
    } // SetDevicesstatus( String )

    public void setIntervaltime(String intervaltime) {
        this.intervaltime = intervaltime;
    } // setIntervaltime( String )

    public String getServerip() {
        return this.serverip;
    } // getServerip()

    public String getPartyid() {
        return this.partyid;
    } // getPartyid()

    public String getMemberid() {
        return this.memberid;
    } // getMemberid()

    public String getDevicesstatus() {
        return this.devicesstatus;
    } // getDevicesstatus()

    public String getIntervaltime() {
        return this.intervaltime;
    } // getIntervaltime()

} // class InformationHolder
