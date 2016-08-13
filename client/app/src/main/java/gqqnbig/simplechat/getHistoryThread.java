package gqqnbig.simplechat;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by acer on 2016/8/12.
 */
public class getHistoryThread implements Runnable{
    Handler handler;
    String receiverID;
    String senderID;
    Context mContext;

    public getHistoryThread(String receiverID, String senderID, Handler handler, Context mContext){
        this.handler=handler;
        this.receiverID=receiverID;
        this.senderID=senderID;
        this.mContext=mContext;
        //this.adp=adp;
    }

    @Override
    public void run() {
        try {
            FileInputStream input = mContext.openFileInput(senderID+receiverID);

            byte[] temp = new byte[1024];

            int len = 0;
            int co=0;
            StringBuilder sb = new StringBuilder("");
            while ((len = input.read(temp)) > 0) {
                //把字条串连接到尾部
                sb.append(new String(temp, 0, len));
            }
            char a=0;
            Log.v("debug","--------/*5/*74654132156---------"+a);
           String content= (sb.toString().replace('#',a)).replace("\n","#");

              int begin=0;
            for(int i=0;i<content.length();i++){
                 if(content.charAt(i)=='#') {

                     Message msg = new Message();
                     Bundle b = new Bundle();// 存放数据
                     b.putString("Mesg", sb.substring(begin,i).replace(a,'#'));
                     msg.setData(b);
                     getHistoryThread.this.handler.sendMessage(msg);
                     begin=i+1;
                     //把字条串连接到尾部

                 }else {

                 }
              ;
            }
            Log.v("debug",""+co);
          // mContext.deleteFile(senderID+receiverID);// Delete local file
            //关闭输入流
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
