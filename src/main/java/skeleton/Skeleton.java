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
import terkep.TerkepLoader;

public class Skeleton {

    // Osztályszintű változó a térkép tárolására, hogy minden metódus elérje
    private static Terkep aktualisTerkep;
    
    private static List<Takarito> tempTakaritok = new ArrayList<>();
    private static List<Buszvezeto> tempBuszvezetok = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   HOKOTRO SZIMULATOR PROTOTIPUS    ");
        System.out.println("====================================");

        while (true) {
            System.out.println("\nFOMENU:");
            System.out.println("1. Uj jatek (Csak terkep betoltes, manualis setup)");
            System.out.println("2. Jatek betoltese (Mentes beolvasasa jatekosokkal)");
            System.out.println("3. Teszt mod");
            System.out.println("4. Kilepes");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            switch (valasztas.trim()) {
                case "1":
                    if (terkepBetoltes()) {
                        jatekModMenu();
                    }
                    break;
                case "2":
                    jatekBetoltesMod(); // ÚJ METÓDUS HÍVÁSA
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

    /**
     * Segédmetódus a térkép bekéréséhez és betöltéséhez.
     * @return true, ha a betöltés sikeres volt.
     */
    private static boolean terkepBetoltes() {
        System.out.print("Add meg a terkep fajlnevet (pl. terkep.xml): ");
        String fajlNev = scanner.nextLine();
        
        aktualisTerkep = TerkepLoader.betolt(fajlNev);
        
        if (aktualisTerkep == null || aktualisTerkep.getTeljesHalozat().isEmpty()) {
            System.out.println(">>> Hiba: A terkepet nem sikerult betolteni vagy ures!");
            return false;
        }
        
        // Beállítjuk a hókotróknak a globális térképet a bolti vásárláshoz is
        Hokotro.setGlobalTerkep(aktualisTerkep);
        return true;
    }

    private static void jatekBetoltesMod() {
        System.out.print("Add meg a mentes fajlnevet (pl. teszt_terkep.xml): ");
        String fajlNev = scanner.nextLine();
        
        // Létrehozunk egy üres játékteret egy üres térképpel (a betöltő majd felülírja)
        Jatekter betoltottJatek = new Jatekter(new Terkep());
        
        if (betoltottJatek.betoltes(fajlNev)) {
            // Beállítjuk a globális térképet a hókotróknak a bolt miatt
            Hokotro.setGlobalTerkep(betoltottJatek.getTerkep());
            
            System.out.println("\n>>> Jatek inditasa a betoltott mentesbol...");
            // Egyből indítjuk a parancssort a betöltött játékosokkal!
            betoltottJatek.startCommandLoop(betoltottJatek.getJatekosok());
        } else {
            System.out.println(">>> Visszateres a fomenube.");
        }
    }

    private static void jatekModMenu() {
        while (true) {
            System.out.println("\n--- JATEK MOD SETUP ---");
            System.out.println("Eddig hozzaadva: " + tempTakaritok.size() + " Takarito, " + tempBuszvezetok.size() + " Buszsofor");
            System.out.println("1. Uj takarito hozzaadasa");
            System.out.println("2. Uj buszsofor hozzaadasa");
            System.out.println("3. Jatek inditasa");
            System.out.println("4. Vissza");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            switch (valasztas.trim()) {
                case "1":
                    System.out.print("Add meg a takarito nevet: ");
                    String tNev = scanner.nextLine();
                    Takarito takarito = new Takarito(3);
                    takarito.setNev(tNev);
                    // Itt az aktualisTerkep már nem null, mert a terkepBetoltes() lefutott
                    Hokotro hokotro = new Hokotro("Hokotro_" + (tempTakaritok.size()+1), aktualisTerkep, null);

                    takarito.hozzaadHokotro(hokotro);
                    tempTakaritok.add(takarito);
                    System.out.println(">>> Takarito (" + tNev + ") es jarmuve hozzaadva!");
                    break;

                case "2":
                    System.out.print("Add meg a buszsofor nevet: ");
                    String bNev = scanner.nextLine();
                    Buszvezeto buszvezeto = new Buszvezeto(3);
                    buszvezeto.setNev(bNev);
                    tempBuszvezetok.add(buszvezeto);
                    System.out.println(">>> Buszsofor (" + bNev + ") hozzaadva!");
                    break;
                
                case "3":
                    if (tempTakaritok.isEmpty() || tempBuszvezetok.isEmpty()) {
                        System.out.println(">>> Hiba: Legalabb 1 Takarito es 1 Buszsofor kell!");
                    } else {
                        startJatekLoop();
                        return;
                    }
                    break;

                case "4":
                    return;

                default:
                    System.out.println(">>> Ervenytelen opcio!");
            }
        }
    }

    private static void startJatekLoop() {
        System.out.println("\n>>> Jatek inditasa a betoltott terkepen...");
        Jatekter jatekter = new Jatekter(aktualisTerkep);

        List<Jatekos> sorrend = new ArrayList<>();
        int tIndex = 0;
        int bIndex = 0;

        while (tIndex < tempTakaritok.size() || bIndex < tempBuszvezetok.size()) {
            if (tIndex < tempTakaritok.size()) {
                Takarito t = tempTakaritok.get(tIndex);
                sorrend.add(t);
                jatekter.hozzaadJatekos(t);
                for (Hokotro h : t.getIranyitottHokotrok()) {
                    jatekter.hozzaadJarmu(h);
                }
                tIndex++;
            }
            if (bIndex < tempBuszvezetok.size()) {
                Jatekos b = tempBuszvezetok.get(bIndex);
                sorrend.add(b);
                jatekter.hozzaadJatekos(b);
                bIndex++;
            }
        }

        tempTakaritok.clear();
        tempBuszvezetok.clear();

        jatekter.startCommandLoop(sorrend);
    }

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

        // 4. Teszt menü indítása
        while (true) {
            System.out.println("\n--- TESZT MOD MENU ---");
            System.out.println("1. Sikeres vásárlás tesztelése");
            System.out.println("2. Fedezethiányos vásárlás");
            System.out.println("3. Hókotró takarítás (Söprőfej)");
            System.out.println("4. Busz mozgás és ütközés");
            System.out.println("5. Globális körváltás és hóesés");
            System.out.println("6. Vissza a főmenübe");

            System.out.print("Válassz egy tesztet: ");
            String valasztas = scanner.nextLine();

            switch (valasztas.trim()) {
                case "1": TesztKornyezet.Test2(); break;
                case "2": TesztKornyezet.Test3(); break;
                case "3": TesztKornyezet.Test4(); break;
                case "4": TesztKornyezet.Test6(); break;
                case "5": TesztKornyezet.Test7(); break;
                case "6": return; // Kilépés a főmenübe
                default: System.out.println(">>> Érvénytelen opció!");
            }
        }
    }
}