package skeleton;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import jatekos.Jatekter;
import terkep.Terkep;
import seged.TesztOsszehasonlito;

/**
 * A TesztKornyezet osztály végzi az automatizált tesztesetek futtatását.
 * Kicseréli a standard bemenetet a teszt fájlok tartalmára, majd ellenőrzi a kimenetet.
 */
public class TesztKornyezet {

    /**
     * Univerzális tesztfuttató metódus, amely automatizálja a folyamatot.
     */
    public static void futtatTeszt(int tesztSzam) {
        // Elérési utak beállítása a képen látható mappa struktúra alapján
        String bemenetFajl = "tesztek/teszt" + tesztSzam + ".txt";
        String elvartFajl = "Elvart/teszt" + tesztSzam + "_elvart.xml";
        
        // A MentesKezelo a Betoltes/ mappába ment és hozzáadja a .xml kiterjesztést
        String kimenetFajlNev = "teszt" + tesztSzam + "_kimenet"; 
        String tenylegesKimenetUt = "Betoltes/" + kimenetFajlNev + ".xml";

        // Térkép kiválasztása a teszt sorszáma alapján (22-es és 48-as teszthez egyedi)
        String terkepFajl = (tesztSzam == 22 || tesztSzam == 48) ? "teszt_terkep_penz.xml" : "teszt_terkep.xml";

        Console.print("\n=========================================");
        Console.print(">>> TESZT " + tesztSzam + " FUTTATÁSA...");
        Console.print(">>> Használt alap térkép: " + terkepFajl);
        Console.print("=========================================");

        // Eredeti System.in kimentése, hogy a teszt után a menü továbbra is működjön
        InputStream eredetiIn = System.in;

        try {
            // 1. Bemeneti fájl ellenőrzése
            File bemenet = new File(bemenetFajl);
            if (!bemenet.exists()) {
                Console.print(">>> [HIBA] Nem található a bemeneti fájl: " + bemenetFajl);
                return;
            }

            // 2. Bemenet beolvasása és "kilepes" parancs hozzáfűzése a végtelen ciklus megtöréséhez
            String bemenetTartalom = new String(Files.readAllBytes(Paths.get(bemenetFajl))) + "\nkilepes\n";
            ByteArrayInputStream bais = new ByteArrayInputStream(bemenetTartalom.getBytes());
            System.setIn(bais);

            // 3. Játéktér példányosítása (ITT kapja meg az új System.in-t a Scanner-e)
            Jatekter jatekter = new Jatekter(new Terkep());

            // 4. A kiválasztott teszt térkép betöltése a Betoltes/ mappából
            boolean betoltve = jatekter.betoltes(terkepFajl);
            if (!betoltve) {
                Console.print(">>> [HIBA] " + terkepFajl + " betöltése sikertelen! Ellenőrizd a Betoltes/ mappát.");
                return;
            }

            // Globális térkép statikus beállítása a Hókotróknak
            jarmu.Hokotro.setGlobalTerkep(jatekter.getTerkep());

            // 5. Parancsok futtatása a txt fájlból
            Console.print(">>> [FUTTATÁS] Parancsok végrehajtása a(z) " + bemenetFajl + " alapján...");
            jatekter.startCommandLoop(jatekter.getJatekosok());

            // 6. Futás utáni állapot kimentése
            Console.print(">>> [MENTÉS] Állapot mentése ide: " + tenylegesKimenetUt);
            jatekter.mentes(kimenetFajlNev);

            // 7. Tényleges és elvárt kimenet összehasonlítása
            File elvart = new File(elvartFajl);
            if (!elvart.exists()) {
                Console.print(">>> [FIGYELMEZTETÉS] Nincs elvárt kimenet fájl: " + elvartFajl + " - Csak a kimenet lett legenerálva.");
            } else {
                TesztOsszehasonlito.fajlokatOsszehasonlit(elvartFajl, tenylegesKimenetUt);
            }

        } catch (Exception e) {
            Console.print(">>> [KIVÉTEL] Hiba a teszt futtatása közben: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Visszaállítjuk az eredeti System.in-t
            System.setIn(eredetiIn);
        }
    }
}