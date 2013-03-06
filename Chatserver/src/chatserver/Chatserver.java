/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatserver;
import java.io.*;
import java.net.*;
import java.util.*;

/**
 *
 * @author ABC
 */
public class Chatserver {

    /**
     * @param args the command line arguments
     */
    Map<String,clienthandler> onlineuser;
    ServerSocket ss;
    Chatserver(){
        try{
        ss=new ServerSocket(5000);
        onlineuser=new HashMap<String,clienthandler>();
        go();
        }catch(Exception e){
            System.out.println("exception");
            
        }
    }
   public class clienthandler implements Runnable{
         Map<String,PrintWriter> chatwith;
         public BufferedReader reader;
         public PrintWriter writer;
         Socket sock;
         Thread t;
         String clientname;
         clienthandler(Socket client){
             try{
                 sock=client;
                 chatwith=new HashMap<String, PrintWriter>();
                 InputStreamReader istr=new InputStreamReader(sock.getInputStream());
                 reader=new BufferedReader(istr);
                 writer=new PrintWriter(sock.getOutputStream());
                 t=new Thread(this);
                 getclientname();
                 
                 
             }catch(Exception e){
                 e.printStackTrace();
                 System.out.println("excepyion");
         }
   
        }
        @Override
         public void run(){
           String msg;
           try{
               while((msg=reader.readLine())!=null){
                   System.out.println("read" + msg);
                   tell(msg);
               }
               }catch(Exception e){
                   System.out.println("exception");
                   e.printStackTrace();
               }
           }

        public void getclientname() {
            try{
                clientname=reader.readLine();
                System.out.println("getclientname : "+clientname);
                onlineuser.put(clientname, this);
                chatwith.put(clientname, this.writer);
            }catch(Exception e){
                System.out.println("ex");
            }
                 //writer.println(clientname);
                 
                 getchatwith();
        }

        public void getchatwith() {
            String x=new String();
            String u=new String();
            String f=new String();
            try{ x=reader.readLine();}catch(Exception e){
                System.out.println("expci");}
            StringTokenizer st=new StringTokenizer(x, ";");
            while(st.hasMoreTokens()){
            Set s=onlineuser.entrySet();
            Iterator it=s.iterator();
            u=st.nextToken();
            while(it.hasNext()){
                Map.Entry m=(Map.Entry)it.next();
                f=(String)m.getKey();
                if(f.equals(u) &&!(f.equals(clientname))){
                    clienthandler a=(clienthandler)m.getValue();
                    chatwith.put(f,a.writer);
                    a.chatwith.put(this.clientname,this.writer);
                    System.out.println(this.clientname+" : "+a.clientname);
                }
            } 
            }
            t.start();
        }

        public void tell(String msg) {
         Set s=chatwith.entrySet();
         PrintWriter wri;
         String a;
         Iterator it=s.iterator();
         while(it.hasNext()){
             Map.Entry m=(Map.Entry)it.next();
             wri=(PrintWriter)m.getValue();
             a=(String)m.getKey();
             System.out.println("saying to: "+a+" : "+msg);
             wri.println(msg);
             wri.flush();
         }
        }
         }
    public void go(){
        while(true){
            Socket clientsocket;
            try {
                clientsocket=ss.accept();
                new clienthandler(clientsocket);
            } catch (Exception e) {
            }
            
        }
    }
         
     
    public static void main(String[] args) {
        // TODO code application logic here
        new Chatserver();
    }
}
