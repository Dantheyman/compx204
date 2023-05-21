import java.net.*;
import java.io.*;
class TftpServer
{
    public static void main(String args[])
    {
	try {
	    /*
	     * allocate a DatagramSocket, and find out what port it is
	     * listening on
	     */
	    DatagramSocket ds = new DatagramSocket();
	    System.out.println("TftpServer on port " + ds.getLocalPort());

	    for(;;) {
		/*
		 * allocate a byte buffer to back a DatagramPacket
		 * with.  I suggest 1472 byte array for this.
		 * allocate the corresponding DatagramPacket, and call
		 * DatagramSocket::receive
		 */
		byte[] buf = new byte[1472];
		DatagramPacket p = new DatagramPacket(buf, 1472);
        
		ds.receive(p);
        

		/*
		 * allocate a new worker thread to process this
		 * packet.  implement the logic looking for a RRQ in
		 * the worker thread's run method.
		 */
		TftpServerWorker worker = new TftpServerWorker(p);
		worker.start();
	    }
	}
	catch(Exception e) {
	    System.err.println("TftpServer::main Exception: " + e);
	}

	return;
    }
}

class TftpServerWorker extends Thread
{
    private DatagramPacket req;

    public void run()
    {
            /* parse the request packet, ensuring that it is a RRQ. */
        TftpPacket tp=TftpPacket.parse(req);
        
        if (tp.getType()!=TftpPacket.Type.RRQ)
        {
            return;

        }
        

        /*
        * make a note of the address and port the client's request
        * came from
        */
        int port = tp.getPort();
        InetAddress ia = tp.getAddr();

        /* create a datagram socket to send on, setSoTimeout to 1s (1000ms) */
        DatagramSocket ds = createSocket(port, ia);
        

        /* try to open the file.  if not found, send an error */
        FileInputStream input = createStream(tp.getFilename(),ds,ia,port);
        if (input==null)
        {
            return;
        }
        

        /*
        * Allocate a txbuf byte buffer 512 bytes in size to read
        * chunks of a file into, and declare an integer that keeps
        * track of the current block number initialized to one.
        *
        * allocate a rxbuf byte buffer 2 bytes in size to receive
        * TFTP ack packets into, and then allocate a DatagramPacket
        * backed by that rxbuf to pass to the DatagramSocket::receive
        * method
        */
        int current = 1;
        byte[] dataBuffer = new byte[512];
        byte[] infoBuffer = new byte[2];  
        DatagramPacket dp1 = new DatagramPacket(dataBuffer, dataBuffer.length);
        DatagramPacket dp2 = new DatagramPacket(infoBuffer, infoBuffer.length); 

        while(true)
        {
            /*
            * read a chunk from the file, and make a note of the size
            * read.  if we get EOF, signalled by
            * FileInputStream::read returning -1, then set the chunk
            * size to zero to cause an empty block to be sent.
            */
            int c;
            Boolean NeedToSend=true;
            try
            {
                if ((c =input.read(dataBuffer))!=-1)
                {
                    dp1=TftpPacket.createDATA(ia, port, current, dataBuffer,c);
                    System.out.println("the packet is "+dp1.getLength()+" long");
                
                    System.out.println(c);
                    

                }
                else 
                {
                
                    System.out.println(c);
                    byte[] empty = new byte[0];
                    dp1=TftpPacket.createDATA(ia, port, current, empty,empty.length);
                    System.out.println("we made it empty"); 

                     
                }

            }
            catch(Exception e)
            {
                System.out.println(e);

            }
           

            /*
            * use TftpPacket.createData to create a DATA packet
            * addressed to the client's address and port, specifying
            * the block number, the contents of the block, and the
            * size of the block
            */
            

            /*
            * declare a boolean value to control transmission through
            * each loop, and an integer to count the number of
            * transmission attempts made with the current block.
            */
            int attempts=0;
            Boolean notSent=true;
            

            while(notSent) 
            {
                /*
                * if we are to transmit the packet this pass through
                * the loop, send the packet and increment the number
                * of attempts we have made with this block.  set the
                * boolean value to false to prevent the packet being
                * retransmitted except on a SocketTimeoutException,
                * noted below.
                */
                attempts+=1;

                try
                {
                    ds.send(dp1);
                    System.out.println("we sent a packet");

                }
                catch(SocketTimeoutException e)
                {
                    System.err.println("socket timed out"); 
                    continue;

                }
                catch(Exception e)
                {
                    System.err.println(e);

                }
                notSent=false; 

            
                /*
                * call receive, looking for an ACK for the current
                * block number.  if we get an ack, break out of the
                * retransmission loop.  otherwise, if we get a
                * SocketTimeoutException, set the boolean value to
                * true.  if we have tried five times, then we break
                * out of the loop to give up.
                */
                try
                {
                    ds.receive(dp2);
                    System.out.println("first one worked");
                    tp = TftpPacket.parse(dp2);
                    if((tp.getType()==TftpPacket.Type.ACK)&&(tp.getBlock()==current))
                    {
                        break;

                    }
                    else if (attempts==5)
                    {
                        System.out.println("we tried"); 
                        break;
                    }



                }
                catch (SocketTimeoutException e)
                {
                     System.err.println(e+": line 195");
                     notSent=true;
                    
                }
                catch(Exception e )
                {
                    System.err.println(e+": line 195");
                }
                


            }

            /*
            * outside of the loop, determine if we just sent our last
            * transmission (the block size was less than 512 bytes,
            * or we tried five times without getting an ack
            */
            tp = TftpPacket.parse(dp1);
            if((tp.getData()).length<512||attempts==5)
            {
                break;
            }

            /*
            * use TftpPacket.nextBlock to determine the next block
            * number to use.
            */
            current=TftpPacket.nextBlock(current);

           
        }

        /* cleanup: close the FileInputStream and the DatagramSocket */
        try
        {
            ds.close();
            input.close();

        }
        catch(Exception e)
        {
            System.err.println(e); 
        }
        

        return;
    }

    public TftpServerWorker(DatagramPacket req)
    {
	this.req = req;
    }

    public static DatagramSocket createSocket(int port, InetAddress ia)
    {
        try 
        {
            DatagramSocket ds = new DatagramSocket(port);
            ds.setSoTimeout(1000);
            return ds ;
        }
        catch(Exception e)
        {
            System.err.println(e);
            return null;  
        }

    }
    public static FileInputStream createStream(String name,DatagramSocket ds,InetAddress ia, int port)
    {
        try 
        {
            FileInputStream input = new FileInputStream(name);
            return input; 
        }
        catch(FileNotFoundException e)
        {
            try
            {
                DatagramPacket dp = TftpPacket.createERROR(ia,port,"File not Found");
                ds.send(dp);
                return null;

            }
            catch (Exception v)
            {
                System.err.println(v); 
                return null;
            }
        }
        catch(Exception e)
        {
            System.err.println(e);
            return null; 
        }
    }
}

