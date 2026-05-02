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

    private static List<Takarito> tempTakaritok = new ArrayList<>();
    private static List<Buszvezeto> tempBuszvezetok = new ArrayList<>();
    
    // A standard Java beolvasó eszköz
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================");
        System.out.println("   HÓKOTRÓ SZIMULÁTOR PROTOTÍPUS    ");
        System.out.println("====================================");

        while (true) {
            System.out.println("\nFŐMENÜ:");
            System.out.println("1. Jatek mod");
            System.out.println("2. Teszt mod");
            System.out.println("3. Kilepes");

            System.out.print("Valassz egy opciot: ");
            String valasztas = scanner.nextLine();

            switch (valasztas.trim()) {
                case "1":
                    jatekModMenu();
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
                    System.out.print("Add meg a takarito nevet (ekezet nelkul, egybe): ");
                    String tNev = scanner.nextLine();
                    
                    Takarito takarito = new Takarito(3);
                    takarito.setNev(tNev);

                    Hokotro hokotro = new Hokotro("Hokotro_" + (tempTakaritok.size()+1), null, null);

                    takarito.hozzaadHokotro(hokotro);
                    tempTakaritok.add(takarito);

                    System.out.println(">>> [RENDSZER] Hókotró létrehozva: " + hokotro.getId() + " és hozzárendelve a " + tNev + " nevű takarítóhoz.");
                    System.out.println(">>> Takarito (" + tNev + ") hozzaadva!");
                    break;
                case "2":
                    System.out.print("Add meg a buszsofor nevet (ekezet nelkul, egybe): ");
                    String bNev = scanner.nextLine();
                    tempBuszvezetok.add(new Buszvezeto(3));
                    //tempBuszvezetok.setNev(bNev);

                    System.out.println(">>> Buszsofor (" + bNev + ") hozzaadva!");
                    break;
                case "3":
                    if (tempTakaritok.isEmpty() || tempBuszvezetok.isEmpty()) {
                        System.out.println(">>> Hiba: A jatek nem tud elindulni, amig nincs legalabb 1 Takarito es 1 Buszsofor!");
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
        System.out.println("\n>>> Jatek inicializalasa...");
        
        Terkep terkep = new Terkep();
        Jatekter jatekter = new Jatekter(terkep);

        List<Jatekos> sorrend = new ArrayList<>();
        int tIndex = 0;
        int bIndex = 0;

        while (tIndex < tempTakaritok.size() || bIndex < tempBuszvezetok.size()) {
            if (tIndex < tempTakaritok.size()) {
                Takarito takarito = tempTakaritok.get(tIndex);
                
                sorrend.add(takarito);
                jatekter.hozzaadJatekos(takarito);

                for (Hokotro h : takarito.getIranyitottHokotrok()) {
                    jatekter.hozzaadJarmu(h);
                }

                tIndex++;
            }
            if (bIndex < tempBuszvezetok.size()) {
                sorrend.add(tempBuszvezetok.get(bIndex));
                jatekter.hozzaadJatekos(tempBuszvezetok.get(bIndex));
                bIndex++;
            }
        }

        tempTakaritok.clear();
        tempBuszvezetok.clear();

        jatekter.startCommandLoop(sorrend);
    }

    private static void tesztMod() {
        System.out.println(">>> Teszt mod inicializalasa...");
        System.out.println(">>> (Ide jon majd az XML fajlok beolvasasa es kiirasa a kesobbi fazisban)");
    }
}