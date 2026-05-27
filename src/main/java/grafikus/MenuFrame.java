package grafikus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import jatekos.Buszvezeto;
import jatekos.Jatekos;
import jatekos.Jatekter;
import jatekos.Takarito;
import jarmu.Busz;
import jarmu.Hokotro;
import kotrofej.SoproFej;
import terkep.Lokacio;
import terkep.Terkep;
import terkep.TerkepLoader;

/**
 * Ez az ablak jelenik meg először, amikor elindítjuk a játékot.
 * Ide lehet beírni a játékosok nevét, kiválasztani a szerepkörüket (Takarító vagy Buszvezető),
 * megadni a betöltendő pályát, majd a Start gombbal elindítani a játékot.
 */
public class MenuFrame extends JFrame {

    /**
     * A két szerepkör-választó gomb (Takarító / Buszvezető) össze van kötve ebbe a csoportba,
     * hogy egyszerre csak az egyik legyen bekapcsolva.
     */
    private ButtonGroup jatekosValaszto;

    /** A Takarító szerepkört kiválasztó rádiógomb jellegű kapcsoló. */
    private JToggleButton takaritoToggle;

    /** A Buszvezető szerepkört kiválasztó rádiógomb jellegű kapcsoló. */
    private JToggleButton buszvezetoToggle;

    /** A kiválasztható pályák (XML fájlok) listája. */
    private JComboBox<String> terkepValaszto;

    /** Új játékos hozzáadását indító gomb. */
    private JButton jatekosHozzaadGomb;

    /** A játék indításáért felelős gomb. Csak megfelelő feltételek mellett aktív. */
    private JButton startGomb;

    /** A játékos nevének megadására szolgáló beviteli mező. */
    private JTextField jatekosNev;

    /** A hozzáadott játékosokat megjelenítő lista a felületen. */
    private DefaultListModel<String> hozzaadottJatekosokModel;

    /** A hozzáadott játékosok listáját vizuálisan megjelenítő Swing komponens. */
    private JList<String> hozzaadottJatekosokLista;

    /** A háttérben tárolt, eddig felvett játékosok listája. */
    private List<Jatekos> ideiglenesJatekosok;

    /** Télies, hideg szürke háttérszín a specifikáció alapján. */
    private final Color HATTTER_SZIN = new Color(236, 240, 241);

    /** Narancssárga kiemelő szín a fő gombokhoz. */
    private final Color NARANCS_SZIN = new Color(230, 126, 34);

    /** Sötétebb szürke szín a másodlagos gombokhoz (pl. Kilépés). */
    private final Color SZURKE_GOMB_SZIN = new Color(127, 140, 141);

    /**
     * Felépíti a menüablakot: összerakja a fejlécet, a játékos-felvevő bal panelt
     * és a jobb oldali indítógombokat, majd beállítja a gombok kezdeti állapotát.
     */
    public MenuFrame() {
        // Ablak alapvető beállításai
        setTitle("Zúzmaraváros — Főmenü");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Középre igazítás a képernyőn
        setResizable(false); // Fix méret a stabil dizájn érdekében
        
        // Inicializáljuk a belső adatstruktúrákat
        ideiglenesJatekosok = new ArrayList<>();
        hozzaadottJatekosokModel = new DefaultListModel<>();

        // Fő panel létrehozása és színezése
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(HATTTER_SZIN);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // UI elemek felépítése delegált metódusokkal a tisztább kód érdekében
        mainPanel.add(cimPanelLetrehozasa(), BorderLayout.NORTH);
        mainPanel.add(balPanelLetrehozasa(), BorderLayout.WEST);
        mainPanel.add(jobbPanelLetrehozasa(), BorderLayout.CENTER);

        // A kész főpanelt hozzáadjuk az ablakhoz
        add(mainPanel);
        
        // Frissítjük a gombok állapotát induláskor (pl. a Start gomb tiltása)
        gombokAllapotFrissitese();
    }

