import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ServerFactorial
{

	public static void main(String[] args)
	{
		try
		{
			ServerSocket server = new ServerSocket(60000);
			
			System.out.println("ServerSocket créé, en attente de connexion");
			Socket socket = server.accept();
			
			System.out.println("Connexion créée, récupération des buffers");
			Scanner input = new Scanner(socket.getInputStream());
			PrintStream output = new PrintStream(socket.getOutputStream(), true);
			
			
			while(!input.hasNext())
			{
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			int n = input.nextInt();
			System.out.println(n);
			output.print(n);
			
			input.close();
			server.close();
			System.out.println("Fin");
			
		}
		
		catch(IOException exception)
		{
			exception.printStackTrace();
		}
	}

}
