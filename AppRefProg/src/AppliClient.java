import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*ce client respecte le protocole BTTP (BretteText Transfer protocol)
    et ne contient aucune information
	liée au domaine traité par les services que le serveur va nous proposer

	mettre dans les arguments BTTP:host:port
*/

public class AppliClient {
    private static final int PORT = 5000;

    public static void main(String[] args) throws IOException {
        BufferedReader clavier = new BufferedReader(new InputStreamReader(System.in));
        Socket socket = null;
        try {
            socket = new Socket("localhost", PORT);
            BufferedReader sin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter sout = new PrintWriter(socket.getOutputStream(), true);
            // Informe l'utilisateur de la connection
            System.out.println("Connecté au serveur " + socket.getInetAddress() + ":" + socket.getPort());

            String line;
            // protocole BTTP jusqu'à saisie de "0" ou fermeture coté service
            do {// réception et affichage de la question provenant du service
                line = sin.readLine();
                if (line == null) break; // fermeture par le service
                System.out.println(nettoyerString(line));
                // prompt d'invite à la saisie
                System.out.print("->");
                line = clavier.readLine();
                if (line.equals("0")) break; // fermeture par le client
                // envoie au service de la réponse saisie au clavier
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

