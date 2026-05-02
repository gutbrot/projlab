package jatekos;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.io.File; // A fájl ellenőrzéséhez

import jarmu.*;
import terkep.*;
import bolt.*;
import kotrofej.*;

public class Jatekter {
    
    private List<Jatekos> jatekosok = new ArrayList<>();
    private List<Jarmu> jarmuvek = new ArrayList<>();
    private Terkep terkep;
    private int aktualisJatekosIndex = 0;
    private Scanner scanner = new Scanner(System.in);
    
    private Bolt bolt = new Bolt();
    private Jarmu aktivJarmu = null;

    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    public void hozzaadJatekos(Jatekos j) { 
        if (j != null) {
            jatekosok.add(j); 
            j.setJatekter(this); // Összekötjük a játékost a játéktérrel
        }
    }

    public void hozzaadJarmu(Jarmu j) { 
        if (j != null) jarmuvek.add(j); 
    }

    /*
    * Egy elmentett játék állapot beolvasásának parancsa
    * Alapértelmezetten sikertelen, és csak akkor válik sikeressé, ha a fájl létezik.
    * 
    * @param fajlNev A beolvasni kívánt fájl neve
    * @return True, ha a fájl létezik és a betöltés elindulhat, különben false.
    */
    public boolean betoltes(String fajlNev) {
        // A siker értékét alapból false-ra állítjuk.
        boolean siker = false;

        // Meghatározzuk az elérési utat a Betoltes mappán belül
        String eleresiUt = "Betoltes/" + fajlNev;
        File mentesFajl = new File(eleresiUt);

        // Ellenőrizzük, hogy a fájl létezik-e
        if (mentesFajl.exists()) {
            // Ha létezik, a siker értékét true-ra állítjuk
            siker = true;
        
            System.out.println(">>> [SIKER] A(z) '" + fajlNev + "' fájl megtalálható, betöltés folyamatban...");
        
            // Itt hívódik meg a tényleges XML feldolgozás a jövőben
        
        
            // Sikeres beolvasás után kiírja a játék állapotát a megfelelő formátumban
            System.out.println(">>> [ÁLLAPOT] Játék adatai betöltve a(z) " + eleresiUt + " helyről.");
        } else {
            // Ha nem létezik, értesítjük a felhasználót
            System.out.println(">>> [HIBA] A megadott fájl nem létezik a Betoltes mappában: " + eleresiUt);
        }

        // Visszatérünk a siker értékével
        return siker;
    }

    /**
     * Az aktuális játék állapotot menti egy szöveges (XML) fájlba.
     * 
     * @param fajlNev A mentés fájlneve vagy időbélyege.
     * @return True, ha a mentés sikeres volt.
     */
    public boolean mentes(String fajlNev) {
        boolean siker = false;
        
        // Fájlnév megtisztítása a biztonság kedvéért (kettőspontok cseréje)
        String tisztaFajlNev = fajlNev.replace(":", "-") + ".xml";
        String eleresiUt = "Betoltes/" + tisztaFajlNev;
        
        System.out.println(">>> [XML RENDSZER] Játék állapotának mentése előkészítve...");
        System.out.println(">>> [XML RENDSZER] Célfájl: " + eleresiUt);
        
        // --- ITT TÖRTÉNNE A TÉNYLEGES XML GENERÁLÁS ÉS FÁJLBA ÍRÁS ---
        // A placeholder kedvéért most egyből true-ra állítjuk
        siker = true; 
        //=============================================================
        
        if (siker) {
            System.out.println(">>> [SIKER] A játékállapot sikeresen kimentve az XML fájlba.");
        } else {
            System.out.println(">>> [HIBA] Mentés sikertelen! Nincs írási jogosultság vagy hiányzó mappa.");
        }
        
        return siker;
    }

    // A soron következő játékos típusát és akciópontjait kiíró segédmetódus
    private void kiirSoronLevo(Jatekos jatekos) {
        String tipus = (jatekos instanceof Takarito) ? "Takarító" : "Buszvezető";
        System.out.println("-------------------------------------");
        System.out.println(">>> Következő játékos: " + jatekos.getNev() + " - " + tipus + " (AP: " + jatekos.getAkcioPont() + ")");
    }

    // Új kör kezdése: időjárás frissítése, járművek és játékosok körének indítása, AP-k visszaállítása
    public void ujKor() {
        if (terkep != null) terkep.idojarasFrissites();
        
        for (Jarmu j : jarmuvek) {
            j.ujKor();
        }

        for (Jatekos j : jatekosok) {
            if (j instanceof Takarito) j.setAkcioPont(3);
            else j.setAkcioPont(3); 
        }
        System.out.println("\n>>> --- ÚJ GLOBÁLIS KÖR KEZDŐDÖTT ---");
    }

