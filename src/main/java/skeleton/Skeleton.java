package skeleton;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import grafikus.GraphicsApp;
import jarmu.Hokotro;
import jatekos.Buszvezeto;
import jatekos.Takarito;
import jatekos.Jatekos;
import jatekos.Jatekter;
import terkep.Terkep;
import terkep.TerkepLoader;

public class Skeleton {

    //Osztályszintű változó a térkép tárolására, hogy minden metódus elérje
    private static Terkep aktualisTerkep;
    
    private static List<Takarito> tempTakaritok = new ArrayList<>();
    private static List<Buszvezeto> tempBuszvezetok = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    /*
    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   HOKOTRO SZIMULATOR PROTOTIPUS    ");
        System.out.println("====================================");

        //Főmenü ciklus
        while (true) {
            System.out.println("\nFOMENU:");
            System.out.println("1. Uj jatek (Csak terkep betoltes, manualis setup)");
            System.out.println("2. Jatek betoltese (Mentes beolvasasa jatekosokkal)");
            System.out.println("3. Teszt mod");
            System.out.println("4. Kilepes");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            //A választás alapján meghívjuk a megfelelő metódust
            switch (valasztas.trim()) {
                case "1":
                    if (terkepBetoltes()) {
                        jatekModMenu();
                    }
                    break;
                case "2":
                    jatekBetoltesMod();
                    break;
                case "3":
                    tesztMod();
                    break;
                case "4":
                    System.out.println(">>> Kilepes...");
                    System.exit(0);
                    break;
                default:
                    System.out.println(">>> Ervenytelen opcio!");
            }
        }
    }
**/

    /**
     * A grafikus változat belépési pontja.
     */
    public static void main(String[] args){
            // A GUI indítása a Singleton GraphicsApp-on keresztül
            GraphicsApp.getInstance().start();
    }

    /**
     * Segédmetódus a térkép bekéréséhez és betöltéséhez
     */
    private static boolean terkepBetoltes() {
        //Bekérjük a térkép fájlnevét a felhasználótól
        System.out.print("Add meg a terkep fajlnevet (pl. terkep.xml): ");
        String fajlNev = scanner.nextLine();
        
        aktualisTerkep = TerkepLoader.betolt(fajlNev);
        
        //Ellenőrizzük, hogy a térkép sikeresen betöltődött-e
        if (aktualisTerkep == null || aktualisTerkep.getTeljesHalozat().isEmpty()) {
            System.out.println(">>> Hiba: A terkepet nem sikerult betolteni vagy ures!");
            return false;
        }
        
        //Beállítjuk a hókotróknak a globális térképet a bolti vásárláshoz is
        Hokotro.setGlobalTerkep(aktualisTerkep);
        return true;
    }

    /**
     * Segédmetódus a játék betöltéséhez egy mentésből, majd a parancssor indításához a betöltött játékosokkal
     */
    private static void jatekBetoltesMod() {
        System.out.print("Add meg a mentes fajlnevet (pl. teszt_terkep.xml): ");
        String fajlNev = scanner.nextLine();
        
        //Létrehozunk egy üres játékteret egy üres térképpel (a betöltő majd felülírja)
        Jatekter betoltottJatek = new Jatekter(new Terkep());
        
        if (betoltottJatek.betoltes(fajlNev)) {
            //Beállítjuk a globális térképet a hókotróknak a bolt miatt
            Hokotro.setGlobalTerkep(betoltottJatek.getTerkep());
            
            System.out.println("\n>>> Jatek inditasa a betoltott mentesbol...");
            //Egyből indítjuk a parancssort a betöltött játékosokkal!
            betoltottJatek.startCommandLoop(betoltottJatek.getJatekosok());
        } else {
            System.out.println(">>> Visszateres a fomenube.");
        }
    }

