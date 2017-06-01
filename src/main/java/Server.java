import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {

    private static void handleConnection(Socket client) throws IOException {
        Scanner in = new Scanner(client.getInputStream());
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);

        // Eingang der Anfrege
        String anfrage = in.nextLine();


        CSVSuperParser parser = new CSVSuperParser("temperaturen.csv");
        ArrayList<String> liste; // speichert die Temperaturwerte des gewuenschten Tages
        liste = parser.gibTemperaturen(anfrage);

        if (!validiereAnfrage(anfrage)) {
            out.println("ERROR: Anfrage hat falsches Format oder Tag existiert nicht");
        }
        // Liste leer -> Tag nicht in DB
        else if (liste.size() <= 0) {
            out.println("ERROR: Keine Daten zu diesem Tag vorhanden");
        }
        // Liste mit mehr oder weniger als 24 Eintraegen -> Daten fehlerhaft
        else if (liste.size() < 24 || liste.size() > 24) {
            out.println("ERROR: Daten zu diesem Tag fehlerhaft");
        } else {
            for (String temp : liste) {
                out.println(temp);
            }
        }

    }

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(3141);

        while (true) {
            Socket client = null;

            try {
                client = server.accept();
                handleConnection(client);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (client != null) {
                    try {
                        client.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }



    /**
     * Prueft einen String auf korrektheit eines Datums im Format: dd.mm.yyyy
     * @param anfrage String, der geprueft werden soll
     * @return true, wenn das Format korrekt ist, sonst false
     */
    private static boolean validiereAnfrage(String anfrage) {
        String regex = "(0[1-9]|[12][0-9]|3[01])[ \\.](0[1-9]|1[012])[ \\.](19|20)\\d\\d";
        return anfrage.matches(regex);
    }

}