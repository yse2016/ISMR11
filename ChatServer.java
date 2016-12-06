import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer{
	static final int DEFAULT_PORT = 6000;
	static ServerSocket servsock;
	static Vector connections;

	// mainメソッド
	public static void main(String[] args) {

		int port = DEFAULT_PORT;
		if( args.length > 0 ){
			port = Integer.parseInt( args[0] );
		}

		// 1. ソケットを生成する(＝接続する)
		try{
			servsock = new ServerSocket( port );

		// 例外処理
		} catch( IOException e ){
			System.err.println(e);
			System.exit(1);
		}

		while( true ){
			try{
				Socket cs = servsock.accept();
				addConection(cs);
				Thread ct = new Thread( new clientProc(cs) );

			// 例外処理
			} catch( IOException e ){
				System.err.println(e);
			}
		}
	}

	// addConectionメソッド：新しい接続を追加する
	public static void addConection( Socket s ){

		if( connections == null ){
			connections = new Vector();
		}
		connections.addElement(s);
	}

	// deleteConectionメソッド：接続を削除する
	public static void deleteConection( Socket s ){

		if( connections != null ){
			connections.removeElement(s);
		}
	}

	// sendAllメソッド：各クライアントにメッセージを送る
	public static void sendAll( String s ){

		if( connections != null ){
			for( Enumeration en = connections.elements(); en.hasMoreElements(); ){
				try{
					PrintWriter pw = new PrintWriter( ((Socket) en.nextElement()).getOutputStream() );
					pw.println( s );
					pw.flush();

				// 例外処理
				} catch( IOException e ){
				}
			}
		}
		System.out.println( s );
	}
}





class clientProc implements Runnable{

	// データを準備
	Socket sock;
	BufferedReader inBR;
	PrintWriter outPW;
	String name = null;
	ChatServer chserv = null;

	// コンストラクタ
	public clientProc( Socket s ) throws IOException{
		sock  = s;
		inBR  = new BufferedReader( new InputStreamReader( sock.getInputStream() ));
		outPW = new PrintWriter( sock.getOutputStream() );
	}

	// スレッド処理の本体
	public void run(){
		try{
			while( name == null ){
				outPW.print("名前を入力してください： ");
				outPW.flush();
				name = inBR.readLine();
			}
			String line = inBR.readLine();
			while( !"quit".equals(line) ){
				ChatServer.sendAll( name + "> " + line );
				line = inBR.readLine();
			}
			ChatServer.deleteConection( sock );
			sock.close();

		// 例外処理
		} catch( IOException e ){
			try{
				sock.close();
			// 例外処理2
			} catch( IOException e2 ){
			}
		}
	}
}