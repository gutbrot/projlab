package skeleton;

import jarmu.Busz;
import jarmu.Hokotro;
import jatekos.Buszvezeto;
import jatekos.Jatekter;
import jatekos.Takarito;
import jarmu.*;
import jatekos.*;
import terkep.*;

/**
 * A TesztKornyezet osztály átmeneti (stub) változata[cite: 12].
 * Üres metódusokat tartalmaz, hogy a Skeleton osztály hiba nélkül leforduljon,
 * amíg a fejlesztőcsapat nem tisztázza a konstruktorok és a világépítés pontos részleteit[cite: 12].
 */
public abstract class TesztKornyezet {

    /**
     * Ide kerül majd a világ felépítése (Térkép, Járművek, Szereplők inicializálása)[cite: 12].
     */
    public static void initializeWorld() {
        Console.print(">>> [Figyelmeztetés] A világépítés (initializeWorld) metódus jelenleg üres! Beszélj a csapattal a konstruktorokról.");
    }

    public static void Test1() {
        Console.print("\n=== INTERAKTÍV JÁTÉK MÓD INDÍTÁSA ===");

        // 1. Pálya felépítése a háttérben
        Terkep terkep = new Terkep();
        SimaUt ut = new SimaUt("Teszt Pálya", 5, 1); // 5 hosszú, 1 sávos út
        terkep.addUt(ut);
        Jatekter jatekter = new Jatekter(terkep);

        // 2. Játékos és Hókotró (Kotrófej még nincs, így null)
        Takarito takarito = new Takarito(3, 100);
        Lokacio kezdoPoz = new Lokacio(ut, ut.getSzakaszok().get(0), ut.getSzakaszok().get(0).get(0));
        Hokotro hokotro = new Hokotro("H1", kezdoPoz, null);
        
        takarito.hozzaadHokotro(hokotro);
        jatekter.hozzaadJatekos(takarito);
        jatekter.hozzaadJarmu(hokotro);

        jatekter.jatekStart();
        
        // Változók a játékos helyzetének követéséhez
        int aktualisSzakasz = 0;
        boolean jatekFut = true;

        // --- INTERAKTÍV JÁTÉKHUROK ---
        while (jatekFut) {
            Console.print("\n------------------------------------------------");
            Console.print(">>> JÁTÉK ÁLLAPOTA:");
            Console.print("Hókotró helyzete: " + aktualisSzakasz + ". szakasz");
            Console.print("Hóvastagság előtted: " + ut.getSzakaszok().get(aktualisSzakasz).get(0).getHo() + " cm");
            Console.print("Akciópontjaid: " + takarito.getAkcioPont());
            Console.print("------------------------------------------------");
            
            Console.print("\nMIT SZERETNÉL TENNI?");
            Console.print("  [1] Lépés előre a következő szakaszra");
            Console.print("  [2] Új kör indítása (Mindenki visszakapja az AP-ját, és esik a hó!)");
            Console.print("  [0] Kilépés a főmenübe");

            String valasz = Console.readLine("\nVálasztásod: ");

            switch (valasz) {
                case "1":
                    if (aktualisSzakasz < ut.getHossz() - 1) {
                        aktualisSzakasz++; // Növeljük az indexet
                        // Lekérjük a következő sávot, és rámozgatjuk a hókotrót
                        Sav celSav = ut.getSzakaszok().get(aktualisSzakasz).get(0);
                        hokotro.mozgas(celSav);
                    } else {
                        Console.print("\n>>> [!] Elérted az út legvégét, nem tudsz tovább menni!");
                    }
                    break;

                case "2":
                    Console.print("\n>>> Kör lezárása... Új kör indul!");
                    jatekter.ujKor();
                    break;

                case "0":
                    Console.print("\n>>> Kilépés az interaktív módból...");
                    jatekFut = false;
                    break;

                default:
                    Console.print("\n>>> [!] Érvénytelen parancs! Kérlek 0, 1 vagy 2 közül válassz.");
            }
        }
    }

    public static void Test2() {
        Console.print(">>> [Figyelmeztetés] A 2. teszt (Sikeres vásárlás) jelenleg üres!");
    }

    public static void Test3() {
        Console.print(">>> [Figyelmeztetés] A 3. teszt (Fedezethiány) jelenleg üres!");
    }

    public static void Test4() {
        Console.print(">>> [Figyelmeztetés] A 4. teszt (Takarítás) jelenleg üres!");
    }

    public static void Test5() {
        Console.print(">>> [Figyelmeztetés] Az 5. teszt (Eszközváltás) jelenleg üres!");
    }

    public static void Test6() {
        Console.print(">>> [Figyelmeztetés] A 6. teszt (Busz mozgás) jelenleg üres!");
    }

    public static void Test7() {
        Console.print(">>> [Figyelmeztetés] A 7. teszt (Körváltás) jelenleg üres!");
    }

    public static void Test8() {
        Console.print(">>> [Figyelmeztetés] A 8. teszt (Alagút) jelenleg üres!");
    }
}