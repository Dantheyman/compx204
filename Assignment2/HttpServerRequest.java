class HttpServerRequest
{
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;
    private int error= 0;
    //private ArrayList<Strings> Headers ;

    public boolean isDone() { return done; }
    public void setDone() {this.done=true;}
    public String getFile() { return file; }
    public void setFile(String File){this.file=File;}
    public String getHost() { return host; }
    public void setHost(String Host){this.host=Host;}

    public void SetLine(int line){this.line=line;}
    public int getLine(){return line;}

   public HttpServerRequest (){}

    public void process(String in)
    {
        if (getLine()==0)
        {
            String[] parts = in.split("");
            
            if (((parts[0].equals("GET"))== false)|| (parts.length!=3))
            {
                System.err.println("Not A well formed request"); 
                return;      
            }
            setfile(FileName(part[1]));
            setError(1);
            return;
 

        }

        else if (getLine()>0)
        {
            if (in.equals(""))
            {
                setDone();
            }
            else if (in.startsWith("HOST"))
            {
                setHost(Host(in)) ;
            }
            else 
            {
                SetLine(getLine()+1);
            }
        
          return;
        }
    }

    public  String FileName(String part)
    {
        String file=part.substring(1);
        if (file.endsWith("/"))
        {
            file=file+"index.hmtl";
        }   
        
        return file;
    }
    
    public String Host(String in)
    {
        return part.substring(4);
    }
}