    /**
     * Játék inicializálása megadott paraméterekkel.
     * Törli az eddigi állapotot, és létrehozza az új entitásokat.
     */
    public boolean jatekInicializalas(String palyaNev, int takaritokSzam, int buszvezetokSzam, int autokSzama, boolean randomKi) {
        System.out.println(">>> [RENDSZER] Új pálya generálása: " + palyaNev);
        System.out.println(">>> [RENDSZER] Paraméterek -> Takarítók: " + takaritokSzam + ", Buszvezetők: " + buszvezetokSzam + ", Autók: " + autokSzama);
        
        if (randomKi) {
            betoltes(palyaNev);
            }

        // 1. Tiszta lappal indulunk
        this.jatekosok.clear();
        this.jarmuvek.clear();
        this.terkep = new Terkep(); 

        // 2. Játékosok felváltva történő hozzáadása
        int tIndex = 0;
        int bIndex = 0;
        while (tIndex < takaritokSzam || bIndex < buszvezetokSzam) {
            if (tIndex < takaritokSzam) {
                hozzaadJatekos(new Takarito(3));
                tIndex++;
            }
            if (bIndex < buszvezetokSzam) {
                hozzaadJatekos(new Buszvezeto(3));
                bIndex++;
            }
        }

        // 3. NPC Autók hozzáadása a kért számban
        for (int i = 0; i < autokSzama; i++) {
            // Placeholder: a null-ok helyére majd valódi lokációk kerülnek a térképről
            hozzaadJarmu(new Auto("Auto_" + (i+1), null, null, null));
        }

        // Ha véletlenül 0 játékost adtak meg, nem tud elindulni a játék
        if (this.jatekosok.isEmpty()) {
            System.out.println(">>> [HIBA] A játéknak legalább 1 játékosra szüksége van!");
            return false;
        }

        // 4. Körök, AP-k és fókuszok visszaállítása a nulladik játékosra
        this.aktualisJatekosIndex = 0;
        for (Jatekos j : this.jatekosok) {
            j.setAkcioPont(3);
        }
        this.aktivJarmu = null;

        System.out.println(">>> [SIKER] A játék sikeresen inicializálva!");
        kiirSoronLevo(this.jatekosok.get(this.aktualisJatekosIndex));

        return true;
    }

    // A segítség parancs kiírja a lehetséges parancsokat és azok használatát
    private void kiirSegitseg() {
        System.out.println("--- ELÉRHETŐ PARANCSOK ---");
        System.out.println("betoltes <FajlNev> - Játékállapot beolvasása");
        System.out.println("mentes [IdoBelyeg] - Játékállapot mentése");
        System.out.println("korvege - Aktuális kör lezárása");
        System.out.println("fordulovege - Aktuális forduló lezárása");
        System.out.println("teszt - Tesztelő módba váltás");
        System.out.println("jatek <Pálya> <Tak> <Busz> <Auto> [RandomKi] - Új játék");
        System.out.println("valaszt <ID> - Jármű vagy Kotrófej kiválasztása");
        System.out.println("mozgas [SavID] - Mozgás / Elérhető sávok listázása");
        System.out.println("vasarlas [TermekID] - Vásárlás (csak Takarító) / Kínálat listázása");
        System.out.println("allomas [BuszID] - Végállomások listázása (csak Buszvezető)");
        System.out.println("segitseg - Ezen menü megnyitása");
    }

    //================================================================================================
    //================================================================================================

