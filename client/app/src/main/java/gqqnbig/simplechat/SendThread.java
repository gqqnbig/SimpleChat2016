package gqqnbig.simplechat;

import android.content.Context;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by acer on 2016/8/12.
 */
public class SendThread implements Runnable{
    Socket socket;
    String senderID;
    String receiverID;
    String message;
    Context mContext;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public SendThread( String senderID,String receiverID,String message,Context mContext){
        this.senderID=senderID;
        this.receiverID=receiverID;
        this.message=message;
        this.mContext=mContext;
    }

    @Override
    public void run() {

        PrintWriter out =null;
        try {
            //连接服务器 并设置连接超时为5秒
            socket = new Socket();
            socket.connect(new InetSocketAddress("192.168.43.130", 8888), 5000);

            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    socket.getOutputStream())), true);
            //获取输入输出流
//            OutputStream ou = socket.getOutputStream();
//            BufferedReader bff = new BufferedReader(new InputStreamReader(
//                    socket.getInputStream()));
//
//
//
//            //向服务器发送信息
//            ou.write("android 客户端".getBytes("gbk"));
//            ou.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.println("##11");
        out.println(senderID);
        out.println(receiverID);
        out.println(message);

       String time= sdf.format(new Date(Integer.parseInt(""+System.currentTimeMillis()/1000L)*1000L));
        try {
            FileOutputStream output = mContext.openFileOutput(senderID+receiverID, Context.MODE_APPEND);


            output.write(("*"+time+":"+message+"\n").getBytes());  //将String字符串以字节流的形式写入到输出流中
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        out.flush();
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
