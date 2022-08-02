import java.net.*;
import java.io.*;

class SimpleServer{

  public static void main(String[] args)
  {

    ServerSocket servsock;
    try {
        servsock= new ServerSocket(0);
        System.out.println("listening on port " + servsock.getLocalPort());
        while (true){
          Socket sock = servsock.accept();
          InetAddress ia = GetINet(servsock);
          String ip = Getip(ia);
          String dns = getDns(ia);
          PrintWriter write = new PrintWriter(sock.getOutputStream(),true);
          write.println("Hello " + dns);
          write.println("Your IP Adress is " + ip);
          sock.close();
          }
        }

    catch(Exception e) {
      System.err.println("Exception: " + e);
      return;

      }
  }
  //Finds the InetAddress of the client
  public static InetAddress GetINet(ServerSocket servsock){

    try {
      InetAddress ia = servsock.getInetAddress();
      return ia;
    }
    catch(Exception e) {
      System.err.println("Exception: " + e);
     return null;
    }
  }
  //finds the ip addres of the client using the InetAddress
  public static String Getip(InetAddress ia ){
    String ip = ia.getHostAddress();
    return ip;

  }
  // finds the DNS of the client using the InetAddress
  public static String getDns(InetAddress ia){
    String dns =  ia.getHostName();
    return dns;
  }


}
