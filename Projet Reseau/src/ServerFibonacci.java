import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.Scanner;

public class ServerFibonacci extends Thread
{
	//Client part
	class ClientThread extends Thread
	{
		private Scanner input;
		private PrintStream output;
		
		ClientThread(Socket socket)
		{
			try
			{
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
			
			//Si n = 0 ou 1, on renvoie n
			if(n == 0 || n == 1)
				output.println(n);
			
			//Sinon, si la valeur se trouve dans le cache on renvoit la valeur présente dans le cache
			else if(ServerFibonacci.cache.containsKey(n))
				output.println(ServerFibonacci.cache.get(n));
			
			//Sinon on calcul la valeur et on l'ajoute au cache
			else
			{
				try
				{
					InetAddress addressOne = InetAddress.getLocalHost(), addressTwo = otherServerAddress;
					int portOne = serverSocket.getLocalPort(), portTwo = otherServerPort;
					
					if(auxiliary)
					{
						addressOne = otherServerAddress;
						portOne = otherServerPort;
						addressTwo = InetAddress.getLocalHost();
						portTwo = serverSocket.getLocalPort();
					}
					
					//Le client qui calcule n-1 se connecte au serveur principal
					ClientFibonacci clientOne = new ClientFibonacci(addressOne, portOne);
					//Le client qui calcule n-2 se connecte au serveur auxiliaire
					ClientFibonacci clientTwo = new ClientFibonacci(addressTwo, portTwo);
					
					int result = clientOne.askForFibonacci(n-1) + clientTwo.askForFibonacci(n-2);
					ServerFibonacci.cache.put(n, result);
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
	private boolean auxiliary;
	private InetAddress otherServerAddress;
	int otherServerPort;
	
	public ServerFibonacci(int port, boolean auxiliary, InetAddress otherServerAddress, int otherServerPort)
	{
		try
		{
			serverSocket = new ServerSocket(port);
			this.auxiliary = auxiliary;
			this.otherServerAddress = otherServerAddress;
			this.otherServerPort = otherServerPort;
		}
		
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run()
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
	

	
	//Main part
	private static Hashtable<Integer, Integer> cache = new Hashtable<Integer, Integer>();
	
	public static void main(String[] args)
	{
		if(args.length != 2)
		{
			System.out.println("Number of argument is invalid");
			System.out.println("Correct syntax: java ServerFibonacci.class <port1> <port2>");
		}
		
		else
		{
			int portOne = Integer.parseUnsignedInt(args[0]);
			int portTwo = Integer.parseInt(args[1]);
			
			try
			{
				InetAddress localhost = InetAddress.getLocalHost();
				ServerFibonacci serverOne = new ServerFibonacci(portOne, false, localhost, portTwo);
				serverOne.start();
				
				ServerFibonacci serverTwo = new ServerFibonacci(portTwo, true, localhost, portOne);
				serverTwo.start();
			}
			catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
