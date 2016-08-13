package gqqnbig.simplechat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Kitster on 9/8/2016.
 */
public class MainActivity extends AppCompatActivity {

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
        adp.add(new Contact("222222222222222"));
        new Thread(new getContactThread(new ContactHandler(),sid)).start();
        lv.setAdapter(adp);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle b = new Bundle();
                tv=(TextView)view.findViewById(R.id.contactID);
                String s = tv.getText().toString();
                b.putString("Sender",s);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,chatWith.class);
                intent.putExtras(b);
                MainActivity.this.startActivity(intent);
            }

        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText editText = new EditText(MainActivity.this);

                new AlertDialog.Builder(MainActivity.this).setTitle("Enter IMEI")

                        .setIcon(android.R.drawable.ic_dialog_info).setView(editText)

                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                TextView tv = (TextView) MainActivity.this.findViewById(R.id.contactID);
                                String IMEI = editText.getText().toString();
                                new Thread(new AddContactThread(sid,IMEI)).start();

                                adp.add(new Contact(IMEI));
                            }
                        })

                        .setNegativeButton("NO",null).show();


            }
        });
    }


    private boolean getContact(String host){
        PrintWriter out =null;
        BufferedReader in = null;
        Socket socket=null;
        boolean flag=true;
        while (true){

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {


                socket = new Socket("192.168.43.130", 8888);
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream())), true);
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            out.println("##33");
            out.println(host);

            String Rhost=null;
            String Rguest=null;

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

        }
    }

    class ContactHandler extends Handler {
        public ContactHandler() {
        }

        public ContactHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage。。。。。。");
            super.handleMessage(msg);
            // deal UI
            Bundle b = msg.getData();
            ArrayList<String> Mesg = b.getStringArrayList("contact");
            for(String a:Mesg)
            MainActivity.this.adp.add(new Contact(a));

        }
    }



}
