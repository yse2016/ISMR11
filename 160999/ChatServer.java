// ChatServer.java

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {
	static final int DEFAULT_PORT = 6000;
	static ServerSocket servsock;
	static Vector connections;

	// main method
	public static void main(String[] args ){
		int port = DEFAULT_PORT;
		if(args.length > 0) {
			port = Integer.parseInt(args[0]);
		}

		// new server socket
		try {
			servsock = new ServerSocket(port);
		} catch(IOException e) {
			System.err.println(e);
			System.exit(1);
		}

		// accept connection
		while(true) {
			try {
				Socket cs = servsock.accept();
				addConnection(cs);
				Thread ct = new Thread(new clientProc(cs));
				ct.start();
			} catch(IOException e) {
				System.err.println(e);
			}
		}
	}

	// method: addConnection()
	public static void addConnection(Socket s) {
		if(connections == null) {
			connections = new Vector();
		}
		connections.addElement(s);
	}

	// method: deleteConnection()
	public static void deleteConnection(Socket s) {
		if(connections != null) {
			connections.removeElement(s);
		}
	}

	// method: sendAll()
	public static void sendAll(String s){
		if(connections != null) {
			for(Enumeration en = connections.elements();  en.hasMoreElements();) {
				try {
					PrintWriter pw = new PrintWriter(((Socket)en.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				} catch(IOException e) {
				}
			}
		}
		System.out.println(s);
	}
}


// class: clientProc
class clientProc implements Runnable {
	// 変数・データをつくる
	Socket sock;
	BufferedReader inBr;
	PrintWriter outPw;
	String name = null;
	ChatServer chserv = null;

	// constructor
	public clientProc(Socket s) throws IOException {
		sock = s;
		inBr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
		outPw = new PrintWriter(sock.getOutputStream());
	}

	// proc for Thread
	public void run() {
		try {
			while( name == null) {
				outPw.print("お名前は？:   ");
				outPw.flush();
				name = inBr.readLine();
			}
			String line = inBr.readLine();
			while(!"quit".equals(line)) {
				ChatServer.sendAll(name + ">  " + line);
				line = inBr.readLine();			
			}
		} catch(IOException e) {
			try {
				sock.close();
			} catch(IOException e2) {
			}
		}
	}

}