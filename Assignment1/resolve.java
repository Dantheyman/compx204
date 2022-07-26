// Daniel Jensen 1576516

import java.net.*;

class resolve{
      public static void main (String[] args)
      {
        // checks to see if no arguments were entered if so prints a usage statment and returns
        if (args.length ==0){
           System.err.println("usage: resolve <name1> .. <nameN>");
           return;
           }
      int x = 0;
        // calls the find ip method for each string in args
         while (args.length-x>0){
         find_ip(args[x]);
         x=x+1;

         }

      }
         //finds the ip for each name and prints it. if it can find an ip
         // prints "unknownhost"
      public static void find_ip(String arg){
              InetAddress ia;
            try{
                ia= InetAddress.getByName(arg);
                }

             catch(UnknownHostException e){
                System.err.println(arg + " : " +  "unknown host");
                return;
                }
             catch(SecurityException e){
                System.err.println("Security exception");
                return;
                }

             String ip = ia.getHostAddress();
             System.out.println(arg + " : " + ip);
             return;

      }

}
