/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver2;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author ABC
 */
public class Chatserver2 {

    /**
     * @param args the command line arguments
     */
    Map clientoutputstream;
    ServerSocket s2;

    public Chatserver2() {
        try{
            s2=new ServerSocket(4242);
            clientoutputstream=new HashMap<String,PrintWriter>();
            go();
        }catch(Exception e){
            System.out.println("ex");
        }
    }
    
    
    public class ClientHandler implements Runnable{
        Socket sock;
        BufferedReader rdr;
        PrintWriter wri;
        String clientname;
        Thread t;
        public ClientHandler(Socket s){
            try{
                sock=s;
                InputStreamReader isr=new InputStreamReader(sock.getInputStream());
                rdr=new BufferedReader(isr);
                wri=new PrintWriter(sock.getOutputStream());
                t=new Thread(this);
                getname();
            }catch(Exception e){
                System.out.println("exception");
            }
        }
        public void getname() {
            try{
                clientname=rdr.readLine();
                System.out.println("getclientname : "+clientname);
                clientoutputstream.put(clientname, this.wri);
            }catch(Exception e){
                System.out.println("ex");
            }
                 //writer.println(clientname);
                 t.start();
                 
        }
        @Override
        public void run(){
            while(true){
                senduser();
                try{Thread.sleep(5000);
                InetAddress localaddr = InetAddress.getLocalHost();
                    System.out.println(localaddr);
                }catch(Exception e){e.printStackTrace();}
            }
        }
        public void senduser(){
            String senduseru=new String();
            String senduserx=new String();
            Set s=clientoutputstream.entrySet();
            Iterator it=s.iterator();
            while(it.hasNext()){
                Map.Entry m=(Map.Entry)it.next();
                senduserx=(String)m.getKey();
                System.out.println("online: "+senduserx);
                senduseru=(senduseru+senduserx+";");
            }
            wri.println(senduseru);
            System.out.println("senduser: "+senduseru);
            wri.flush();
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        
        new Chatserver2();
    }
    public void go(){
    try{
        while(true){
            Socket client=s2.accept();
            new ClientHandler(client);
        }
    }catch(Exception e){
        System.out.println("ec");
    }
}
}
