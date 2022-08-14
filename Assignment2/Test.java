public class Test 
{
    public static void main (String[] args ){
        String in = args[0];
        String out=HttpServerRequest.FileName(in);
        System.out.println(out);
        
    }
    
}
