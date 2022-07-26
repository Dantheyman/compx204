.import java.net.*;
class SimpleServer{

  public static void main(String[] args)
  {
    ServerSocket servsock;
    try {
    servsock= Serversocket(2222)
       while (true){
      socket Sock = servsock.accept();

       }
    }
    catch(IOException e ){
      System.err.println("invalid port address");
      return;
    }
    catch(SecurityException e) {
      System.err.println("SecurityException")

    }

    System.out.println("listening on port  2222" )


  }

}
