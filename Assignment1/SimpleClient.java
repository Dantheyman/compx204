import java.net.*;
import java.io.;

class SimpleClient{

   public static void (String[] args)
   {
     if (args != 2 ){
         System.err.println("usage: Simple client <DNS> <port>")
         }
     InetAddress ia;
     try {
       ia = InetAddress.getByName(args[0]);
       }
     catch(exception e ){
         System.err.println("Exception" + e );
         }

     InetAddress ia = InetAddress.getByName(args);
     try{
       int port = Integer.parseint(args[1]);
       }
     catch(excetption e ){
       System.err.println("Exception: " +e);
       return;
     } 
    Socket Client = new Socket(ia,port)




   }





}
