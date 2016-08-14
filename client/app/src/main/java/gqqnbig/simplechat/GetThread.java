package gqqnbig.simplechat;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer on 2016/8/12.
 */
public class GetThread implements Runnable{
    //private ChatArrayAdapter adp;
    Socket socket;
    String receiverID;
    Handler handler;
    String senderID;
    Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    String SCID;
    String SSID;
    String Stime;
    String Smessage;
    public GetThread(String receiverID,String senderID,Handler handler,Context mContext){
        this.handler=handler;
        this.receiverID=receiverID;
        this.senderID=senderID;
        this.mContext=mContext;
        //this.adp=adp;
    }


    @Override
    public void run() {;

        PrintWriter out =null;
        BufferedReader in = null;
        int index=0;
        while (true){

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
            out.println("##22");
            out.println(receiverID);
            out.println(senderID);


            Log.v("debug","****----****kkk");
            try {
                if (!socket.isClosed()) {
                    if (socket.isConnected()) {
                        if (!socket.isInputShutdown()) {
                            Log.v("debug","****----****kkkk");
                               SCID = in.readLine();
                                SSID=in.readLine();
                                Stime=in.readLine();
                                Smessage=in.readLine();
                            Log.v("debug",SCID + SSID + Stime + Smessage);
                            } else {

                            }
                        }
                    }
                } catch (IOException e) {
                e.printStackTrace();
            }
            Log.v("debug","****----****kkkkk");

            if(SCID==null||SSID==null||Stime==null||Smessage==null){
                Log.v("debug","****--cnn not find--****");
            }else{

                Log.v("debug","****--4444444444444--****");
                //adp.add(new ChatMessage(true,Smessage));
                int a=Integer.parseInt(Stime);
                String time=sdf.format(new Date(a*1000L));
                Message msg = new Message();
                Bundle b = new Bundle();// 存放数据
                b.putString("Mesg",time+":"+Smessage);
                msg.setData(b);
                GetThread.this.handler.sendMessage(msg);

                try {
                    FileOutputStream output = mContext.openFileOutput(senderID+receiverID, Context.MODE_APPEND);


                    output.write((time+":"+Smessage+"\n").getBytes());  //将String字符串以字节流的形式写入到输出流中
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                try {
//                FileInputStream input = mContext.openFileInput(senderID+receiverID);
//
//                byte[] temp = new byte[1024];
//
//                StringBuilder sb = new StringBuilder("");
//                int len = 0;
//                    int co=0;
//                while ((len = input.read(temp)) > 0) {
//                    //把字条串连接到尾部
//                    Log.v("debug",new String(temp));
//                    co++;
//                }
//                    Log.v("debug",""+co);
//                //关闭输入流
//                input.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                //返回字符串
//                index++;



                try {
                    socket = new Socket("192.168.43.130", 8888);

                    out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                out.println("##44");
                out.println(SCID);
                out.flush();
                try {
                    socket.close();
                    socket=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


}
