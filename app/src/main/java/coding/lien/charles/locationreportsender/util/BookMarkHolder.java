package coding.lien.charles.locationreportsender.util;

import java.io.Serializable;

import io.realm.RealmObject;

/**
 * Author: lienching
 * Description: This class is just a holder that hold info of bookmark
 */

public class BookMarkHolder extends RealmObject implements Serializable {
    public String serverip;
    public String partyid;
    public String memberid;

    public BookMarkHolder() {}

    public BookMarkHolder( String server, String party, String member ) {
        this.serverip = server;
        this.partyid = party;
        this.memberid = member;
    }
}
