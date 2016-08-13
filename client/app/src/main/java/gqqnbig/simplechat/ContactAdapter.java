package gqqnbig.simplechat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ContactAdapter extends ArrayAdapter<Contact> {
    private TextView contactID;
    //private TextView contactMsg;
    private List<Contact> contactList = new ArrayList<Contact>();
    private LinearLayout layout;

    public ContactAdapter(Context context, int resource) {
        super(context, resource);
    }



    public void add(Contact object){
        super.add(object);
        contactList.add(object);
    }

    @Override
    public int getCount() {
        return this.contactList.size();
    }

    @Override
    public Contact getItem(int position) {
        return this.contactList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null){
            LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.cadp,parent,false);
        }
        layout = (LinearLayout)v.findViewById(R.id.Contact);
        Contact contactObj = getItem(position);
        contactID =(TextView)v.findViewById(R.id.contactID);
        contactID.setText(contactObj.getContactID());
        //contactMsg = (TextView)v.findViewById(R.id.contactMsg);
        //contactMsg.setText(contactObj.getContactMsg());

        return v;
    }
}