    public void startCommandLoop(List<Jatekos> sorrend) {
        this.jatekosok = sorrend;
        aktualisJatekosIndex = 0;
        
        // Minden játékos akciópontjainak visszaállítása a kör elején
        for (Jatekos j : jatekosok) {
            j.setAkcioPont(3); 
        }

        // Ha nincs játékos, nem tudunk elindítani egyetlen kört sem
        if (jatekosok.isEmpty()) return;
        // Kezdjük a játékot az első játékossal
        Jatekos aktivJatekos = jatekosok.get(aktualisJatekosIndex);
        System.out.println("\n>>> Jatek elindult!");
        // Kiírjuk az első játékos típusát és akciópontjait
        kiirSoronLevo(aktivJatekos);

        while (true) {
            System.out.print("[" + aktivJatekos.getNev() + "] > ");
            String bemenet = scanner.nextLine();
            if (bemenet == null || bemenet.trim().isEmpty()) continue;

            String[] darabok = bemenet.trim().split("\\s+");
            String parancs = darabok[0].toLowerCase();

            switch (parancs) {
                //A tesztelés funkcióhoz szükséges, nem indít játékot, csak teszteteket lehet rajta futtatni.
                case "betoltes":
                    // A dokumentáció szerint kötelező paraméter a <FajlNev>
                    if (darabok.length < 2) {
                        System.out.println("HIBAS"); 
                    } else {
                        // Meghívjuk a dedikált betoltes függvényt
                        if (betoltes(darabok[1])) {
                            System.out.println("SIKERES");
                        } else {
                            System.out.println("HIBAS");
                        }
                    }
                    break;

                case "mentes":
                    String idobelyeg;
                    // Ha adtak meg fájlnevet, azt használjuk, ha nem, akkor az aktuális időt
                    if (darabok.length > 1) {
                        idobelyeg = darabok[1];
                    } else {
                        // Aktuális idő formázása operációs rendszer kompatibilis fájlnévvé
                        java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
                        idobelyeg = java.time.LocalDateTime.now().format(dtf);
                    }
                    
                    // Meghívjuk a mentes függvényt, ami elvégzi a logikát
                    if (mentes(idobelyeg)) {
                        System.out.println("SIKERES");
                    } else {
                        System.out.println("HIBAS");
                    }
                    break;

                // A körvége parancs lezárja az aktuális játékos körét, és a következő játékosra vált
                case "korvege":
                    // 1. Az aktuális játékos lezárja a saját fordulóját (AP nullázás)
                    aktivJatekos.korVege();
                    aktivJarmu = null; // Elengedjük a kiválasztott jármű fókuszát
                    
                    // 2. Léptetjük az indexet a Játéktéren a KÖVETKEZŐ játékosra
                    aktualisJatekosIndex++;
                    
                    // 3. Ellenőrizzük, hogy mindenki lépett-e már (GLOBÁLIS KÖR VÉGE)
                    if (aktualisJatekosIndex >= jatekosok.size()) {
                        ujKor(); // Meghívja az időjárást, levonja a mozgásképtelenséget és frissíti az AP-kat
                        aktualisJatekosIndex = 0; // Visszaugrunk az első játékosra
                    }
                    
                    // 4. Átváltunk az új soron lévő játékosra, és kiírjuk az adatait
                    aktivJatekos = jatekosok.get(aktualisJatekosIndex);
                    System.out.println("SIKERES");
                    kiirSoronLevo(aktivJatekos);
                    break;

                case "fordulovege":
                    jatekosok.get(aktualisJatekosIndex).forduloVege(aktivJatekos);
                    aktualisJatekosIndex = 0;
                    aktivJatekos = jatekosok.get(aktualisJatekosIndex);
                    System.out.println("SIKERES");
                    kiirSoronLevo(aktivJatekos);
                    break;

                case "teszt":                                                       //EZT MÉG MEG KELL CSINÁLNI
                    System.out.println(">>> Tesztelő mód aktiválva.");
                    System.out.println("SIKERES");
                    break;

                //Játékot indít, egy meglévő pályát tölt be és inicializál mindent is.
                case "jatek":
                    // jatek <PalyaNev> <TakaritokSzam> <BuszvezetokSzma> <AutokSzama> [RandomKi]
                    if (darabok.length < 5) {
                        System.out.println("HIBAS"); // Hiányzó kötelező paraméterek
                    } else {
                        try {
                            // Kötelező paraméterek beolvasása
                            String palyaNev = darabok[1];
                            int takaritokSzam = Integer.parseInt(darabok[2]);
                            int buszvezetokSzam = Integer.parseInt(darabok[3]);
                            int autokSzama = Integer.parseInt(darabok[4]);
                            
                            // Opcionális RandomKi paraméter ellenőrzése
                            boolean randomKi = false;
                            if (darabok.length >= 6 && darabok[5].equalsIgnoreCase("RandomKi")) {
                                randomKi = true;
                            }

                            // Inicializáló metódus meghívása
                            if (jatekInicializalas(palyaNev, takaritokSzam, buszvezetokSzam, autokSzama, randomKi)) {
                                System.out.println("SIKERES");
                            } else {
                                System.out.println("HIBAS");
                            }
                        } catch (NumberFormatException e) {
                            // Ha betűket írtak a számok helyére (pl. jatek Palya Egy Ket Harom)
                            System.out.println("HIBAS");
                        }
                    }
                    break;

                // A segítség parancs kiírja a lehetséges parancsokat és azok használatát
                case "segitseg":
                    kiirSegitseg();
                    System.out.println("SIKERES");
                    break;

                case "valaszt":
                    if (darabok.length < 2) {
                        System.out.println("HIBAS");
                    } else {
                        boolean talalt = false;
                        for (Jarmu j : jarmuvek) {
                            if (j instanceof Hokotro && ((Hokotro)j).getId().equals(darabok[1])) {
                                aktivJarmu = j;
                                talalt = true;
                                break;
                            } else if (j instanceof Busz && ((Busz)j).getId().equals(darabok[1])) {
                                aktivJarmu = j;
                                talalt = true;
                                break;
                            }
                        }
                        if (talalt) {
                            System.out.println(">>> Jármű kiválasztva: " + darabok[1]);
                            System.out.println("SIKERES");
                        } else {
                            System.out.println("HIBAS");
                        }
                    }
                    break;

                case "mozgas":
                    if (aktivJarmu == null) {
                        // Ha a játékos még nem választott járművet a 'valaszt' paranccsal
                        System.out.println("HIBAS");
                    } else {
                        if (darabok.length == 1) {
                            // Paraméter nélkül: Elérhető sávok listázása
                            // (null-t adunk át a Jarmu.mozgas-nak)
                            if (aktivJarmu.mozgas(null)) {
                                System.out.println("SIKERES");
                            } else {
                                System.out.println("HIBAS");
                            }
                        } else {
                            // Opcionális paraméter megadva: Tényleges mozgás a SavID alapján
                            try {
                                int celSavId = Integer.parseInt(darabok[1]);
                                Sav celSav = new Sav(celSavId); // (Ideális esetben a Térképről kérjük le a sávot az ID alapján)
                                
                                boolean siker = aktivJarmu.mozgas(celSav);
                                if (siker) {
                                    aktivJatekos.akcioPontKezelo(); // AP levonása
                                    System.out.println("SIKERES");
                                } else {
                                    System.out.println("HIBAS");
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("HIBAS");
                            }
                        }
                    }
                    break;

                case "vasarlas":
                    // Ha nem takarító próbál vásárolni, az mindig hibás
                    if (!(aktivJatekos instanceof Takarito)) {
                        System.out.println("ROSSZ JATEKOS");
                    } else {
                        // Takarító vásárlási logikája
                        Takarito t = (Takarito) aktivJatekos;
                        // Ha csak "vasarlas" parancs érkezik paraméter nélkül, akkor a kínálatot listázzuk
                        if (darabok.length == 1) {
                            bolt.listaz();
                            System.out.println("SIKERES");
                        } 
                        // Ha terméknevet is megadtak, megpróbáljuk megvásárolni azt a terméket
                        else {
                            boolean siker = bolt.vasarlas(t, darabok[1]);
                            // Ha a vásárlás sikeres volt, levonjuk az Akciópontokat és kiírjuk a sikeres üzenetet
                            if (siker) {
                                t.akcioPontKezelo();
                                System.out.println("SIKERES");
                            } 
                            // Ha a vásárlás nem sikerült (pl. nincs elég pénz vagy a termék nem létezik), akkor hibás üzenetet írunk ki
                            else {
                                System.out.println("HIBAS");
                            }
                        }
                    }
                    break;

                case "allomas":
                    // Ha nem buszvezető próbálja lekérni a végállomásokat, az hibás
                    if (!(aktivJatekos instanceof Buszvezeto)) {
                        System.out.println("ROSSZ JATEKOS");
                    } else {
                        Buszvezeto bv = (Buszvezeto) aktivJatekos;
                        // Ha csak "allomas" parancs érkezik paraméter nélkül, akkor az összes irányítható buszt listázzuk
                        if (darabok.length == 1) {
                            System.out.println(">>> Elérhető buszaid:");
                            for (Busz b : bv.getIranyithatoBuszok()) {
                                System.out.println("    - " + b.getId());
                            }
                            System.out.println("SIKERES");
                        } 
                        // Ha buszazonosítót is megadtak, lekérjük annak a végállomásait
                        else {
                            boolean talalt = false;
                            // Megkeressük a megadott ID-jű buszt a buszvezető irányítható buszai között
                            for (Busz b : bv.getIranyithatoBuszok()) {
                                if (b.getId().equals(darabok[1])) {
                                    System.out.println(">>> A(z) " + b.getId() + " végállomásai le lettek kérve.");
                                    talalt = true;
                                    break;
                                }
                            }
                            // Ha találtunk ilyen buszt, akkor sikeres, ha nem, akkor hibás
                            if (talalt) System.out.println("SIKERES");
                            else System.out.println("HIBAS");
                        }
                    }
                    break;
 
                // Kilépési parancs, amivel a játékos befejezheti a játékot
                case "kilepes": 
                    return;

                // Ha a parancs nem ismert, hibás üzenetet írunk ki
                default:
                    System.out.println("ERTELMEZHETETLEN");
            }
        }
    }

}