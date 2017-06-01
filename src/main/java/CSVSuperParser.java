import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;

/**
 * Greift auf die Datei mit den Temperaturen zu und macht die Daten zugaenglich.
 */
public class CSVSuperParser {
    private String filename;

    public CSVSuperParser(String filename) {
        this.filename = filename;
    }


    /**
     * Erzeugt eine Liste mit Temperaturen eines Tages
     *
     * @param datum Datum, von dem die Temeraturen gegeben werden soll
     * @return ArrayList mit den Temperaturen des gewuenschten Tages. Gibt null zurueck, wenn der Tag nicht existiert.
     */
    public ArrayList<String> gibTemperaturen(String datum) {
        Reader in;
        ArrayList<String> liste = new ArrayList<String>();

        try {
            in = new FileReader(filename);
            for (CSVRecord rec : CSVFormat.DEFAULT.withDelimiter(';').parse(in)) {
                if (rec.get(0).equals(datum)) {
                    liste.add(rec.get(2));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return liste;
    }


}
