/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package chatclient2;

/**
 *
 * @author ABC
 */
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.io.InputStreamReader;
import java.util.*;
public class Chatclient2 implements Runnable{

    /**
     * @param args the command line arguments
     */
    JTextField field;
    JTextArea onlineu;
    JFrame dframe,frame;
    String cname;
    BufferedReader rdr;
    PrintWriter wri;
    Thread t;
    Socket sock;
    Chatclient2(){
        frame=new JFrame("Online Users");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        onlineu=new JTextArea(15,20);
        t=new Thread(this);
        frame.getContentPane().add(BorderLayout.CENTER,onlineu);
        frame.setSize(350, 450);
        try {
            sock=new Socket("192.168.1.3",4242);
            InputStreamReader isr=new InputStreamReader(sock.getInputStream());
            rdr=new BufferedReader(isr);
            wri=new PrintWriter(sock.getOutputStream());
        } catch (Exception e) {
            System.out.println("exce");
        }
        getcname();
    }
    @Override
    public void run(){
        while(true){
            getonuser();
            try{
                Thread.sleep(5000);
            }catch(Exception e){
                System.out.println("ads");
            }
        }
    }
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
                wri.println(cname);
                wri.flush();
            }catch(Exception e){
                System.out.println("ex at namelistener");
            }
            dframe.setVisible(false);
            frame.setVisible(true);
            t.start();
        }
    }
    public void getonuser(){
        String str=new String();
        try{ str=rdr.readLine();}catch(Exception e){
            System.out.println("exception");}
        StringTokenizer st=new StringTokenizer(str,";");
        onlineu.setText("");
        while(st.hasMoreTokens()){
            onlineu.append(st.nextToken()+"\n");
        }
    }
    public static void main(String[] args) {
        // TODO code application logic here
        new Chatclient2();
    }
}
