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
            BufferedOutputStream output = new BufferedOutputStream(client.getOutputStream(),);
            HttpServerRequest HttpRequest = new HttpServerRequest();
            while (HtppRequest.isDone==false)
            {
                String requestLine = reader.readLine();
                HttpRequest.process(requestLine);
            }
            String file = HttpRequest.getFile();
            String host = HttpRequest.getHost();
            SendFile(file,host,output);
            
    
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
    public static void SendFile(String file,String host,BufferedOutputStream output)
    {
        byte[] data = new byte[20] 
        int offset=0;
        String path = host+"/"+ file ;
        try
        {
            FileInputStream input = new FileInputStream(path);
             String response = "HTTP/1.1 200  OK \r\n \r\n";
             PrintLn(output,response);
            while(input.read()!=-1)
            {
                input.read(data,offset,20);
                output.write(data offset,20);
                offset+=20; 
            }
            output.flush();
            return;
        }
        catch(FileNotFoundException e)
        {
           String response = "HTTP/1.1 404 Not Found \r\n \r\n  404 File not Found";
           PrintLn(output,response);
           return;
        }
    }


    //////Getters and Setters 
    public int GetPort(){return Port;}
    public void SetPort(int port){this.Port = port;}
}