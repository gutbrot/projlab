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
            System.out.println("1. Jatek mod");
            System.out.println("2. Teszt mod");
            System.out.println("3. Kilepes");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            switch (valasztas.trim()) {
                case "1":
                    // Mielőtt belépnénk a setupba, be kell tölteni a térképet
                    if (terkepBetoltes()) {
                        jatekModMenu();
                    }
                    break;
                case "2":
                    tesztMod();
                    break;
                case "3":
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
        System.out.println(">>> Teszt mod inicializalasa...");
        System.out.println(">>> (Ide jon majd az XML parancsfajlok beolvasasa)");
    }
}