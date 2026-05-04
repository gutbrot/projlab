package seged;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TesztOsszehasonlito {

    /**
     * Két XML fájl determinisztikus összehasonlítása
     */
    public static boolean fajlokatOsszehasonlit(String elvartFajl, String tenylegesFajl) {
        try {
            //Beolvassuk a fájlokat és megtisztítjuk őket a szóközöktől, üres soroktól és kommentektől
            List<String> elvartSorok = fajlTisztitasa(Files.readAllLines(Paths.get(elvartFajl)));
            List<String> tenylegesSorok = fajlTisztitasa(Files.readAllLines(Paths.get(tenylegesFajl)));

            //Először ellenőrizzük a sorok számát
            if (elvartSorok.size() != tenylegesSorok.size()) {
                System.out.println(">>> [TESZT BUKÁS] A fájlok sorszáma eltér! (Elvárt: " + elvartSorok.size() + ", Tényleges: " + tenylegesSorok.size() + ")");
                return false;
            }

            //Ezután soronként összehasonlítjuk a fájlokat
            for (int i = 0; i < elvartSorok.size(); i++) {
                String elvart = elvartSorok.get(i);
                String tenyleges = tenylegesSorok.get(i);

                //Ha bármelyik sor eltér, akkor a teszt bukik
                if (!elvart.equals(tenyleges)) {
                    System.out.println(">>> [TESZT BUKÁS] Eltérés az értékes adatok " + (i + 1) + ". sorában!");
                    System.out.println("    [-] Elvárt:    " + elvart);
                    System.out.println("    [+] Tényleges: " + tenyleges);
                    return false;
                }
            }

            System.out.println(">>> [TESZT SIKER] A két XML fájl logikailag és tartalmilag is megegyezik.");
            return true;
        } catch (Exception e) {
            System.out.println(">>> [TESZT HIBA] Nem sikerült beolvasni a fájlokat: " + e.getMessage());
            return false;
        }
    }

    /**
     * Segédfüggvény, amely kiszedi a szóközöket, az üres sorokat és a kommenteket
     */
    private static List<String> fajlTisztitasa(List<String> eredetiSorok) {
        List<String> tisztaSorok = new ArrayList<>();
        //Végigmegyünk az összes soron, és csak a releváns adatokat tartjuk meg
        for (String sor : eredetiSorok) {
            String trimmelt = sor.trim();
            //Kihagyjuk az üres sorokat és a kommenteket
            if (!trimmelt.isEmpty() && !trimmelt.startsWith("<!--") && !trimmelt.startsWith("''")) {
                tisztaSorok.add(trimmelt);
            }
        }
        //Visszaadjuk a megtisztított sorokat
        return tisztaSorok;
    }
}