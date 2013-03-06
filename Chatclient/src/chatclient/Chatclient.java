/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.util.*;
/**
 *
 * @author ABC
 */
public class Chatclient {

    /**
     * @param args the command line arguments
     */
    JFrame frame;
    JButton add;
    JTextField auname;
    JTextArea incoming,outgoing;
    JPanel sendbox;
    JButton sendb;
    BufferedReader reader;
    PrintWriter writer;
    String cname;
    JPanel addpanel;
    JTextArea useronline;
    int flag;
    Chatclient(){
        flag=0;
        frame=new JFrame("CHATTER                         by amourphious");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sendbox=new JPanel();
        addpanel=new JPanel();
        add=new JButton("ADD");
        add.addActionListener(new Addlistener());
        auname=new JTextField("Add");
        incoming=new JTextArea(15,30);
        incoming.setText("messages here");
        outgoing=new JTextArea(5,31);
        outgoing.setText("Type here");
        incoming.setLineWrap(true);
        outgoing.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        outgoing.setWrapStyleWord(true);
        JScrollPane qscroller=new JScrollPane(incoming);
        qscroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        qscroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        sendb=new JButton("SEND");
        sendb.addActionListener(new Sendlistener());
        sendbox.add(qscroller);
        sendbox.add(outgoing);
        outgoing.disable();
        incoming.disable();
        addpanel.add(add);
        addpanel.add(auname);
        frame.getContentPane().add(BorderLayout.CENTER,sendbox);
        frame.getContentPane().add(BorderLayout.SOUTH,sendb);
        frame.getContentPane().add(BorderLayout.WEST,addpanel);
        frame.setSize(700, 450);
        netsetup();
        getcname();
        Incomingwriter t1=new Incomingwriter();
    }
    public class Addlistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            try {
                writer.println(auname.getText());
                writer.flush();
                outgoing.setText("");
            } catch (Exception e) {
                System.out.println("ex at addlistener");
            }
            incoming.enable();
            incoming.setText("chat started\n");
            outgoing.enable();
            outgoing.setText("");
            flag=1;
        }
    }
    public void netsetup(){
        try{
            Socket sock=new Socket("127.0.0.1",5000);
            InputStreamReader istr=new InputStreamReader(sock.getInputStream());
            reader=new BufferedReader(istr);
            writer=new PrintWriter(sock.getOutputStream());
        }catch(Exception io){
            System.out.println("exception in netsetup"); 
        }
    }
    class Sendlistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            try{
                writer.println(cname+": "+outgoing.getText());
                writer.flush();
                outgoing.setText("");
            }catch(Exception e){
                System.out.println("excep at listener");
            }
        }
    }
    JTextField field;
    JFrame dframe;
    public void getcname(){
        dframe=new JFrame("enter name");
        dframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        field=new JTextField("chatter_x");
        JButton button=new JButton("OK");
        button.addActionListener(new Namelistener());
        dframe.getContentPane().add(BorderLayout.NORTH,new JLabel("enter your name"));
        dframe.getContentPane().add(BorderLayout.CENTER,field);
        dframe.getContentPane().add(BorderLayout.SOUTH,button);
        dframe.setSize(250, 110);
        dframe.setVisible(true);
    }
    class Namelistener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent ae){
            cname=field.getText();
            try{
                writer.println(cname);
                writer.flush();
            }catch(Exception e){
                System.out.println("ex at namelistener");
            }
            dframe.setVisible(false);
            frame.setVisible(true);
        }
    }
    class Incomingwriter implements Runnable{
        Thread t;
        Incomingwriter(){
            t=new Thread(this,"display");
            t.start();
        }
        @Override
        public void run(){
            String message;
            try{
                while((message=reader.readLine())!=null){
                    incoming.append(message+"\n");
            }
            }catch(Exception e){
                System.out.println("ex in thread");
            }      
        }
    }
    public static void main(String[] args) {
        Chatclient client=new Chatclient();
    }
}
