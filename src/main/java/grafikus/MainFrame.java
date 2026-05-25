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
        
        setTitle("Hókotró Szimulátor");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Felső státusz sáv (Aktuális játékos kiírása)
        statuszCimke = new JLabel("Aktuális játékos: Betöltés...", SwingConstants.CENTER);
        statuszCimke.setFont(new Font("Arial", Font.BOLD, 16));
        statuszCimke.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statuszCimke, BorderLayout.NORTH);

        // Panelek inicializálása - csak létrehozzuk őket, még nem frissítünk!
        terkepPanel = new TerkepPanel(jatekter);
        mozgasPanel = new MozgasPanel(this, jatekter);
        //takaritoPanel = new TakaritoPanel(jatekter, this);
        takaritoPanel = new TakaritoPanel(this, jatekter);

        // Középső játéktér panel
        JScrollPane scrollPane = new JScrollPane(terkepPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollPane, BorderLayout.CENTER);

        // Jobb oldali panel (Mozgás vezérlő)
        add(mozgasPanel, BorderLayout.EAST);

        // Alsó panel (Takarító eszközök / Vásárlás)
        add(takaritoPanel, BorderLayout.SOUTH);

        // Az ablak megjelenítése
        setVisible(true);

        // FONTOS JAVÍTÁS: Az első kör frissítését az eseménykezelő szálra bízzuk,
        // így biztosan lefut a komponensek teljes betöltése (elkerülve a NullPointerException-t).
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                korFrissites();
            }
        });
    }

    /**
     * Frissíti a fejlécben megjelenő szöveget.
     * @param szoveg Az új megjelenítendő szöveg.
     */
    public void aktivJatekosFrissites(String szoveg) {
        if (statuszCimke != null) {
            statuszCimke.setText("Aktuális játékos: " + szoveg);
        }
    }

    /**
     * Engedélyezi vagy letiltja a takarítóhoz tartozó specifikus vezérlőpanelt.
     * @param engedelyezett true esetén aktív, false esetén inaktív lesz.
     */
    public void takaritoPanelEngedelyezes(boolean engedelyezett) {
        if (takaritoPanel != null) {
            takaritoPanel.setVisible(engedelyezett);
        }
    }

    /**
     * A globális körfrissítő metódus.
     * Kigyűjti a modellből a legújabb állapotokat, és szétküldi azokat a megfelelő paneleknek.
     */
    public void korFrissites() {
        // Biztonsági ellenőrzések: ha valami még null, nem futtatjuk le a frissítést
        if (jatekter == null || jatekter.getJatekosok().isEmpty()) return;
        if (mozgasPanel == null || takaritoPanel == null || terkepPanel == null) return;
        
        // Lekérjük az aktuális játékost a modellből
        int aktIndex = jatekter.aktualisJatekosIndex;
        if (aktIndex >= 0 && aktIndex < jatekter.getJatekosok().size()) {
            jatekos.Jatekos aktJatekos = jatekter.getJatekosok().get(aktIndex);
            
            // Felső felirat frissítése
            String nev = aktJatekos.getNev() != null ? aktJatekos.getNev() : "Ismeretlen";
            String szerep = (aktJatekos instanceof jatekos.Takarito) ? " (Takarító)" : " (Buszvezető)";
            aktivJatekosFrissites(nev + szerep);
            
            // Takarító panel (bolt és szerelés) megjelenítése vagy elrejtése a szerepkör alapján
            boolean isTakarito = (aktJatekos instanceof jatekos.Takarito);
            takaritoPanelEngedelyezes(isTakarito);
            
            // Ha takarító, frissítjük a pénzét és a boltját is
            if (isTakarito) {
                takaritoPanel.feluletAdatFrissites();
            }
            
            // Mozgás panel frissítése
            mozgasPanel.frissit();
        }
        
        // Térkép újrarajzolása a megváltozott koordinátákkal
        terkepPanel.jarmuGrafikusObjektumokFrissitese();
        terkepPanel.repaint();
    }
}