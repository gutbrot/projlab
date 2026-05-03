package skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jarmu.Hokotro;
import jatekos.Buszvezeto;
import jatekos.Takarito;
import jatekos.Jatekos;
import jatekos.Jatekter;
import terkep.Terkep;

public class Skeleton {

    // Ideiglenes listák a játékosok és járművek tárolására a játék mód setup-jában.
    // Ezek a listák a játék indítása után kiürülnek, és a játékosok és járművek a Jatekterben lesznek tárolva.
    private static List<Takarito> tempTakaritok = new ArrayList<>();
    private static List<Buszvezeto> tempBuszvezetok = new ArrayList<>();
    
    // A standard Java beolvasó eszköz
    private static Scanner scanner = new Scanner(System.in);

    // A fő metódus, amely elindítja a programot és kezeli a főmenüt.
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   HOKOTRO SZIMULATOR PROTOTIPUS    ");
        System.out.println("====================================");

        while (true) {
            System.out.println("\nFOMENU:");
            System.out.println("1. Jatek mod");
            System.out.println("2. Teszt mod");
            System.out.println("3. Kilepes");

            // Beolvassuk a játékos választását
            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            // A játékos választása alapján meghívjuk a megfelelő metódust.
            switch (valasztas.trim()) {

                // Az első opció a játék mód setup-ját indítja el, ahol a játékosok és járművek hozzáadására van lehetőség.
                case "1":
                    jatekModMenu();
                    break;
                
                // A második opció egy teszt módot indít el, amelyben majd az XML fájlok beolvasása és kiírása lesz a fő fókusz.
                case "2":
                    tesztMod();
                    break;
                
                // A harmadik opció kilép a programból.
                case "3":
                    System.out.println(">>> Kilepes...");
                    System.exit(0);
                    break;
                
                // Ha a játékos érvénytelen opciót választ, akkor egy hibaüzenetet kap.
                default:
                    System.out.println(">>> Ervenytelen opcio!");
            }
        }
    }

    // Ez a menu felelős a játékosok és járművek hozzáadásáért, valamint a játék indításáért.
    private static void jatekModMenu() {
        
        while (true) {
            System.out.println("\n--- JATEK MOD SETUP ---");
            System.out.println("Eddig hozzaadva: " + tempTakaritok.size() + " Takarito, " + tempBuszvezetok.size() + " Buszsofor");
            System.out.println("1. Uj takarito hozzaadasa");
            System.out.println("2. Uj buszsofor hozzaadasa");
            System.out.println("3. Jatek inditasa");
            System.out.println("4. Vissza");

            // Beolvassuk a játékos választását
            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            // A játékos választása alapján meghívjuk a megfelelő műveletet.
            switch (valasztas.trim()) {
                
                // Az első opció egy új takarító hozzáadását teszi lehetővé, amelyhez egy hókotró is tartozik.
                case "1":
                    // Bekérjük a takarító nevét.
                    System.out.print("Add meg a takarito nevet (ekezet nelkul, egybe): ");
                    String tNev = scanner.nextLine();
                    
                    // Létrehozzuk a takarító objektumot, és beállítjuk a nevét.
                    Takarito takarito = new Takarito(3);
                    takarito.setNev(tNev);
                    tempTakaritok.add(takarito);

                    System.out.println(">>> Takarito (" + tNev + ") hozzaadva!");
                    break;

                // A második opció egy új buszsofőr hozzáadását teszi lehetővé.
                case "2":
                    // Bekérjük a buszsofőr nevét.
                    System.out.print("Add meg a buszsofor nevet (ekezet nelkul, egybe): ");
                    String bNev = scanner.nextLine();

                    // Létrehozzuk a buszsofőr objektumot, és beállítjuk a nevét, majd hozzáadjuk a tempBuszvezetok listához.
                    Buszvezeto buszvezeto = new Buszvezeto(3);
                    buszvezeto.setNev(bNev);
                    tempBuszvezetok.add(buszvezeto);

                    // Kiírjuk a konzolra, hogy egy új buszsofőr létre lett hozva.
                    System.out.println(">>> Buszsofor (" + bNev + ") hozzaadva!");
                    break;
                
                // A harmadik opció elindítja a játékot, de csak akkor, ha legalább egy takarító és egy buszsofőr hozzá lett adva.
                case "3":

                    // Ellenőrizzük, hogy van-e legalább egy takarító és egy buszsofőr a listákban.
                    // Ha nincs, akkor egy hibaüzenetet írunk ki, és nem indítjuk el a játékot.
                    if (tempTakaritok.isEmpty() || tempBuszvezetok.isEmpty()) {
                        System.out.println(">>> Hiba: A jatek nem tud elindulni, amig nincs legalabb 1 Takarito es 1 Buszsofor!");
                    } 
                    // Ha van legalább egy takarító és egy buszsofőr, akkor elindítjuk a játékot.
                    else {
                        startJatekLoop();
                        return;
                    }
                    break;

                // A negyedik opció visszalép a főmenüre.
                case "4":
                    return;

                // Ha a játékos érvénytelen opciót választ, akkor egy hibaüzenetet kap.
                default:
                    System.out.println(">>> Ervenytelen opcio!");
            }
        }
    }

    // Ez a metódus felelős a játék fő ciklusának elindításáért, ahol a játékosok és járművek inicializálása után a játéktér létrehozása és a parancsok kezelése történik.
    private static void startJatekLoop() {
        System.out.println("\n>>> Jatek inicializalasa...");
        
        // Létrehozzuk a játéktérhez szükséges térképet, majd a játéktér objektumot.
        Terkep terkep = new Terkep();
        Jatekter jatekter = new Jatekter(terkep);

        // Ezután egy sorrendet hozunk létre a játékosok számára, amelyben felváltva helyezkednek el a takarítók és buszsofőrök.
        List<Jatekos> sorrend = new ArrayList<>();
        int tIndex = 0;
        int bIndex = 0;

        // Amíg van még takarító vagy buszsofőr a temp listákban, addig hozzáadjuk őket a sorrendhez és a játéktérhez.
        while (tIndex < tempTakaritok.size() || bIndex < tempBuszvezetok.size()) {
            
            // Először hozzáadunk egy takarítót, ha van még.
            if (tIndex < tempTakaritok.size()) {

                // Kiválasztjuk a következő takarítót a temp listából.
                Takarito takarito = tempTakaritok.get(tIndex);
                
                // A takarítót hozzáadjuk a sorrendhez.
                sorrend.add(takarito);
                // A takarítót hozzáadjuk a játéktérhez.
                jatekter.hozzaadJatekos(takarito);
                // Mivel minden takarítóhoz tartozik egy hókotró jármű, ezért a takarítóhoz tartozó hókotrókat is hozzáadjuk a játéktérhez.
                for (Hokotro h : takarito.getIranyitottHokotrok()) {
                    jatekter.hozzaadJarmu(h);
                }
                // Növeljük a takarító indexét, hogy a következő iterációban a következő takarítót adjuk hozzá.
                tIndex++;
            }
            // Ezután hozzáadunk egy buszsofőrt, ha van még.
            if (bIndex < tempBuszvezetok.size()) { 
                // Kiválasztjuk a következő buszsofőrt a temp listából.
                sorrend.add(tempBuszvezetok.get(bIndex));
                // A buszsofőrt hozzáadjuk a játéktérhez.
                jatekter.hozzaadJatekos(tempBuszvezetok.get(bIndex));
                // Növeljük a buszsofőr indexét, hogy a következő iterációban a következő buszsofőrt adjuk hozzá.
                bIndex++;
            }
        }

        // Miután a játékosok és járművek hozzá lettek adva a játéktérhez, kiürítjük a temp listákat, mivel ezekre már nincs szükség.
        tempTakaritok.clear();
        tempBuszvezetok.clear();

        // Végül elindítjuk a játéktér parancskezelő ciklusát a létrehozott sorrenddel.
        jatekter.startCommandLoop(sorrend);
    }

    // Ez a metódus egy teszt módot indít el
    private static void tesztMod() {
        System.out.println(">>> Teszt mod inicializalasa...");
        System.out.println(">>> (Ide jon majd az XML fajlok beolvasasa es kiirasa a kesobbi fazisban)");
    }
}