import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Scanner;

public class ServerFactorial
{
	//Client part
	class ClientThread extends Thread
	{
		private Socket clientSocket;
		private Scanner input;
		private PrintStream output;
		
		ClientThread(Socket socket)
		{
			try
			{
				clientSocket = socket;
				input = new Scanner(socket.getInputStream());
				output = new PrintStream(socket.getOutputStream(), true);
			}
			
			catch(IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public void run()
		{
			while(!input.hasNext())
			{
				try
				{
					Thread.sleep(1000);
				}
				
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int n = input.nextInt();
			
			//Si n = 0 on renvoie 1
			if(n == 0)
				output.println(1);
			
			//Sinon, si la valeur se trouve dans le cache on revnoit la valeur présente dans le cache
			else if(ServerFactorial.cache.containsKey(n))
				output.println(ServerFactorial.cache.get(n));
			
			//Sinon on calcul la valeur et on l'ajoute au cache
			else
			{
				try
				{
					ClientFactorial clientFactice = new ClientFactorial(InetAddress.getLocalHost(), serverSocket.getLocalPort());
					int result = n * clientFactice.askForFactorial(n-1);
					ServerFactorial.cache.put(n, result);
					output.println(result);
				}
				
				catch (SocketException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				catch (UnknownHostException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//Server part	
	private ServerSocket serverSocket;
	private static Hashtable<Integer, Integer> cache = new Hashtable<Integer, Integer>();
	
	public ServerFactorial(int port)
	{
		try
		{
			serverSocket = new ServerSocket(port);
		}
		
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void run()
	{
		try
		{
			while(true)
			{
				Socket clientSocket = serverSocket.accept();				
				ClientThread newThread = new ClientThread(clientSocket);
				newThread.start();
			}		
		}
		
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		if(args.length != 1)
		{
			System.out.println("Nombre d'arguments invalide");
			System.out.println("Correct syntax: java ServerFactorial.class <port>");
		}
		
		else
		{
			ServerFactorial server = new ServerFactorial(Integer.parseUnsignedInt(args[0]));
			server.run();
		}
	}
}
