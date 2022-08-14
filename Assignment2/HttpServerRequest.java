class HttpServerRequest
{
    private String file = null;
    private String host = null;
    private boolean done = false;
    private int line = 0;

    public boolean isDone() { return done; }
    public String getFile() { return file; }
    public String getHost() { return host; }

    public void process(String in)
    {
        if ((in.equals("GET"))== false)
        {
            return;      
        }

        String[] parts = in.split("");
        this.file=FileName(parts[1]);
        this.host=Host(parts[3]);
            
    
        
     
	/*
	 * process the line, setting 'done' when HttpServerSession should
	 * examine the contents of the request using getFile and getHost
	 */
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
    
    public String Host(String part)
    {

    }
}
