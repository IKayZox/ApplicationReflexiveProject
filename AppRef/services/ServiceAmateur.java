package services;

import bri.Service;
import bri.ServiceRegistry;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

public class ServiceAmateur implements Service {
    private Socket socketClient;

    public ServiceAmateur(Socket socket){
        this.socketClient = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader (new InputStreamReader(socketClient.getInputStream ( )));
            PrintWriter out = new PrintWriter (socketClient.getOutputStream ( ), true);
            out.println(ServiceRegistry.toStringue()+"Tapez le numéro de service désiré :");
            int choix = Integer.parseInt(in.readLine());
            Class<? extends Service> classe = ServiceRegistry.getServiceClass(choix);
            Constructor<? extends Service> constructeur = classe.getConstructor(socketClient.getClass());
            new Thread(constructeur.newInstance(socketClient)).run();
        } catch (IOException e) {
            //Fin du service
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        try {socketClient.close();} catch (IOException e2) {}
    }
}
