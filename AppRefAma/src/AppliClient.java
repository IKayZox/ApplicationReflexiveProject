import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AppliClient {
	private static final int PORT = 3000;
	
	public static void main(String[] args) throws IOException {
		BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
		Socket socket = null;
		try {
			socket = new Socket("localhost", PORT);
			BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);
			// Informe l'utilisateur de la connection
			System.out.println("Connect� au serveur " + socket.getInetAddress() + ":" + socket.getPort());

			String line;
			// protocole BTTP jusqu'� saisie de "0" ou fermeture cot� service
			do {// r�ception et affichage de la question provenant du service
				line = sin.readLine();
				if (line == null) break; // fermeture par le service
				System.out.println(nettoyerString(line));
				// prompt d'invite � la saisie
				System.out.print("->");
				line = clavier.readLine();
				if (line.equals("0")) break; // fermeture par le client
				// envoie au service de la r�ponse saisie au clavier
				sout.println(line);
			} while (true);
			socket.close();
		} catch (IOException e) {
			System.err.println("Fin du service");
		}
		// Refermer dans tous les cas la socket
		try {
			if (socket != null) socket.close();
		} catch (IOException e2) {
			;
		}
	}

	public static String nettoyerString(String s){
		return s.replace("##", "\n");
	}
}
