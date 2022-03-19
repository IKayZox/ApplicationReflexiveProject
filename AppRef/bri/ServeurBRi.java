package bri;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.*;


public class ServeurBRi implements Runnable {
	private ServerSocket listen_socket;
	private Class<? extends Service> service;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	public ServeurBRi(Class<? extends Service> service, int port) throws IOException {
		listen_socket = new ServerSocket(port);
		this.service = service;
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un ServiceInversion, 
	// qui va la traiter.
	public void run() {
		while(true) {
			try {
				Constructor<? extends Service> constructeur = service.getConstructor(java.net.Socket.class);
				Service service = constructeur.newInstance(listen_socket.accept());
				new Thread(service).start();
			} catch (IOException e) {
				System.err.println("Problème sur le port d'écoute :" + e);
				break;
			} catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				break;
			}
		}
	}

	 // restituer les ressources --> finalize
	protected void finalize() throws Throwable {
		try {this.listen_socket.close();} catch (IOException e1) {}
	}

	// lancement du serveur
	public void lancer() {
		(new Thread(this)).start();		
	}
}
