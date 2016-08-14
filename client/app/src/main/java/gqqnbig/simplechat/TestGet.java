package gqqnbig.simplechat;

import android.util.Log;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by acer on 2016/8/12.
 */
public class TestGet implements Runnable {
    private ChatArrayAdapter adp;
    Socket socket;
    String receiverID;


    String SCID;
    String SSID;
    String Stime;
    String Smessage;
    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out =null;

        try {
            socket = new Socket("192.168.43.130", 8888);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.println("##22");
        out.println("222222222222222");

        String SCID=null;
        String SSID=null;
        String Stime=null;
        String Smessage=null;
        try {
            // while (true) {
            if (!socket.isClosed()) {
                if (socket.isConnected()) {
                    if (!socket.isInputShutdown()) {
                        if ((SCID = in.readLine()) != null) {
                            SSID=in.readLine();
                            Stime=in.readLine();
                            Smessage=in.readLine();
                            Log.v("debug",SCID+SSID+Stime+Smessage);

                            //mHandler.sendMessage(mHandler.obtainMessage());
                        } else { Log.v("debug","failed to pass the mesg back");

                        }
                    }
                }
            }
            //}
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(!(SCID!=null&&SSID!=null&&Stime!=null&&Smessage!=null)){
            Log.v("debug","failed to pass the mesg back");
        } else{
            try {

                socket.close();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }}

        try {

            socket.close();
            socket=null;
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }



        try {
            socket = new Socket("localhost", 8888);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        out.println("##44");
        out.println(SCID);
    }


}
