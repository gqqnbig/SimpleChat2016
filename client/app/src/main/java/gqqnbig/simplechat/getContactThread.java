package gqqnbig.simplechat;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by acer on 2016/8/12.
 */
public class getContactThread implements Runnable{


    Handler handler;
    private ArrayList<String> contact=new ArrayList<String>();
    String host;

    public getContactThread(Handler handler,String host){
          this.handler=handler;
        this.host=host;
    }
    @Override
    public void run() {
        while(!getContact(host));
    }


    private boolean getContact(String host){
        PrintWriter out =null;
        BufferedReader in = null;
        Socket socket=null;
        boolean flag=true;

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
                                    break;
                                }else {
                                    Rguest=in.readLine();
                                    if(Rguest==null){socket.close();return false;}
                                    else{
                                        contact.add(Rguest);
                                    }
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
        Message msg = new Message();
        Bundle b = new Bundle();// 存放数据
        b.putStringArrayList("contact",contact);
        msg.setData(b);
        getContactThread.this.handler.sendMessage(msg);
        return true;
    }


}
