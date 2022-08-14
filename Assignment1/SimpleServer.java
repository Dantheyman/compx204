// Daniel Jensen 1576516  
import java.net.*;
import java.io.*;

class SimpleServer{

  public static void main(String[] args)
  {

    ServerSocket server;
    try {
        server= new ServerSocket(0);
        System.out.println("listening on port " + server.getLocalPort());
        // loop that connects to client then finds ip and dns and uses sends message back to client
        while (true){
          Socket client = server.accept();
          InetAddress ia = GetINet(client);
          String ip = Getip(ia);
          String dns = getDns(ia);
          PrintWriter write = new PrintWriter(client.getOutputStream(),true);
          write.print("Hello " + dns );
          System.out.println("in a loop");
          write.println("Your IP Adress is " + ip);
          client.close();
          }
        }

    catch(Exception e) {
      System.err.println("Exception: " + e);
      return;

      }
  }
  //Finds the InetAddress of the client
  public static InetAddress GetINet(Socket client){

    try {
      InetAddress ia = client.getInetAddress();
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
