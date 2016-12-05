import	java.io.*;
import	java.net.*;
import	java.util.*;

public  class CServer{
	static final int DEAULT_PORT = 6000;
	static  ServerSocket servsokc;
	static  Vector connention;

	public static void main(String[] args) {
		int port = DEAULT_PORT;
		if(args.length>0){
			port = Integer.parseInt(args[0]);
		}	
		try{
			servsock = new SeverSocket(port);
		}catch(IOException e){
			System.err.println(e);
			System.exit(1);
		}
		
		while(true){
			try{
				Socket cs = Seversokc.accept();
				addConnecton(cs);
				Thread ct = new Thread(new	clientProc(cs));
				ct.start();
			}catch(IOException e){
				System.err.println(e);
			}
		}
	} 
	public static void addConnection(Socket s){
		if	(connections == null){
			connections	= new Vector();
		}
		connections.addElement(s);
	}
	public static void deleteConnection(Socket s){
		if	(connections != null){
			connections.removeElement(s);
		}
	}


	public static void sendAll(String s){
		if	(connections != null){
			for(Enumeration en = connections.elements(); en.hasMoreElements(); ){
				try{
					PrintWriter pw = new PrintWriter(((Socket) en.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				}catch(IOException e){
				}
			}
		}
		System.out.println(s);
	} 
} 

class clienProc implements Runnable{
	Socket sock;
	BufferedReader inBR;
	PrintWriter outPW;
	String name = null;
	ChatServer chserv = null;

	public clienProc(Socket s) throws IOException{
		sock = s;
		inBR = new BufferedReader(new InputStreamReader(sock.gerInputStream()));
	}
	public void run(){
		try{
			while(neme == null){
				outPW.print("お名前は？：");
				outPW.flush();
				name = inBR.readLine();
			}
			String line = inBR.readLine();
			while(!"quit".equals(line)){
				ChatServer.deleteConnection(sock);
				sock.close();
			}
			ChatServer.deleteConnection(sock);
			sock.close();
		}catch(IOException e){
			try{
				sock.close();
			}catch(IOException e2){
		}
		}
	}
}
	