    /**
     * Segédmetódus a játék mód setupjához, ahol a felhasználó manuálisan hozzáadhat takarítókat és buszsofőröket, majd elindíthatja a játékot
     */
    private static void jatekModMenu() {
        //A játék mód setup ciklusa, ahol a felhasználó hozzáadhat játékosokat és elindíthatja a játékot
        while (true) {
            System.out.println("\n--- JATEK MOD SETUP ---");
            System.out.println("Eddig hozzaadva: " + tempTakaritok.size() + " Takarito, " + tempBuszvezetok.size() + " Buszsofor");
            System.out.println("1. Uj takarito hozzaadasa");
            System.out.println("2. Uj buszsofor hozzaadasa");
            System.out.println("3. Jatek inditasa");
            System.out.println("4. Vissza");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            //A választás alapján meghívjuk a megfelelő műveletet
            switch (valasztas.trim()) {
                //Új takarító hozzáadása
                case "1":
                    System.out.print("Add meg a takarito nevet: ");
                    String tNev = scanner.nextLine();
                    Takarito takarito = new Takarito(3);
                    takarito.setNev(tNev);
                    Hokotro hokotro = new Hokotro("Hokotro_" + (tempTakaritok.size()+1), aktualisTerkep, null);

                    takarito.hozzaadHokotro(hokotro);
                    tempTakaritok.add(takarito);
                    System.out.println(">>> Takarito (" + tNev + ") es jarmuve hozzaadva!");
                    break;
                //Új buszsofőr hozzáadása
                case "2":
                    System.out.print("Add meg a buszsofor nevet: ");
                    String bNev = scanner.nextLine();
                    Buszvezeto buszvezeto = new Buszvezeto(3);
                    buszvezeto.setNev(bNev);
                    tempBuszvezetok.add(buszvezeto);
                    System.out.println(">>> Buszsofor (" + bNev + ") hozzaadva!");
                    break;
                //Játék indítása, ha van legalább 1 takarító és 1 buszsofőr
                case "3":
                    if (tempTakaritok.isEmpty() || tempBuszvezetok.isEmpty()) {
                        System.out.println(">>> Hiba: Legalabb 1 Takarito es 1 Buszsofor kell!");
                    } else {
                        startJatekLoop();
                        return;
                    }
                    break;
                //Vissza a főmenüre
                case "4":
                    return;
                //Érvénytelen opció kezelése
                default:
                    System.out.println(">>> Ervenytelen opcio!");
            }
        }
    }

    /**
     * Segédmetódus a játék indításához a manuális setup után, ahol létrehozzuk a játékteret, hozzáadjuk a játékosokat és járműveket, majd elindítjuk a parancssort
     */
    private static void startJatekLoop() {
        System.out.println("\n>>> Jatek inditasa a betoltott terkepen...");
        //Létrehozzuk a játékteret a betöltött térképpel
        Jatekter jatekter = new Jatekter(aktualisTerkep);

        List<Jatekos> sorrend = new ArrayList<>();
        int tIndex = 0;
        int bIndex = 0;

        //Váltakozva adjuk hozzá a takarítókat és buszvezetőket a sorrendhez és a játékterhez, amíg van belőlük
        while (tIndex < tempTakaritok.size() || bIndex < tempBuszvezetok.size()) {
            //Először egy takarító, majd egy buszvezető, amíg van belőlük
            if (tIndex < tempTakaritok.size()) {
                Takarito t = tempTakaritok.get(tIndex);
                sorrend.add(t);
                jatekter.hozzaadJatekos(t);
                for (Hokotro h : t.getIranyitottHokotrok()) {
                    jatekter.hozzaadJarmu(h);
                }
                tIndex++;
            }
            //Ezután egy buszvezető, amíg van belőlük
            if (bIndex < tempBuszvezetok.size()) {
                Jatekos b = tempBuszvezetok.get(bIndex);
                sorrend.add(b);
                jatekter.hozzaadJatekos(b);
                bIndex++;
            }
        }

        tempTakaritok.clear();
        tempBuszvezetok.clear();

        //Indítjuk a parancssort a létrehozott játékosokkal
        jatekter.startCommandLoop(sorrend);
    }

    /**
     * Segédmetódus a teszt módhoz, ahol először megpróbáljuk betölteni a 'teszt_terkep.xml' fájlt, 
     * majd egy menüt jelenítünk meg különböző tesztek futtatásához
     */
    private static void tesztMod() {
        System.out.println("\n>>> Teszt mod inicializalasa...");
        System.out.println(">>> A 'teszt_terkep.xml' beolvasasanak ellenorzese...");
    
        // 1. Játéktér és térkép létrehozása a beolvasáshoz
        Terkep tesztTerkep = new Terkep();
        Jatekter tesztJatekter = new Jatekter(tesztTerkep);
    
        // 2. Automatikus beolvasás megkísérlése rögtön a belépéskor
        boolean betoltesSikeres = tesztJatekter.betoltes("teszt_terkep.xml");
    
        // 3. Eredmény kiírása
        if (betoltesSikeres) {
            System.out.println(">>> [SIKER] A teszt_terkep.xml helyesen beolvasodott!");
        } else {
            System.out.println(">>> [HIBA] A teszt_terkep.xml beolvasasa sikertelen!");
        }

        // 4. Teszt menü indítása - DINAMIKUS TESZT VÁLASZTÓ
        while (true) {
            System.out.println("\n--- TESZT MOD MENU ---");
            System.out.println("Add meg a futtatni kívánt teszt számát (1-50)!");
            System.out.println("Vagy írd be, hogy '0' a visszatéréshez a főmenübe.");

            System.out.print("Választott teszt sorszáma: ");
            String valasztas = scanner.nextLine();

            try {
                int tesztSzam = Integer.parseInt(valasztas.trim());
                if (tesztSzam == 0) {
                    return; // Kilépés a főmenübe
                }
                
                // Meghívja a TesztKornyezet univerzális metódusát a sorszámmal
                TesztKornyezet.futtatTeszt(tesztSzam);
                
            } catch (NumberFormatException e) {
                System.out.println(">>> [HIBA] Érvénytelen sorszám! Kérlek csak számot adj meg.");
            }
        }
    }
}