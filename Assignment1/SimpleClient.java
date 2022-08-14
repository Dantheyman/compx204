// Daniel Jensen 1576516
import java.net.*;
import java.io.*;

class SimpleClient{

   public static void main (String[] args)
   {
     // makes sure there are 2 arguments
     if (args.length != 2 ){
         System.err.println("usage: Simple client <DNS> <port>");
         }
      // works out InetAddress
     InetAddress ia =null ;
     try {
       ia = InetAddress.getByName(args[0]);
       }
     catch(Exception e ){
         System.err.println("Exception" + e );
         }
    //converts port string to an Integer
      int port;
     try{
       port = Integer.parseInt(args[1]);
       }
     catch(Exception e ){
       System.err.println("Exception: " +e);
       return;
       }

    Socket client=null;
    String message =  null;
    // creates new socket then receives message from the server then pronts it to the screen
    try {
      client = new Socket(ia,port);
      BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
      message = reader.readLine();
      }
      catch (IOException e){
       System.err.println(e);
      }

    System.out.println(message);
    try{
      client.close();
      }
      catch(IOException e){

      }




   }





}
