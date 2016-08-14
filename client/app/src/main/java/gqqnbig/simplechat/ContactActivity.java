package gqqnbig.simplechat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kitster on 12/8/2016.
 */
public class ContactActivity extends AppCompatActivity {
    private ListView lv;
    private ContactAdapter adp;
    private TextView tv;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);
        lv = (ListView) findViewById(R.id.contacts_list);
        adp = new ContactAdapter(getApplicationContext(), R.layout.cadp);
        lv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        TelephonyManager imei = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String sid = imei.getDeviceId().toString();
        //Get the Contact of yourself
        while (!getContact(sid));
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle b = new Bundle();
                tv=(TextView)view.findViewById(R.id.contactID);
                String s = tv.getText().toString();
                b.putString("Receiver",s);
                Intent intent = new Intent();
                intent.setClass(ContactActivity.this,chatWith.class);
                intent.putExtras(b);
                ContactActivity.this.startActivity(intent);
            }

        });
    }
    private boolean getContact(String host){
        PrintWriter out =null;
        BufferedReader in = null;
        Socket socket=null;
        boolean flag=true;
        while (true){
            Log.v("debug","****----****"+host);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {

                Log.v("debug","****----****k");
                socket = new Socket("192.168.43.130", 8888);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("debug","****----****kk");
            out.println("##33");
            out.println(host);

            String Rhost=null;
            String Rguest=null;
            Log.v("debug","****----****kkk");
            try {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                          while(flag){
                              Rhost=in.readLine();
                              if(Rhost==null){socket.close();return false;}
                              if(Rhost.equals("##33")){
                                  socket.close();
                                  return true;
                              }else {
                                  Rguest=in.readLine();
                                  if(Rguest==null){socket.close();return false;}
                                  else adp.add(new Contact(Rguest));
                              }
                          }
                        }
                    }
                } else {
                    socket.close();
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("debug","****----****kkkkk");
    }
}
}
