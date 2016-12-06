// 簡易通信プログラム。ストリームの読み書きを繰り返す。
// 実験方法： java  client  ホスト名  ポート番号
// T1.java

import	java.net.*;
import	java.io.*;

public	class	 ChatClient
{
	// main()メソッド
	public	static	void	main(String[]	args)
	{
		try	{
			T1	t	=	null;
			t	=	new	T1();
			t.openConnection(args[0],  Integer.parseInt(args[1]));
			t.main_proc();
		}	catch(Exception	e)	{
			e.printStackTrace();
			System.exit(1);
		}
	} // main()メソッドの終わり。
} // ChatClientクラスの終わり。



class	T1
{
	protected	Socket	sock;
	public	OutputStream	outST;
	public	BufferedInputStream	inBIS;


	// openConnection()メソッド
	public	void	openConnection(String	host,  int	port)	throws IOException, UnknownHostException
	{
		sock	=	new	Socket(host, port);
		outST = sock.getOutputStream();
		inBIS = new BufferedInputStream(sock.getInputStream());
	} // openConnection()メソッドの終わり。


	// main_proc()メソッド
	public	void	main_proc()	throws	IOException
	{
		try	{
			StreamConnector	stdin_to_socket = new	StreamConnector(System.in,  outST);
			StreamConnector socket_to_stdout = new	StreamConnector(inBIS, System.out);

			Thread	input_thread = new Thread(stdin_to_socket);
			Thread	output_thread = new Thread(socket_to_stdout);

			input_thread.start();
			output_thread.start();
		}	catch(Exception	e)	{
			System.err.print(e);
			System.exit(1);
		}
	} // main_proc()メソッドの終わり。
} // T1クラスの終わり。













// StreamConnectorクラス
class	StreamConnector	implements	Runnable
{
	InputStream	src	=	null;
	OutputStream	dist	=	null;

	// コンストラクタ
	public	StreamConnector(InputStream	in,		OutputStream	out)
	{
		src	=	in;
		dist	=	out;
	}


	// スレッド処理の本体
	public	void	run()
	{
		byte[]	buff	=	new	byte[1024];

		while(true)
		{
			try	{
				int	n	=	src.read(buff);
				if( n>0 )
				{
					dist.write(buff, 0, n);
				}
			}	catch(Exception	e)	{
				e.printStackTrace();
				System.err.print(e);
				System.exit(1);
			}
		}
	}
} // StreamConnectorクラスの終わり。
