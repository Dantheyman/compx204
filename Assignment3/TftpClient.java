import java.net.*;


import java.io.*;

class TftpClient

{
    public static void main(String args[])
    {
	    /* expect three arguments */
	    if(args.length != 3) {
	        System.err.println("usage: TftpClient <name> <port> <file>\n");
	        return;
	    }

	    /* process the command line arguments */
	    String name = args[0];
	    String filename = args[2];

	    /*
	     * use Integer.parseInt to get the number from the second
	     * (port) argument
	     */
	    int port;
	    port = Integer.parseInt(args[1]);

	    /*
	     * use InetAddress.getByName to get an IP address for the name
	     * argument
	     */
	    InetAddress ia = getInet(name);
    
	

	    /* allocate a DatagramSocket, and setSoTimeout to 6s (6000ms) */
	    DatagramSocket ds=createSocket(51234, ia);
    
    

	    /*
	     * open an output file; preface the filename with "rx-" so
	     * that you do not try to overwrite a file that the server is
	     * about to try to send to you.
	    */
        String outputFile = "rx-"+filename;
	    FileOutputStream fos=createStream(outputFile);     

	    /*
	    * create a read request using TftpPacket.createRRQ and then
	    * send the packet over the DatagramSocket.
	    */
        DatagramPacket dp=TftpPacket.createRRQ(ia,port,filename); 
        try 
        {
            ds.send(dp);
        } 
        catch(Exception e )
        {
            System.err.println(e);
        }


	

	    /*
        * declare an integer to keep track of the block that you
        * expect to receive next, initialized to one.  allocate a
        * byte buffer of 514 bytes (i.e., 512 block size plus two one
        * byte header fields) to receive DATA packets.  allocate a
        * DatagramPacket backed by that byte buffer to pass to
        * DatagramSocket::receive to receive packets into.
        */

        int current=1; 
        byte[] buffer = new byte[514];
        DatagramPacket recieved = new DatagramPacket(buffer, buffer.length); 
        

        /*
        * an infinite loop that we will eventually break out of, when
        * either an exception occurs, or we receive a block less than
        * 512 bytes in size.
        */
        while(true) 
        {
            try {
            


            /*  
            * receive a packet on the DatagramSocket, and then
            * parse it with TftpPacket.parse.  get the IP address
            * and port where the packet came from.  The port will
            * be different to the port you sent the RRQ to, and
            * we will use these values to transmit the ACK to
            */
            ds.receive(recieved); 
           int length = recieved.getLength();   
            TftpPacket tp = TftpPacket.parse(recieved);
            if (tp!=null){
                port = tp.getPort();
                ia = tp.getAddr();
                

            }
            /*
            * if we could not parse the packet (parse returns
            * null), then use "continue" to loop again without
            * executing the remaining code in the loop.
            */
            
            else if(tp == null)
            {
                continue;
            }
            /*
            * if the response is an ERROR packet, then print the
            * error message and return.
            */
            if(tp.getType()==TftpPacket.Type.ERROR)
            {
                String s= tp.getError();
                System.out.println(s);
                return; 
            }

            /*
            * if the packet is not a DATA packet, then use
            * "continue" to loop again without executing the
            * remaining code in the loop.
            */
            if (tp.getType()!=TftpPacket.Type.DATA)
            {
                continue ;
            }

            /*
            * if the block number is exactly the block that we
            * were expecting, then get the data (TftpPacket::getData)
            * and then write it to disk.  then, send an ack for the
            * block.  then, check to see if we received less than
            * 512 bytes in that block; if we
             did, then we infer that
            * the sender has finished, and break out of the while loop.
            */
            if (tp.getBlock()==current)
            {
                byte[] data =tp.getData();
                System.out.println(length);

                 

                fos.write(data);
                

                dp = TftpPacket.createACK(ia, port, current);
                ds.send(dp);
                if (length<512)
                {
                    break;
                }
                current=TftpPacket.nextBlock(current);

            }

            /*
            * else, if the block number is the same as the block
            * number we just received, send an ack without writing
            * the block to disk, etc.  in this case, the server
            * didn't receive the ACK we sent, and retransmitted.
            */
            else if(tp.getBlock()==TftpPacket.lastBlock(current)) 
            {
                dp = TftpPacket.createACK(ia, port, current);
                ds.send(dp);
            }
            } catch(Exception e) {
            System.err.println("Exception: " + e);
            break;
            }
        }

        /* cleanup -- close the output file and the DatagramSocket */
        
        try 
        {
            fos.close();
            ds.close();
        }
        catch(Exception e)
        {
            System.err.println(e);

        }

    }

    


    public static InetAddress getInet(String name)
    {
        try 
        {
            InetAddress ia = InetAddress.getByName(name);
            return ia; 
        }
        catch(Exception e)
        {
            System.err.println(e ); 
            return null;
        }
    }

    public static DatagramSocket createSocket(int port, InetAddress ia)
    {
        try 
        {
            DatagramSocket ds = new DatagramSocket(port);
            ds.setSoTimeout(6000);
            return ds ;
        }
        catch(Exception e)
        {
            System.err.println(e);
            return null;  
        }

    }

    public static FileOutputStream createStream(String name)
    {
      try 
        {
            FileOutputStream fos = new FileOutputStream(name);
            return fos;
        }
        catch (Exception e)
        {
            System.err.println(e+" Line 61");
            return null;  
        }
      
    }

}
