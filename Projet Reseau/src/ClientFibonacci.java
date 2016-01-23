import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientFibonacci
{
	
	private Socket socket;
	private PrintStream output;
	private Scanner input;
	
	ClientFibonacci(InetAddress host, int port) throws SocketException, UnknownHostException, IOException
	{
		socket = new Socket(host, port);
		output = new PrintStream(socket.getOutputStream(), true);
		input = new Scanner(socket.getInputStream());
	}
	
	public int askForFibonacci(int n)
	{		
		//On envoie n au serveur et on attend qu'il nous renvoie la réponse
		output.println(n);
		
		//Tant qu'on ne reçoit pas de réponse, on dort
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
		
		return input.nextInt();
	}

	public static void main(String[] args)
	{
		//Si on a pas le bon nombre d'argument, on affiche un message d'erreur
		if(args.length != 3)
		{
			System.out.println("Number of argument is invalid");
			System.out.println("Correct syntax: java ClientFibonacci.class <value> <adress> <port>");
		}
		
		//Si il y a le bon nombre de paramètre on crée un client, on l'envoie demander au serveur le résultat
		//et on l'affiche
		else
		{
			try
			{	
				ClientFibonacci client = new ClientFibonacci(InetAddress.getByName(args[1]), Integer.parseUnsignedInt(args[2]));				
				System.out.println(client.askForFibonacci(Integer.parseUnsignedInt(args[0])));
			}
			
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				System.out.println("Error!");
				System.out.println("Correct syntax: java ClientFibonacci.class <value> <adress> <port>");
				e.printStackTrace();
			}
			
			catch (SocketException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			catch (UnknownHostException e)
			{
				// TODO Auto-generated catch block
				System.out.println("Can't reach the host");
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
