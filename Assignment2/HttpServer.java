import java.net.*;
import java.io.*;
import java.util.*; 

class HttpServer
{
    public static void main(String[] args)
    {
        
        try 
        {
            ServerSocket server = new ServerSocket(51234);
            System.out.println("listning on port 51234");
            while(true)
            {
                Socket client = server.accept();
                HttpServerThread ServerThread = new HttpServerThread(51234);
                ServerThread.start();
                client.close();

            }
        
            
            
        }
        
        catch(Exception e)
        {
            System.err.println("Exception: "+e);
          
        }
        
        
    }
}

class HttpServerThread extends Thread
{
    private int Port;
    public void run(Socket client)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            BufferedOutputStream output = new BufferedOutputStream(client.getOutputStream());
            while ((reader.readLine()!=null)||reader.readLine()!="")
            {
                String request = reader.readLine();
                String response = "200  OK \r\n \r\n Hello world";
                PrintLn(output,response);
                
            }
        }
        catch(Exception e)
        {
            System.err.println("Exception: " + e);
        }
    } 
    
    public HttpServerThread(int port)
    {
       SetPort(port);
    }
    ///////Methods
    public static void  PrintLn(BufferedOutputStream output,String message)
    {
        String NewMessage= message + "\r\n";
        byte[] array = NewMessage.getBytes();
        try
        {
            output.write(array,0,array.length);
        }
        catch(Exception e)
        {
         System.err.println("Exception: " + e);
        }
        
    }

    //////Getters and Setters 
    public int GetPort()
    {
        return Port;
    }
    public void SetPort(int port)
    {
      this.Port = port ;
    }
}