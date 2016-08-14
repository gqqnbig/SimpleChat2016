package gqqnbig.simplechat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.*;

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

        Context context = getApplicationContext();
        int permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE);
        if (permissionCheck == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
        else
            createUI();
    }



    private void createUI() {
        TelephonyManager imei = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String sid = imei.getDeviceId();
        //Get the Contact of yourself
        adp.add(new Contact("222222222222222"));
        new Thread(new GetContactThread(new ContactHandler(),sid)).start();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    createUI();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
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
