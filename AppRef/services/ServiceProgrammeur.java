package services;

import bri.Service;
import bri.ServiceRegistry;
import bri.ValidationException;
import utilisateurs.Utilisateur;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;

public class ServiceProgrammeur implements Service {
    private Socket socketClient;
    private static LinkedList<Utilisateur> utilisateurs = new LinkedList<>();

    public ServiceProgrammeur(Socket socket){
        this.socketClient = socket;
        if(utilisateurs.size() == 0){
            utilisateurs.add(new Utilisateur("slim", "12052002", "ftp://localhost:2121"));
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(socketClient.getInputStream ( )));
            PrintWriter out = new PrintWriter (socketClient.getOutputStream ( ), true);
            out.println("Veuillez vous connecter :##Login :");
            String login = in.readLine();
            out.println("Password :");
            String password = in.readLine();
            Utilisateur utilisateur = null;
            for(Utilisateur u: utilisateurs){
                if(u.getLogin().equals(login) && u.getPassword().equals(password)){
                    utilisateur = u;
                }
            }
            out.println("Connexion réussie !##Que voulez-vous faire ?##1 - Fournir un nouveau service##2 - Mettre à jour un service##3 - Changer l'adresse de mon serveur ftp");
            int choix = Integer.parseInt(in.readLine());
            switch (choix){
                case 1:
                    out.println("Quel service voulez-vous ajouter ?");
                    String newService = in.readLine();
                    URL[] urls = new URL[1];
                    urls[0] = new URL(utilisateur.getFtpServeur() + "/");
                    URLClassLoader ucl = new URLClassLoader(urls);
                    utilisateur.setUcl(ucl);
                    Class<? extends Service> classe = (Class<? extends Service>) ucl.loadClass(utilisateur.getLogin() + "." + newService);
                    ServiceRegistry.addService(classe);
                    break;
                case 2:
                    out.println(ServiceRegistry.toStringue() + "Quel service voulez vous mettre à jour ?");
                    Class<? extends Service> classModif = ServiceRegistry.getServiceClass(Integer.parseInt(in.readLine()));
                    ServiceRegistry.getServicesClasses().remove(classModif);
                    //mettre à jour la classe
                    System.out.println(utilisateur);
                    URL[] urlsModif = utilisateur.getUcl().getURLs();
                    utilisateur.setUcl(null);
                    System.gc();
                    utilisateur.setUcl(new URLClassLoader(urlsModif));
                    classModif = (Class<? extends Service>) utilisateur.getUcl().loadClass(classModif.getName());
                    ServiceRegistry.addService(classModif);
                    break;
                case 3:
                    out.println("Quelle est la nouvelle url de votre serveur ftp ?");
                    String urlFtp = in.readLine();
                    utilisateur.setFtpServeur(urlFtp);
                    break;
            }
        } catch (IOException | ClassNotFoundException | ValidationException e) {
            e.printStackTrace();
        } /*catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }*/
        try {socketClient.close();} catch (IOException e2) {}
    }
}
