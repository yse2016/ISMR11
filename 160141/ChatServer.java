import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
	static final int DEFAULT_PORT  = 6000;
	static ServerSocket  servsoch;
	static Vector connections;

	public static void  main(String[] args)
	{
		int port    =  DEFAULT_PORT;
		if(args.length  >  0)
		{
			port = Integer.parseInt(args[0]);
		}
		try{
			servsoch = new ServerSocket(port);
		} catch (IOException  e)  {
			System.err.println(e);
			System.exit(1);
		}
		while(true){
				try{
					Socket cs = servsoch.accept();
					addConnection(cs);
					Thread ct = new Thread(new clientProc(cs));
					ct.start();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
	}

	public static void addConnection(Socket s){
		if (connections ==null)
		 {
		connections	= new Vector();
		}
		connections.addElement(s);
	}

	public static void deleteConnection(Socket s){
		if (connections  != null)
		{
			connections.removeElement(s);
		}
	}


      public static void sendAll(String s){
      	if (connections != null)
      	{
      		for(Enumeration en = connections.elements();  en.hasMoreElements(); )
      		{
      			try{
      			PrintWriter pw = new PrintWriter(((Socket) en.nextElement()).getOutputStream());
      			pw.println(s);
      			pw.flush();
      		} catch(IOException e) {
      		}
      	}
    }
    System.out.println(s);
   }
}

class clientProc implements Runnable{
	Socket sock;
	BufferedReader inBR;
	PrintWriter outPW;
	String name =  null;
	ChatServer chserv =null;
	public clientProc(Socket s) throws IOException
	{
		sock = s;
		inBR = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		outPW = new PrintWriter(sock.getOutputStream());
	}

	public void run() {
		try{
			while (name == null)
			{
				outPW.print("お名前は？：");
				outPW.flush();
				name = inBR.readLine();
			}
			String line = inBR.readLine();
			while(!"quit".equals(line))
			{
				ChatServer.sendAll(name + "> " + line);
				line = inBR.readLine();
			}
			ChatServer.deleteConnection(sock);
			sock.close();
		} catch (IOException e) {
			try{
				sock.close();
			} catch (IOException e2){
			}
		}
	}
}

