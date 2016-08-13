package gqqnbig.simplechat;

/*
 * Created by Kitster on 9/8/2016.
 */




public interface IServer {


    public void send(String senderID,String receiverID,String message) ;


    public void online(String senderID) ;

    public String[] pullMesg(String receiverID);

    public void received(String conversationID) ;

    public void testInsert(int test1,String test);

    public int testGet();
}