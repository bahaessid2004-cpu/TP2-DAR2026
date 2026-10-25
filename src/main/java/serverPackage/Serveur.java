package serveurPackage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Serveur {


    public static void main(String[] args) {
        try {
            // Création du serveur sur le port 1234
            ServerSocket serverSocket = new ServerSocket(1234);
            System.out.println("Serveur en attente de connexion...");

            // Attente de la connexion du client
            Socket socket = serverSocket.accept();
            System.out.println(" Un client est connecté !");

            // Création des flux d'objets
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

            Object obj = ois.readObject();
            while (obj != null && !(obj instanceof String && obj.equals("quit"))) {
                if (obj instanceof Operation) {
                    Operation op = (Operation) obj;
                    System.out.println("Opération reçue : " + op);

                    // Calcul du résultat
                    String resultat = calculer(op.getOperande1(), op.getOperateur(), op.getOperande2());

                    // Envoi du résultat au client
                    oos.writeObject(resultat);
                    oos.flush();
                    System.out.println("Résultat envoyé : " + resultat);
                }
                obj = ois.readObject();
            }

            System.out.println("Fin de la communication.");
            socket.close();
            serverSocket.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté serveur : " + e.getMessage());
        }
    }

    // Méthode de calcul
    static String calculer(double a, String op, double b) {
        double r;
        switch (op) {
            case "+": r = a + b; break;
            case "-": r = a - b; break;
            case "*": r = a * b; break;
            case "/":
                if (b == 0) return "Erreur : division par zéro";
                r = a / b;
                break;
            default: return "Erreur : opérateur inconnu";
        }
        return a + " " + op + " " + b + " = " + r;
    }
}

// Classe interne représentant l’objet échangé
class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    private double operande1;
    private double operande2;
    private String operateur;

    public Operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    public double getOperande1() { return operande1; }
    public double getOperande2() { return operande2; }
    public String getOperateur() { return operateur; }

    @Override
    public String toString() {
        return operande1 + " " + operateur + " " + operande2;
    }
}


