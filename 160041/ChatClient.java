// �ȈՒʐM�v���O�����B�X�g���[���̓ǂݏ������J��Ԃ��B
// �������@�F java  client  �z�X�g��  �|�[�g�ԍ�
// T1.java

import	java.net.*;
import	java.io.*;

public	class	 ChatClient
{
	// main()���\�b�h
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
	} // main()���\�b�h�̏I���B
} // ChatClient�N���X�̏I���B



class	T1
{
	protected	Socket	sock;
	public	OutputStream	outST;
	public	BufferedInputStream	inBIS;


	// openConnection()���\�b�h
	public	void	openConnection(String	host,  int	port)	throws IOException, UnknownHostException
	{
		sock	=	new	Socket(host, port);
		outST = sock.getOutputStream();
		inBIS = new BufferedInputStream(sock.getInputStream());
	} // openConnection()���\�b�h�̏I���B


	// main_proc()���\�b�h
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
	} // main_proc()���\�b�h�̏I���B
} // T1�N���X�̏I���B













// StreamConnector�N���X
class	StreamConnector	implements	Runnable
{
	InputStream	src	=	null;
	OutputStream	dist	=	null;

	// �R���X�g���N�^
	public	StreamConnector(InputStream	in,		OutputStream	out)
	{
		src	=	in;
		dist	=	out;
	}


	// �X���b�h�����̖{��
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
} // StreamConnector�N���X�̏I���B
