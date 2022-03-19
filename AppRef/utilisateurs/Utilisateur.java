package utilisateurs;

import java.net.URLClassLoader;

public class Utilisateur {
    private String login;
    private String password;
    private String ftpServeur;
    private URLClassLoader ucl;

    public Utilisateur(String login, String password, String ftpServeur){
        this.login = login;
        this.password = password;
        this.ftpServeur = ftpServeur;
    }

    public void setFtpServeur(String ftpServeur) {
        this.ftpServeur = ftpServeur;
    }

    public Utilisateur(String login, String password){
        this.login = login;
        this.password = password;
        this.ftpServeur = null;
    }

    public String getLogin(){
        return login;
    }

    public String getPassword(){
        return password;
    }

    public String getFtpServeur(){
        return ftpServeur;
    }

    public URLClassLoader getUcl() {
        return ucl;
    }

    public void setUcl(URLClassLoader ucl) {
        this.ucl = ucl;
    }
}