    /**
     * A fejléc panel az ablak tetején: nagy betűkkel a játék neve,
     * alatta kisebb dőlt betűkkel a csapatnév.
     */
    private JPanel cimPanelLetrehozasa() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.setBackground(HATTTER_SZIN);

        // Főcím beállítása nagy betűmérettel
        JLabel cimLabel = new JLabel("Zúzmaraváros");
        cimLabel.setFont(new Font("Arial", Font.BOLD, 32));
        cimLabel.setForeground(new Color(44, 62, 80)); // Sötétkék/szürke szövegszín

        // Alcím / Csapatnév beállítása dőlt betűkkel
        JLabel alcimLabel = new JLabel("wíwíwí");
        alcimLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        alcimLabel.setForeground(new Color(149, 165, 166));

        panel.add(cimLabel);
        panel.add(alcimLabel);
        
        return panel;
    }

    /**
     * A bal oldali panel, ahol a játékosokat lehet felvenni.
     * Ide kell beírni a nevet, kiválasztani a szerepkört, majd a Hozzáad gombbal
     * rögzíteni — az eddig felvett játékosok listája is itt látható.
     */
    private JPanel balPanelLetrehozasa() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(HATTTER_SZIN);
        panel.setPreferredSize(new Dimension(350, 0));

        // Címke a játékos hozzáadásához
        JLabel hozzaadasLabel = new JLabel("JÁTÉKOS HOZZÁADÁSA");
        hozzaadasLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(hozzaadasLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Szerepkör választó gombok beállítása
        JPanel togglePanel = new JPanel(new GridLayout(1, 2));
        takaritoToggle = new JToggleButton("Takarító", true); // Alapértelmezetten kiválasztva
        buszvezetoToggle = new JToggleButton("Buszvezető");
        
        // Csoportba foglaljuk őket, hogy csak egy lehessen aktív egyszerre
        jatekosValaszto = new ButtonGroup();
        jatekosValaszto.add(takaritoToggle);
        jatekosValaszto.add(buszvezetoToggle);
        
        togglePanel.add(takaritoToggle);
        togglePanel.add(buszvezetoToggle);
        panel.add(togglePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Név megadó mező beállítása
        jatekosNev = new JTextField();
        jatekosNev.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        panel.add(jatekosNev);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Hozzáadás gomb és annak eseménykezelője
        jatekosHozzaadGomb = new JButton("Hozzáad");
        jatekosHozzaadGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nev = jatekosNev.getText().trim();
                // Csak akkor adunk hozzá játékost, ha nem üres a név mező
                if (!nev.isEmpty()) {
                    jatekosHozzaad(nev);
                    jatekosNev.setText(""); // Mező ürítése sikeres hozzáadás után
                }
            }
        });
        panel.add(jatekosHozzaadGomb);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Hozzáadott játékosok listájának címe
        JLabel listaLabel = new JLabel("HOZZÁADOTT JÁTÉKOSOK");
        listaLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(listaLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Lista vizuális beállítása gördítősávval
        hozzaadottJatekosokLista = new JList<>(hozzaadottJatekosokModel);
        JScrollPane scrollPane = new JScrollPane(hozzaadottJatekosokLista);
        panel.add(scrollPane);

        return panel;
    }

    /**
     * A jobb oldali panel a főbb vezérlőkkel: pályaválasztó legördülő lista,
     * Start gomb a játék elindításához, és Kilépés gomb az alkalmazás bezárásához.
     */
    private JPanel jobbPanelLetrehozasa() {
        JPanel panel = new JPanel(new GridLayout(4, 1, 0, 15));
        panel.setBackground(HATTTER_SZIN);
        panel.setBorder(new EmptyBorder(30, 20, 0, 0));

        // Start gomb beállítása narancssárga színnel (a specifikáció dizájnja alapján)
        startGomb = new JButton("Start");
        startGomb.setBackground(NARANCS_SZIN);
        startGomb.setForeground(Color.WHITE);
        startGomb.setFont(new Font("Arial", Font.BOLD, 18));
        startGomb.setFocusPainted(false);
        startGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ha a felhasználó rákattint, betöltjük a térképet és indítjuk a játékot
                String valasztottTerkep = (String) terkepValaszto.getSelectedItem();
                Jatekter jatekter = terkepBetoltes(valasztottTerkep);
                
                if (jatekter != null) {
                    jatekInditasa(jatekter);
                }
            }
        });
        panel.add(startGomb);

        // Térképválasztó ComboBox dinamikus feltöltése a Betoltes/ mappából
        terkepValaszto = new JComboBox<>();
        java.io.File betoltesDir = new java.io.File("Betoltes");
        if (betoltesDir.exists() && betoltesDir.isDirectory()) {
            java.io.File[] xmlFajlok = betoltesDir.listFiles((dir, name) ->
                name.endsWith(".xml") && !name.contains("_kimenet") && !name.contains("_elvart"));
            if (xmlFajlok != null) {
                java.util.Arrays.sort(xmlFajlok);
                for (java.io.File f : xmlFajlok) {
                    terkepValaszto.addItem(f.getName());
                }
            }
        }
        if (terkepValaszto.getItemCount() == 0) {
            terkepValaszto.addItem("uj_teszt_terkep.xml");
        }
        terkepValaszto.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(terkepValaszto);

        // Kilépés gomb sötétszürke színnel
        JButton kilepesGomb = new JButton("Kilépés");
        kilepesGomb.setBackground(SZURKE_GOMB_SZIN);
        kilepesGomb.setForeground(Color.WHITE);
        kilepesGomb.setFont(new Font("Arial", Font.BOLD, 18));
        kilepesGomb.setFocusPainted(false);
        kilepesGomb.addActionListener(e -> System.exit(0)); // Alkalmazás bezárása
        panel.add(kilepesGomb);

        // Információs szöveg a Start gomb feltételeiről
        JLabel infoLabel = new JLabel("<html>A Start csak akkor aktív, ha legalább egy takarító és egy buszvezető is hozzá lett adva.</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        infoLabel.setForeground(Color.DARK_GRAY);
        panel.add(infoLabel);

        return panel;
    }

    /**
     * Felvesz egy új játékost a megadott névvel és a kiválasztott szerepkörrel,
     * majd rögtön frissíti a listát és ellenőrzi, hogy a Start gomb aktívvá válhat-e.
     */
    public void jatekosHozzaad(String nev) {
        Jatekos ujJatekos;
        String szerepkorSzoveg;

        // Eldöntjük a Toggle gombok alapján, hogy milyen objektumot példányosítsunk
        if (takaritoToggle.isSelected()) {
            ujJatekos = new Takarito(3);
            ujJatekos.setNev(nev);
            szerepkorSzoveg = "Takarító";
        } else {
            ujJatekos = new Buszvezeto(3);
            ujJatekos.setNev(nev);
            szerepkorSzoveg = "Buszvezető";
        }

        // Hozzáadjuk a belső listához
        ideiglenesJatekosok.add(ujJatekos);
        
        // Frissítjük a UI listát (pl. "Kovács Béla - Takarító")
        hozzaadottJatekosokModel.addElement(nev + " — " + szerepkorSzoveg);
        
        // Minden hozzáadás után ellenőrizzük, aktiválható-e a Start gomb
        gombokAllapotFrissitese();
    }

    /**
     * Beolvassa a megadott nevű XML fájlból a pályát és felépíti a Játékteret.
     * Ha valami hiba van (pl. hiányzik a fájl, üres a hálózat), hibaüzenet jelenik meg
     * és null értékkel tér vissza.
     */
    public Jatekter terkepBetoltes(String terkepFile) {
        // A TerkepLoader betölti az úthálózatot
        Terkep betoltottTerkep = TerkepLoader.betolt(terkepFile);
        
        if (betoltottTerkep == null || betoltottTerkep.getTeljesHalozat().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Hiba a térkép betöltésekor!", "Hiba", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        // Beállítjuk a statikus globális térképet a Hókotróknak a bolt/vásárlás miatt
        Hokotro.setGlobalTerkep(betoltottTerkep);

        // Létrehozzuk az új Játékteret a betöltött térképpel
        Jatekter ujJatekter = new Jatekter(betoltottTerkep);

        // NPC autók betöltése az XML-ből (ha vannak)
        new seged.Betolteskezelo().betoltNpcAutok(terkepFile, betoltottTerkep, ujJatekter);

        return ujJatekter;
    }

    /**
     * Megnézi, hogy a felvett játékosok közt van-e legalább egy takarító és egy buszvezető.
     * Ha igen, a Start gomb aktív lesz; ha nem, szürkén marad és nem kattintható.
     */
    private void gombokAllapotFrissitese() {
        boolean vanTakarito = false;
        boolean vanBuszvezeto = false;

        // Végigiterálunk a felvett játékosokon, hogy megnézzük a típusukat
        for (Jatekos j : ideiglenesJatekosok) {
            if (j instanceof Takarito) vanTakarito = true;
            if (j instanceof Buszvezeto) vanBuszvezeto = true;
        }

        // Csak akkor kattintható a Start, ha mindkét feltétel teljesül
        startGomb.setEnabled(vanTakarito && vanBuszvezeto);
        
        // Ha inaktív, szürkébb színt adunk neki a vizuális visszajelzéshez
        if (startGomb.isEnabled()) {
            startGomb.setBackground(NARANCS_SZIN);
        } else {
            startGomb.setBackground(new Color(243, 156, 18)); // Halványabb narancs
        }
    }

    /**
     * Összeköti a játékosokat a betöltött pályával, majd kinyitja a főjáték ablakot.
     * Minden takarítóhoz létrehoz egy kezdő hókotrót; minden buszvezető kap
     * egy véletlenszerű A–B végállomású buszt. A menüablak ezután bezárul.
     */
    private void jatekInditasa(Jatekter jatekter) {
        // A felvett játékosokat bevezetjük a Játéktérbe
        for (Jatekos j : ideiglenesJatekosok) {
            jatekter.hozzaadJatekos(j);

            // Ha takarító, adunk neki egy kezdő hókotrót, hogy legyen mivel mozognia
            if (j instanceof Takarito) {
                Takarito t = (Takarito) j;
                Hokotro h = new Hokotro(Hokotro.kovetkezoId(), jatekter.getTerkep(), new SoproFej(30));
                t.hozzaadHokotro(h);
                jatekter.hozzaadJarmu(h);
            } else if (j instanceof Buszvezeto) {
                Buszvezeto bv = (Buszvezeto) j;
                Lokacio lokA = jatekter.getTerkep().getRandomLokacio();
                Lokacio lokB = jatekter.getTerkep().getRandomLokacio();
                // Biztosítjuk, hogy A és B különböző cellák legyenek
                if (lokA != null && lokB != null && lokA.getSav() == lokB.getSav()) {
                    lokB = jatekter.getTerkep().getRandomLokacio();
                }
                Busz b = new Busz(Busz.kovetkezoId(), lokA, lokA, lokB);
                b.setVezeto(bv);
                bv.hozzaadBusz(b);
                jatekter.hozzaadJarmu(b);
            }
        }

        // A MainFrame példányosítása és az alkalmazás szintű regisztrációja
        MainFrame mainFrame = new MainFrame(jatekter);
        GraphicsApp.getInstance().setMainFrame(mainFrame);
        
        // Első kör inicializálása a felületen
        mainFrame.korFrissites();
        
        // Főablak megjelenítése
        mainFrame.setVisible(true);
        
        // Ezt a menü ablakot bezárjuk, mert a játék elindult
        this.dispose();
        
        System.out.println(">>> [MenuFrame] Játék inicializálva, átlépés a játéktérbe...");
    }
}
