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
import seged.*;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jatekter {
    
    // A játékosok listája, amelyben a játékban résztvevő összes játékos szerepel.
    private List<Jatekos> jatekosok = new ArrayList<>();
    // A járművek listája, amelyben a játékban résztvevő összes jármű szerepel.
    private List<Jarmu> jarmuvek = new ArrayList<>();
    // A térkép, amelyen a játék zajlik.
    private Terkep terkep;
    // Az aktuális játékos indexe a jatekosok listájában, amely meghatározza, hogy ki a soron következő játékos.
    public int aktualisJatekosIndex = 0;
    private Scanner scanner = new Scanner(System.in);

    private static Terkep globalTerkep;
    
    // A bolt, amelyből a takarító játékosok vásárolhatnak eszközöket és járműveket.
    private Bolt bolt = new Bolt();
    private Jarmu aktivJarmu = null;

    // Konstruktor, amely inicializálja a játéktér térképét.
    public Jatekter(Terkep terkep) {
        this.terkep = terkep;
    }

    // A térkép beállítása és a globális térkép frissítése.
    public void setTerkep(Terkep terkep) {
        this.terkep = terkep;
        globalTerkep = terkep; // Itt állítjuk be a statikus referenciát!
    }

    // A globális térkép lekérdezése.
    public static Terkep getGlobalTerkep() {
        return globalTerkep;
    }

    // Játékos hozzáadása a játéktérhez, amely egyben összeköti a játékost a játéktérrel is.
    public void hozzaadJatekos(Jatekos j) { 
        if (j != null) {
            jatekosok.add(j); 
            j.setJatekter(this); // Összekötjük a játékost a játéktérrel
        }
    }

    // Jármű hozzáadása a játéktérhez.
    public void hozzaadJarmu(Jarmu j) { 
        if (j != null) jarmuvek.add(j); 
    }

    public boolean betoltes(String fajlNev) {
        // Tiszta lappal indulunk betöltéskor
        this.jatekosok.clear();
        this.jarmuvek.clear();
        
        seged.Betolteskezelo bekezelo = new seged.Betolteskezelo();
        boolean siker = bekezelo.betolt(fajlNev, this);
        
        if (siker) {
            System.out.println(">>> [SIKER] Játékállapot betöltve a Játéktérbe.");
        }
        return siker;
    }

    /**
     * Az aktuális játék állapotot menti egy szöveges (XML) fájlba.
     * 
     * @param fajlNev A mentés fájlneve vagy időbélyege.
     * @return True, ha a mentés sikeres volt.
     */
    public boolean mentes(String fajlNev) {
        // Fájlnév megtisztítása a biztonság kedvéért (kettőspontok cseréje, kiterjesztés hozzáadása)
        String tisztaFajlNev = fajlNev.replace(":", "-");
        if (!tisztaFajlNev.endsWith(".xml")) {
            tisztaFajlNev += ".xml";
        }
        String eleresiUt = "Betoltes/" + tisztaFajlNev;
        
        System.out.println(">>> [XML RENDSZER] Játék állapotának mentése előkészítve...");
        System.out.println(">>> [XML RENDSZER] Célfájl: " + eleresiUt);
        
        // BIZTONSÁG: Ellenőrizzük, hogy létezik-e a "Betoltes" mappa, és ha nem, létrehozzuk!
        File mappa = new File("Betoltes");
        if (!mappa.exists()) {
            mappa.mkdir();
            System.out.println(">>> [XML RENDSZER] Létrehozva a hiányzó 'Betoltes' könyvtár.");
        }

        // Tényleges mentés meghívása a MentesKezelo segítségével
        boolean siker = seged.MentesKezelo.allapototMent(this, eleresiUt);
        
        if (siker) {
            System.out.println(">>> [SIKER] A játékállapot sikeresen kimentve az XML fájlba.");
        } else {
            System.out.println(">>> [HIBA] Mentés sikertelen! Nincs írási jogosultság vagy hiba lépett fel.");
        }
        
        return siker;
    }

    // A soron következő játékos típusát és akciópontjait kiíró segédmetódus
    private void kiirSoronLevo(Jatekos jatekos) {
        String tipus = (jatekos instanceof Takarito) ? "Takarító" : "Buszvezető";
        System.out.println("-------------------------------------");
        System.out.println(">>> Következő játékos: " + jatekos.getNev() + " - " + tipus + " (AP: " + jatekos.getAkcioPont() + ")");
    }

    private void alapertelmezettJarmuBeallitasa(Jatekos jatekos) {
        jatekos.aktivJarmu = null;

        if (jatekos instanceof Takarito) {
            Takarito t = (Takarito) jatekos;

            if (!t.getIranyitottHokotrok().isEmpty()) {
                aktivJarmu = t.getIranyitottHokotrok().get(0);
                System.out.println(">>> Alapértelmezett hókotró kiválasztva: " + ((Hokotro) aktivJarmu).getId());
            }
        } else if (jatekos instanceof Buszvezeto) {
            Buszvezeto bv = (Buszvezeto) jatekos;

            if (!bv.getIranyithatoBuszok().isEmpty()) {
                aktivJarmu = bv.getIranyithatoBuszok().get(0);
                System.out.println(">>> Alapértelmezett busz kiválasztva: " + ((Busz) aktivJarmu).getId());
            }
        }
    }

    // Ez a metódus felelős annak ellenőrzéséért, hogy a soron lévő játékosnak van-e még akciópontja.
    // Ha nincs, akkor automatikusan átadja a körét a következő játékosnak.
    private Jatekos leptetHaNincsAkcioPont(Jatekos aktivJatekos) {
        if (aktivJatekos.getAkcioPont() > 0) {
            return aktivJatekos;
        }

        System.out.println(">>> " + aktivJatekos.getNev() + " befejezte a körét.");

        aktivJarmu = null;

        aktualisJatekosIndex++;

        // Ha az aktuális játékos indexe meghaladja a játékosok számát, akkor új kört kezdünk, és visszaállítjuk az indexet.
        if (aktualisJatekosIndex >= jatekosok.size()) {
            ujKor();
            aktualisJatekosIndex = 0;
        }

        Jatekos ujAktivJatekos = jatekosok.get(aktualisJatekosIndex);
        kiirSoronLevo(ujAktivJatekos);

        return ujAktivJatekos;
    }

    // Új kör kezdése
    public void ujKor() {
        // Térkép frissítése
        if (terkep != null) terkep.idojarasFrissites();

        System.out.println("\n>>> [RENDSZER] NPC autók automatikus mozgatása...");
        Navigacio navigacio = new Navigacio();

        // Végigmegyünk a járművek listáján
        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                Auto auto = (Auto) j;
                
                // Ha balesetet szenvedett, pihen és gyógyul
                if (auto.getMozgaskeptelenKorokSzama() > 0) {
                    auto.ujKor();
                    continue;
                }

                // Cél meghatározása: ha elértük az aktuális végállomást, forduljon meg
                Lokacio[] vegallomasok = auto.getVegallomasok();
                Lokacio celLokacio = vegallomasok[auto.getCelVegallomasIndex()];
                if (celLokacio != null && auto.getPozicio() != null &&
                        auto.getPozicio().getSav() == celLokacio.getSav()) {
                    auto.toggleCelVegallomasIndex();
                    celLokacio = vegallomasok[auto.getCelVegallomasIndex()];
                }

                // Útvonaltervezés és közvetlen mozgatás
                Sav kovetkezoSav = navigacio.kovetkezoLepes(auto.getPozicio(), celLokacio);
                if (kovetkezoSav != null) {
                    auto.mozgasDirekt(kovetkezoSav);
                }
            } else {
                // Más járművek (hókotró, busz) baleseti számlálójának csökkentése
                j.ujKor();
            }
        }

        // Akciópontok visszaállítása a kör elején
        for (Jatekos jatekos : jatekosok) {
            jatekos.setAkcioPont(3); 
        }
        System.out.println(">>> --- ÚJ GLOBÁLIS KÖR KEZDŐDÖTT ---\n");
    }

    //todo: Ezen dolgozni kell még
    /*private void npcAutokMozgatasa() {
        System.out.println(">>> [RENDSZER] NPC autók automatikus mozgatása...");
        seged.Navigacio navigacio = new seged.Navigacio();

        for (Jarmu j : jarmuvek) {
            if (j instanceof Auto) {
                Auto auto = (Auto) j;
                
                if (auto.getMozgaskeptelenKorokSzama() > 0) continue;

                // Az Auto 1-es indexű végállomása a munkahely/cél (a 0-ás a kiinduló lakás)
                if (auto.getVegallomasok() != null && auto.getVegallomasok().length > 1) {
                    Lokacio cel = auto.getVegallomasok()[1]; 
                    
                    if (cel != null && !auto.vegallomasraErt()) {
                        Sav kovetkezoSav = navigacio.kovetkezoLepes(auto.getPozicio(), cel);
                        
                        if (kovetkezoSav != null) {
                            // Itt hívjuk a frissen létrehozott direkt metódust!
                            auto.mozgasDirekt(kovetkezoSav);
                        }
                    }
                }
            }
        }
    }*/

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

    // A globális térkép lekérdezése
    public Terkep getTerkep() {
        return this.terkep;
    }

    public java.util.List<jarmu.Jarmu> getJarmuvek() {                                  //Ha fölös törökjük
        return this.jarmuvek;
    }

    public java.util.List<Jatekos> getJatekosok() {
        return this.jatekosok;
    }

    public Jatekos getAktivJatekos() {
        if (jatekosok.isEmpty()) return null;
        return jatekosok.get(aktualisJatekosIndex);
    }

    public Bolt getBolt() {
        return bolt;
    }

    /**
     * Ha az aktuális játékos AP-ja 0, automatikusan lezárja a körét.
     * @return true, ha körváltás történt
     */
    public boolean autoKorvaltas() {
        Jatekos akt = getAktivJatekos();
        if (akt != null && akt.getAkcioPont() <= 0) {
            korVegeVegrehajtas();
            return true;
        }
        return false;
    }

    /**
     * GUI-ból hívható körváltó logika: lezárja az aktuális játékos körét,
     * lépteti az indexet, és szükség esetén új globális kört indít.
     */
    public void korVegeVegrehajtas() {
        if (jatekosok.isEmpty()) return;
        Jatekos aktJatekos = jatekosok.get(aktualisJatekosIndex);
        aktJatekos.korVege();
        this.aktivJarmu = null;

        aktualisJatekosIndex++;
        if (aktualisJatekosIndex >= jatekosok.size()) {
            ujKor();
            aktualisJatekosIndex = 0;
        }

        Jatekos ujAktiv = jatekosok.get(aktualisJatekosIndex);
        alapertelmezettJarmuBeallitasa(ujAktiv);
        kiirSoronLevo(ujAktiv);
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
        System.out.println("valaszt [ID] - Jarmu kivalasztasa / listazas");
        System.out.println("fejcsere [ID] - Kotrofej felszerelese / listazas (csak Takarito)");
        System.out.println("mozgas [SavID] - Mozgás / Elérhető sávok listázása");
        System.out.println("vasarlas [TermekID] - Vásárlás (csak Takarító) / Kínálat listázása");
        System.out.println("allomas [BuszID] - Végállomások listázása (csak Buszvezeto)");
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

        // Alapértelmezett jármű beállítása az aktív játékosnak
        alapertelmezettJarmuBeallitasa(aktivJatekos);
        // Kiírjuk az első játékos típusát és akciópontjait
        kiirSoronLevo(aktivJatekos);

        while (true) {
            alapertelmezettJarmuBeallitasa(aktivJatekos);
            // Kimenet prompttal, hogy lássuk, ki a soron következő játékos
            System.out.print("[" + aktivJatekos.getNev() + "] > ");
            // Bemenet olvasása a konzolról
            String bemenet = scanner.nextLine();
            // Ha a bemenet üres vagy csak szóköz, akkor újra kérjük a parancsot
            if (bemenet == null || bemenet.trim().isEmpty()) continue;

            // A bemenetet szóközök mentén daraboljuk, az első darab lesz a parancs, a többi pedig a paraméterek
            String[] darabok = bemenet.trim().split("\\s+");
            String parancs = darabok[0].toLowerCase();

            // A parancs alapján meghívjuk a megfelelő metódust, vagy végrehajtjuk a logikát
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
                    // Alapértelmezett jármű beállítása az új játékosnak
                    alapertelmezettJarmuBeallitasa(aktivJatekos);

                    System.out.println("SIKERES");
                    kiirSoronLevo(aktivJatekos);
                    break;

                // A fordulóvége parancs lezárja az aktuális játékos teljes fordulóját, és visszaállítja a sorrendet a nulladik játékosra
                case "fordulovege":
                    jatekosok.get(aktualisJatekosIndex).forduloVege(aktivJatekos);
                    aktualisJatekosIndex = 0;
                    aktivJatekos = jatekosok.get(aktualisJatekosIndex);
                    alapertelmezettJarmuBeallitasa(aktivJatekos);
                    System.out.println("SIKERES");
                    kiirSoronLevo(aktivJatekos);
                    break;

                case "teszt":                                                       //Todo: EZT MÉG MEG KELL CSINÁLNI
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

                // A választ parancs lehetővé teszi a játékos számára, hogy kiválassza a fókuszban lévő járművet vagy kotrófejet az ID alapján.
                case "valaszt":
                    if (darabok.length == 1) {
                        // Ha nincs paraméter: csak listázzuk az elérhető járműveket
                        if (aktivJatekos instanceof Takarito) {
                            Takarito t = (Takarito) aktivJatekos;
                            List<Hokotro> hokotrok = t.getIranyitottHokotrok();
                            if (hokotrok.isEmpty()) {
                                System.out.println(">>> Nincs elérhető hókotró.");
                            } else {
                                System.out.println(">>> Elérhető hókotrók:");
                                for (Hokotro h : hokotrok) {
                                    System.out.println("    - " + h.getId()); // ID-k listázása
                                }
                            }
                        } else if (aktivJatekos instanceof Buszvezeto) {
                            Buszvezeto bv = (Buszvezeto) aktivJatekos;
                            List<Busz> buszok = bv.getIranyithatoBuszok();
                            if (buszok.isEmpty()) {
                                System.out.println(">>> Nincs elérhető busz.");
                            } else {
                                System.out.println(">>> Elérhető buszok:");
                                for (Busz b : buszok) {
                                    System.out.println("    - " + b.getId());
                                }
                            }
                        }
                        System.out.println("SIKERES");
                    } else {
                        // Paraméteres eset: konkrét jármű kiválasztása ID alapján
                        boolean talalt = false;
                        String keresettId = darabok[1];

                        if (aktivJatekos instanceof Takarito) {
                            Takarito t = (Takarito) aktivJatekos;

                            // Végigmegyünk az összes irányított hókotrón
                            for (Hokotro h : t.getIranyitottHokotrok()) {
                                if (h.getId().equals(keresettId)) {
                                    aktivJarmu = h; // Kiválasztjuk aktív járműnek
                                    talalt = true;
                                    break;  // Megtaláltuk, nem kell tovább keresni
                                }
                            }
                        } else if (aktivJatekos instanceof Buszvezeto) {
                            Buszvezeto bv = (Buszvezeto) aktivJatekos;

                            for (Busz b : bv.getIranyithatoBuszok()) {
                                if (b.getId().equals(keresettId)) {
                                    aktivJarmu = b;
                                    talalt = true;
                                    break;
                                }
                            }
                        }

                        // Visszajelzés a keresés eredményéről
                        if (talalt) {
                            System.out.println(">>> Jarmu kiválasztva: " + keresettId);
                            System.out.println("SIKERES");
                        } else {
                            System.out.println("HIBAS");    // Nem létező ID vagy nem a játékoshoz tartozik
                        }
                    }
                    break;

                // Fejcsere parancs
                case "fejcsere":
                    // Csak takarító használhatja
                    if (!(aktivJatekos instanceof Takarito)) {
                        System.out.println("ROSSZ JATEKOS");
                        break;
                    }

                    Takarito tak = (Takarito) aktivJatekos;

                    // Paraméter nélkül listázza a saját eszköztárban lévő kotrófejeket
                    if (darabok.length == 1) {
                        tak.getEszkoztar().listazKotroFejek();
                        System.out.println("SIKERES");
                        break;
                    }

                     // Hibás paraméterezés (pl. túl kevés adat)
                    if (darabok.length < 2) {
                        System.out.println("HIBAS");
                        break;
                    }

                    // Csak akkor lehet fejet cserélni, ha egy hókotró van kiválasztva
                    if (!(aktivJarmu instanceof Hokotro)) {
                        System.out.println("HIBAS");
                        break;
                    }

                    Hokotro kivalasztottHokotro = (Hokotro) aktivJarmu;
                    String fejNev = darabok[1];

                    // Fejcsere logika delegálva a Takarito osztálynak
                    if (tak.kotrofejValt(kivalasztottHokotro, fejNev)) {
                        System.out.println("SIKERES");
                        // Akciópont csökkentése és esetleges körváltás
                        aktivJatekos = leptetHaNincsAkcioPont(aktivJatekos);
                    } else {
                        System.out.println("HIBAS"); // Nincs ilyen fej
                    }

                    break;

                // Mozgás parancs eset
                case "mozgas":
                    if (aktivJarmu == null) {
                        // Ha a játékos még nem választott járművet a 'valaszt' paranccsal, akkor nem tud mozogni
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
                                    aktivJatekos.akcioPontKezelo(); // Akciópont levonás
                                    System.out.println("SIKERES");
                                    aktivJatekos = leptetHaNincsAkcioPont(aktivJatekos);
                                } else {
                                    System.out.println("HIBAS");    // Nem elérhető vagy tiltott mozgás
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("HIBAS");    // Nem szám típusú paraméter
                            }
                        }
                    }
                    break;

                // A vásárlás parancs lehetővé teszi a takarító játékos számára, hogy megvásároljon egy terméket a boltból, vagy listázza a kínálatot.
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
                            if (t.getAkcioPont() <= 0) {
                                System.out.println("HIBAS");
                            } else {
                                boolean siker = bolt.vasarlas(t, darabok[1]);
                                if (siker) {
                                    t.akcioPontKezelo();
                                    System.out.println("SIKERES");
                                    aktivJatekos = leptetHaNincsAkcioPont(aktivJatekos);
                                } else {
                                    System.out.println("HIBAS");
                                }
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