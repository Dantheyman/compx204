// Daniel Jensen 1576516

import java.net.*;

class reverse{
      public static void main (String[] args)
      {
        // checks to see if no arguments were entered if so prints a usage statment and returns
        if (args.length ==0){
           System.err.println("usage: resolve <ip1> .. <ipN>");
           return;
           }
      int x = 0;
        // calls the find ip method for each string in args
         while (args.length-x>0){
         find_dns(args[x]);
         x=x+1;

         }

      }
         //finds the ip given the text rep then finds the dns name and prints it. if it cant find a name it
         // prints "No name"
      public static void find_dns(String arg){
              InetAddress ia;
            try{
                ia= InetAddress.getByName(arg);
               }

             catch(UnknownHostException e){
                System.err.println(arg + " : " +  "No name");
                return;
                }
             catch(SecurityException e){
                System.err.println("Security exception");
                return;
                }

             String ip = ia.getHostName();
             System.out.println(arg + " : " + ip);
             return;

      }

}
