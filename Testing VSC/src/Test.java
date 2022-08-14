import java.net.*;

class Test
 {
     static public void main(String[] args)
     {
        System.out.println ("hello world ");
        InetAddress ia =null ;
        if (args.length ==0) {
             System.out.println("No address entered");
             return; }
        int x = 0;
        while (x<args.length){
             try
                 {ia = InetAddress.getByName(args[x]);}
             catch (UnknownHostException e ) {
                System.err.println("unknownhost for " + x + " argument" );
                return;
             }
             String ip = ia.getHostAddress();
             System.out.println("IP Adress for " + args[x] + " is:" + ip );
             String name = ia.getHostName();
             System.out.println(" the host name for " + ip + " is " + name);
             x= x+1;

         }





     }


 }
