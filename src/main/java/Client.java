import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

class Client {
    public static void main(String[] args) {
        Socket server = null;
        Scanner in;
        PrintWriter out;
        ArrayList<Double> liste; //Liste, die die Temperaturdaten speichert

        // Nach jedem Durch kann ein neues Datum eingegeben werden. Eingabe x beendet das Programm.
        while (true) {
            String eingabe = benutzerAbfragen();

            try {
                server = new Socket("localhost", 3141);
                in = new Scanner(server.getInputStream());
                out = new PrintWriter(server.getOutputStream(), true);

                // Anfrage an Server senden
                out.println(eingabe);


                // Empfange die Antwort und fuelle die Liste mit den Temperaturdaten oder gib Error aus
                boolean error = false;
                liste = new ArrayList<Double>();
                while (in.hasNext()) {
                    String zeile = in.nextLine();
                    if (!zeile.contains("ERROR"))
                        liste.add(Double.parseDouble(zeile));
                    else
                        error = true;
                    System.out.println(zeile);
                }

                // Liste auf Konsole ausgeben, wenn es keinen Fehler gab
                if (!error) {
                    System.out.println("Temperaturen fuer den " + eingabe);
                    for (int i = 0; i < liste.size(); i++) {
                        System.out.print(i + ":00 Uhr ");
                        System.out.println(liste.get(i) + " C");
                    }
                    System.out.println("Tiefstwert: " + berechneMin(liste) +
                            "    Hoechstwert: " + berechneMax(liste) + "    Schnitt: " +
                            berechneSchnitt(liste) + "\n\n"
                    );
                }


            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (server != null) {
                    try {
                        server.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Berechnet den durchnittlichen Wert einer uebergebenen Double ArrayList
     *
     * @param liste ArrayList mit Double werten
     * @return den durchschnittlichen Wert gerundet auf zwei Kommastellen
     */
    private static double berechneSchnitt(ArrayList<Double> liste) {
        double schnitt = 0.0;
        for (double wert : liste) {
            schnitt += wert;
        }
        schnitt /= liste.size();
        return ((int) schnitt + ((Math.round(Math.pow(10, 2) * (schnitt - (int) schnitt))) / (Math.pow(10, 2))));
    }

    /**
     * Berechnet den groessten Wert einer uebergebenen Double ArrayList
     *
     * @param liste ArrayList mit Double werten
     * @return den groessten Wert
     */
    private static double berechneMax(ArrayList<Double> liste) {
        double max = Double.MIN_VALUE;
        for (double wert : liste) {
            if (wert > max)
                max = wert;
        }
        return max;
    }

    /**
     * Berechnet den kleinsten Wert einer uebergebenen Double ArrayList
     *
     * @param liste ArrayList mit Double werten
     * @return den kleinsten Wert
     */
    private static double berechneMin(ArrayList<Double> liste) {
        double min = Double.MAX_VALUE;
        for (double wert : liste) {
            if (wert < min)
                min = wert;
        }
        return min;
    }

    /**
     * Fragt den Benutzer nach einem Datum. Mit einem x kann der Benutzer das Programm beenden
     *
     * @return den String, den der Benutzer eingegeben hat
     */
    private static String benutzerAbfragen() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Datum eingeben oder x zum beenden: ");
        String eingabe = sc.next();

        // Bei x Programm beenden
        if (eingabe.equals("x")) {
            System.out.println("Programm wird beendet...");
            System.exit(0);
        }
        return eingabe;
    }
}