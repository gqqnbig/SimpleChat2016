package gqqnbig.simplechat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by acer on 2016/8/12.
 */
public class AddContactThread implements Runnable{

    Socket socket;
    private String guest;
    private String host;
    public AddContactThread(String host,String guest){
        this.guest=guest;
        this.host=host;

    }

    @Override
    public void run() {
        PrintWriter out =null;
        BufferedReader in = null;



//      try {
//              Thread.sleep(1000);
//          } catch (InterruptedException e) {
//              e.printStackTrace();
//          }
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
            out.println("##55");
            out.println(host);
            out.println(guest);


    }
}
