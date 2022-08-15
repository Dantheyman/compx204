class HttpServerRequest
{
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    
    public String getHost() { return host; }
    public int getLine(){return line;}

   public HttpServerRequest (){}

    public void process(String in)
    {
        if (line==0)
        {
            String[] parts = in.split(" ");
            
            if (((parts[0].equals("GET"))== false)|| (parts.length!=3))
            {
                System.err.println("Not A well formed request"); 
                return;      
            }
            file=(FileName(parts[1]));
            line=1;
            return;
 

        }

        else if (line>0)
        {
            if (in.equals(""))
            {
                done=true;
            }
            else if (in.startsWith("Host:"))
            {
                host=Host(in) ;
            }
            else 
            {
                line+=1;
            }
        
          return;
        }
    }

    public  String FileName(String part)
    {
        String file=part.substring(1);
        if (file.endsWith("/"))
        {
            file=file+"index.html";
        }   
        
        return file;
    }
    
    public String Host(String in)
    {
        return in.substring(6);
    }
    
}
