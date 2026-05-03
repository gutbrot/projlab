package seged;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class TesztOsszehasonlito {

    /**
     * Két XML fájl determinisztikus összehasonlítása.
     * @param elvartFajl Az előre rögzített helyes kimenet
     * @param tenylegesFajl A teszt által most generált kimenet
     * @return true, ha a kettő tartalmilag megegyezik.
     */
    public static boolean fajlokatOsszehasonlit(String elvartFajl, String tenylegesFajl) {
        try {
            List<String> elvartSorok = fajlTisztitasa(Files.readAllLines(Paths.get(elvartFajl)));
            List<String> tenylegesSorok = fajlTisztitasa(Files.readAllLines(Paths.get(tenylegesFajl)));

            if (elvartSorok.size() != tenylegesSorok.size()) {
                System.out.println(">>> [TESZT BUKÁS] A fájlok sorszáma eltér! (Elvárt: " + elvartSorok.size() + ", Tényleges: " + tenylegesSorok.size() + ")");
                return false;
            }

            for (int i = 0; i < elvartSorok.size(); i++) {
                String elvart = elvartSorok.get(i);
                String tenyleges = tenylegesSorok.get(i);

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
        for (String sor : eredetiSorok) {
            String trimmelt = sor.trim();
            // Kihagyjuk az üres sorokat és a kommenteket (akár szabványos, akár a te egyedi '' formátumod)
            if (!trimmelt.isEmpty() && !trimmelt.startsWith("<!--") && !trimmelt.startsWith("''")) {
                tisztaSorok.add(trimmelt);
            }
        }
        return tisztaSorok;
    }
}