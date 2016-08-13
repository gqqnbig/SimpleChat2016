package gqqnbig.simplechat;

/**
 * Created by Kitster on 11/8/2016.
 */
public class Contact {
    private String contactID;
    //private String contactMsg;


    public Contact(String contactID) {
        super();
        this.contactID = contactID;
        //this.contactMsg = contactMsg;
    }

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

//    public String getContactMsg() {
//        return contactMsg;
//    }
//
//    public void setContactMsg(String contactMsg) {
//        this.contactMsg = contactMsg;
//    }
}
