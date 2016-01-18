
public class Client {

	public static void main(String[] args)
	{
			//Pour l'instant on récupère n via la saisie clavier mais il faudra le passer en argument à la fin
			System.out.println("Entrez la valeur de n:");
			Scanner clavier = new Scanner(System.in);
			int n = clavier.nextInt();

			//On crée la connexion
			int port = 60000;
			INetAdress serverAdress = INetAdress.getLocalHost();
			Socket socket = new Socket(serverAdress, port);

			//On crée les flux d'entrée et de sortie
			PrintStream output = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			Scanner input = new Scanner(inputStream);

			//On  envoie la valeur de n au serveur et on attend la réponse

			while(!input.hasNext());
				//TODO rechercher l'équivalent de sleep en java

			int result = input.nextInt();

			System.out.println("Résultat");
			System.out.print(result);

	}

}
