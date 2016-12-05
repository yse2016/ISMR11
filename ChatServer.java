import	java.io.*;
import	java.net.*;
import	java.util.*;

public	class	ChatServer
{
	static	final	int	DEFAULT_PORT	=	6000;
	static	ServerSocket	servsock;
	static	Vector	connections;


	// main���\�b�h
	public	static	void	main(String[]	args)
	{
		int	port	=	DEFAULT_PORT;
		if(args.length	>	0)
		{
			port	=	Integer.parseInt(args[0]);
		}

		// �\�P�b�g�𐶐�����i���ڑ�����j�B
		try	{
			servsock	=	new	ServerSocket(port);
		}	catch	(IOException	e)	{
			System.err.println(e);
			System.exit(1);
		}

		while(true)
		{
			try	{
				Socket	cs	=	servsock.accept();
				addConnection(cs);
				Thread	ct	=	new	Thread(new	clientProc(cs));
				ct.start();
			} catch	(IOException	e)	{
				System.err.println(e);
			}
		}
	} // main���\�b�h�̏I���B


	// addConnection���\�b�h�F�V�����ڑ���ǉ�����B
	public	static	void	addConnection(Socket	s)
	{
		if	(connections	==	null)
		{
			connections	=	new	Vector();
		}
		connections.addElement(s);
	} // addConnection���\�b�h�̏I���B


	// deleteConnection���\�b�h�F�ڑ����폜����B
	public	static	void	deleteConnection(Socket	s)
	{
		if	(connections	!=	null)
		{
			connections.removeElement(s);
		}
	} // deleteConnection���\�b�h�̏I���B











	// sendAll���\�b�h�F�e�N���C�A���g�Ƀ��b�Z�[�W�𑗂�B
	public	static	void	sendAll(String	s)
	{
		if	(connections	!=	null)
		{
			for(Enumeration	en	=	connections.elements();		en.hasMoreElements();	)
			{
				try	{
					PrintWriter	pw	=	new	PrintWriter(((Socket) en.nextElement()).getOutputStream());
					pw.println(s);
					pw.flush();
				} catch(IOException	e)	{
				}
			}
		}
		System.out.println(s);
	} // sendAll���\�b�h�̏I���B
} // ChatServer�N���X�̏I���B




// ClientProc�N���X
class	clientProc	implements	Runnable
{
	Socket	sock;
	BufferedReader	inBR;
	PrintWriter	outPW;
	String	name	=	null;
	ChatServer	chserv	=	null;

	// �R���X�g���N�^
	public	clientProc(Socket	s)	throws	IOException
	{
		sock	=	s;
		inBR	=	new	BufferedReader(new	InputStreamReader(sock.getInputStream()));
		outPW	=	new	PrintWriter(sock.getOutputStream());
	}


	// �X���b�h�����̖{��
	public	void	run()	{
		try	{
			while	(name == null)
			{
				outPW.print("�����O�́H�F  ");
				outPW.flush();
				name	=	inBR.readLine();
			}
			String	line	=	inBR.readLine();
			while(!"quit".equals(line))
			{
				ChatServer.sendAll(name + ">  " + line);
				line	=	inBR.readLine();
			}
			ChatServer.deleteConnection(sock);
			sock.close();
		} catch (IOException	e) {
			try	{
				sock.close();
			} catch (IOException e2) {
			}
		}
	} // run�̏I���B
} // clientProc�N���X�̏I���B


