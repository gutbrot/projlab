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
import jarmu.Hokotro;
import terkep.Terkep;
import terkep.TerkepLoader;

/**
 * A játék indulás előtti konfigurációs ablaka. 
 * A játékosok létrehozását, a szerepkörök kiválasztását és a pálya betöltését kezeli.
 */
public class MenuFrame extends JFrame {

    /**
     * A játékos szerepkörének kiválasztására szolgáló gombcsoport (Takarító vs. Buszvezető).
     * Bár a specifikáció egy JToggleButton-t ír, a UI terven kettő látszik, így ButtonGroup-pal oldjuk meg.
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
     * A MenuFrame konstruktora. 
     * Felépíti a grafikus felületet, beállítja a színeket, elrendezéseket és az eseménykezelőket.
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
     * Létrehozza az ablak felső részén található cím panelt.
     * @return A formázott címet tartalmazó JPanel.
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
     * Létrehozza a bal oldali panelt, amely a játékosok felvételéért és listázásáért felel.
     * @return A játékos hozzáadó UI elemeket tartalmazó JPanel.
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
     * Létrehozza a jobb oldali panelt a fő vezérlőgombokkal (Start, Térkép, Kilépés).
     * @return A vezérlőgombokat tartalmazó JPanel.
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

        // Térképválasztó ComboBox feltöltése alapértelmezett fájlokkal
        String[] terkepek = {"teszt_terkep.xml", "teszt_terkep_penz.xml"};
        terkepValaszto = new JComboBox<>(terkepek);
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
     * Új játékost hoz létre a megadott névvel és a kiválasztott szerepkörrel.
     * Frissíti az adatmodellt és a grafikus listát.
     * @param nev A játékos neve, amit a szövegmezőből olvasunk ki.
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
     * Betölti a kiválasztott pályát az XML fájlból a meglévő modell (TerkepLoader) segítségével.
     * @param terkepFile A betöltendő XML fájl neve (pl. "teszt_terkep.xml").
     * @return Az inicializált Jatekter objektum, vagy null hiba esetén.
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
        return ujJatekter;
    }

    /**
     * Ellenőrzi, hogy megvan-e a játék indításához szükséges minimum feltétel 
     * (1 takarító és 1 buszvezető), és ennek megfelelően kapcsolja a Start gombot.
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
     * Összeköti a felvett játékosokat a betöltött Játéktérrel, majd átadja a vezérlést
     * a főablaknak (MainFrame).
     * @param jatekter Az inicializált Játéktér objektum a betöltött térképpel.
     */
    private void jatekInditasa(Jatekter jatekter) {
        // A felvett játékosokat bevezetjük a Játéktérbe
        int hokotroCounter = 1;
        for (Jatekos j : ideiglenesJatekosok) {
            jatekter.hozzaadJatekos(j);
            
            // Ha takarító, adunk neki egy kezdő hókotrót, hogy legyen mivel mozognia
            if (j instanceof Takarito) {
                Takarito t = (Takarito) j;
                Hokotro h = new Hokotro("Hokotro_" + hokotroCounter++, jatekter.getTerkep(), null);
                t.hozzaadHokotro(h);
                jatekter.hozzaadJarmu(h);
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
