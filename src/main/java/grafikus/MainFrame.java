package grafikus;

import javax.swing.*;
import java.awt.*;
import jatekos.Jatekter;

/**
 * A játék fő ablaka. 
 * Tartalmazza a játék során használt grafikus paneleket és koordinálja azok frissítését 
 * a specifikált pull-alapú MVC mechanizmus szerint.
 */
public class MainFrame extends JFrame {

    /** A játéktér kirajzolásáért felelős panel (középen helyezkedik el). */
    private TerkepPanel terkepPanel;

    /** A takarító műveleteit kezelő panel (alul helyezkedik el). */
    private TakaritoPanel takaritoPanel;

    /** A játékos mozgásának vezérléséért felelős panel (jobb oldalon helyezkedik el). */
    private MozgasPanel mozgasPanel;

    /** A játékmodell állapotát és logikáját összefogó objektum referenciája. */
    private Jatekter jatekter;

    /** A képernyő tetején található információs sáv, amely kiírja az aktuális játékost. */
    private JLabel statuszCimke;

    /**
     * A MainFrame konstruktora. 
     * Felépíti az ablak szerkezetét és elhelyezi benne a specifikált három fő panelt.
     *
     * @param jatekter A játék aktuális állapotát tartalmazó modell osztály.
     */
    public MainFrame(Jatekter jatekter) {
        this.jatekter = jatekter;

        // Ablak alapbeállításai
        setTitle("Zúzmaraváros — Játék");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Középre igazítás
        setLayout(new BorderLayout()); // A specifikáció szerinti elrendezés
        getContentPane().setBackground(new Color(236, 240, 241)); // Télies, világosszürke háttér

        // Felső státuszsáv beállítása
        statuszCimke = new JLabel("Inicializálás...");
        statuszCimke.setFont(new Font("Arial", Font.BOLD, 16));
        statuszCimke.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));
        statuszCimke.setForeground(new Color(44, 62, 80));
        add(statuszCimke, BorderLayout.NORTH);

        // Panelek példányosítása és elhelyezése
        terkepPanel = new TerkepPanel(jatekter);
        mozgasPanel = new MozgasPanel(this, jatekter);
        takaritoPanel = new TakaritoPanel(this, jatekter);

        add(terkepPanel, BorderLayout.CENTER);
        add(mozgasPanel, BorderLayout.EAST);
        add(takaritoPanel, BorderLayout.SOUTH);
    }

    /**
     * Frissíti a pálya megjelenítését.
     * A pull-alapú architektúra részeként ez a metódus utasítja a TerkepPanel-t az újrarajzolásra.
     */
    public void terkepFrissites() {
        if (terkepPanel != null) {
            terkepPanel.frissit();
        }
    }

    /**
     * Engedélyezi vagy tiltja a takarító panel elemeit.
     * Ha Buszvezető a soron lévő játékos, a specifikáció szerint a takarító vezérlők letiltásra kerülnek.
     *
     * @param engedelyezve Igaz esetén a panel látható és használható (Takarító), hamis esetén rejtett (Buszvezető).
     */
    public void takaritoPanelEngedelyezes(boolean engedelyezve) {
        if (takaritoPanel != null) {
            takaritoPanel.setVisible(engedelyezve);
        }
    }

    /**
     * Frissíti az aktuális játékos adatait a felületen (pl. körváltáskor).
     *
     * @param jatekosNev Az újonnan aktívvá vált játékos neve és típusa.
     */
    public void aktivJatekosFrissites(String jatekosNev) {
        statuszCimke.setText("Aktuális játékos: " + jatekosNev);
    }

    /**
     * Frissíti a teljes felületet a soron lévő játékos típusának és állapotának megfelelően.
     * Ezt a metódust hívja meg a rendszer minden kör végén (vagy a játék legelején).
     */
    public void korFrissites() {
        if (jatekter == null || jatekter.getJatekosok().isEmpty()) return;
        
        // Lekérjük az aktuális játékost a modellből (feltételezve, hogy a 0. indexszel kezdődik a lista,
        // vagy a Jatekter tartja nyilván az 'aktualisJatekosIndex'-et)
        // A te Jatekter kódod alapján az aktualisJatekosIndex publikus, de elegánsabb ha így kezeljük:
        int aktIndex = jatekter.aktualisJatekosIndex;
        if (aktIndex >= 0 && aktIndex < jatekter.getJatekosok().size()) {
            jatekos.Jatekos aktJatekos = jatekter.getJatekosok().get(aktIndex);
            
            // Felső felirat frissítése
            String szerep = (aktJatekos instanceof jatekos.Takarito) ? " (Takarító)" : " (Buszvezető)";
            aktivJatekosFrissites(aktJatekos.getNev() + szerep);
            
            // Takarító panel (bolt és szerelés) megjelenítése vagy elrejtése a szerepkör alapján
            takaritoPanelEngedelyezes(aktJatekos instanceof jatekos.Takarito);
            
            // Ha takarító, frissítjük a pénzét és a boltját is
            if (aktJatekos instanceof jatekos.Takarito && takaritoPanel != null) {
                takaritoPanel.feluletAdatFrissites();
            }
        }
        
        // Térkép vizuális újrarajzolása
        terkepFrissites();
    }
}
