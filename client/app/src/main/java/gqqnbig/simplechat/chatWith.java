package gqqnbig.simplechat;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class chatWith extends AppCompatActivity {
    private ChatArrayAdapter adp;
    private ListView lv;
    private EditText et;
    private Button btn;
    private String RIMEI;
    //private int receiverID;
    Intent i;



    private boolean side = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       TelephonyManager imei = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
       final String sid = imei.getDeviceId().toString();
        Intent i = getIntent();
        Bundle b=getIntent().getExtras();
        final String RIMEI=b.getString("Sender");


        setContentView(R.layout.content_chat);
        btn = (Button) findViewById(R.id.button);
        lv = (ListView) findViewById(R.id.message);
        adp = new ChatArrayAdapter(getApplicationContext(), R.layout.chat);
        et = (EditText) findViewById(R.id.editText);
        lv.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        Log.v("debug","****----****"+RIMEI);
        new Thread(new getHistoryThread(sid,RIMEI,new GetHandler(),getApplicationContext())).start();

        new Thread(new GetThread(sid,RIMEI,new GetHandler(),getApplicationContext())).start();
        lv.setAdapter(adp);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                new Thread(new SendThread(sid,RIMEI,et.getText().toString(),getApplicationContext())).start();
              //  new Thread(new TestGet()).start();

                //new Thread(new TestGet()).start();
                sendChatMessage();
            }
        });
        adp.registerDataSetObserver(new DataSetObserver() {

            public void OnChanged(){
                super.onChanged();

                lv.setSelection(adp.getCount() -1);
            }


        });
    }


//    class Conn extends AsyncTask<Void, Void, MainActivity> {
//
//        @Override
//        protected MainActivity doInBackground(Void... params) {
////           // Looper.prepare();
////            try {
////                CallHandler callHandler = new CallHandler();
////                Client client = new Client(serverIP, 8888, callHandler);
////                IServer testService = (IServer) client.getGlobal(IServer.class);
////                testService.pullMesg("5");
////                Log.v("debug","----*3******#**");
////
////                client.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////            //Looper.loop();
//            //Looper.prepare();
//            try {
//                CallHandler callHandler = new CallHandler();
//                Client client = new Client(serverIP, 8888, callHandler);
//                IServer testService = (IServer) client.getGlobal(IServer.class);
//                testService.send("123199931293", "987654321012345", "iloveyou");
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            //Looper.loop();
//            return null;
//        }
//
//    }

    private boolean sendChatMessage(){
        adp.add(new ChatMessage(side, et.getText().toString()));
        et.setText("");
        //side = !side;
        return true;
    }



    class GetHandler extends Handler {
        public GetHandler() {
        }

        public GetHandler(Looper L) {
            super(L);
        }
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            Log.d("MyHandler", "handleMessage。。。。。。");
            super.handleMessage(msg);
            // deal UI
            Bundle b = msg.getData();
            String Mesg = b.getString("Mesg");
            chatWith.this.adp.add(new ChatMessage(true,Mesg));

        }
    }





}
