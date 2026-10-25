package clientPackage;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;
public class Client {

    public static void main(String[] args) {
        try {
            System.out.println("Client prêt à se connecter...");
            Socket socket = new Socket("localhost", 1234);
            System.out.println(" Connecté au serveur !");

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Scanner sc = new Scanner(System.in);

            while (true) {
                System.out.print("Donnez une opération (ex : 25 * 4) ou 'quit' : ");
                String saisie = sc.nextLine().trim();
                if (saisie.equalsIgnoreCase("quit")) break;

                String[] parts = saisie.split("\\s+");
                if (parts.length != 3) {
                    System.out.println(" Syntaxe invalide !");
                    continue;
                }

                double op1 = Double.parseDouble(parts[0]);
                String operateur = parts[1];
                double op2 = Double.parseDouble(parts[2]);


                Operation operation = new Operation(op1, operateur, op2);
                oos.writeObject(operation);
                oos.flush();


                String resultat = (String) ois.readObject();
                System.out.println("Résultat reçu : " + resultat);
            }

            oos.writeObject("quit");
            System.out.println("Fin de la communication.");
            socket.close();
            sc.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Erreur côté client : " + e.getMessage());
        }
    }
}


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


