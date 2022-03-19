package appli;

import java.io.IOException;
import java.net.URLClassLoader;
import java.util.Scanner;

import bri.ServeurBRi;
import services.ServiceAmateur;
import services.ServiceProgrammeur;

public class BRiLaunch {
	private final static int PORT_AMA = 3000;
	private final static int PORT_PROG = 5000;
	
	public static void main(String[] args) throws IOException {
		new Thread(new ServeurBRi(ServiceAmateur.class, PORT_AMA)).start();
		new Thread(new ServeurBRi(ServiceProgrammeur.class, PORT_PROG)).start();
	}
}
