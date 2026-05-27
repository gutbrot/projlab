package grafikus;

import javax.swing.*;
import java.awt.*;
import jatekos.Jatekter;

/**
 * A főjáték ablak, ahol minden megjelenik: középen a térkép, jobbra a mozgásvezérlő,
 * alul a takarítós/buszvezétős panel. A korFrissites() metóduson keresztül
 * frissíti az összes részét minden lépés vagy körváltás után.
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
     * Összerakja a főablakot: beállítja a méretét, elhelyezi a három fő panelt
     * (térkép középen, mozgásvezérlő jobbra, alsó sáv lent), majd elindítja az első kör frissítését.
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
     * Beírja az aktuális játékos nevét és szerepkörét a felső feliratba.
     */
    public void aktivJatekosFrissites(String szoveg) {
        if (statuszCimke != null) {
            statuszCimke.setText("Aktuális játékos: " + szoveg);
        }
    }

    /**
     * Minden lépés vagy körváltás után ez frissíti a teljes felületet:
     * beírja az aktuális játékos nevét, frissíti az alsó panelt és a mozgáspanelt,
     * majd újrarajzolja a térképet.
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

            // Az alsó panel mindig látható, feluletAdatFrissites() kezeli a nem-Takarító esetet
            takaritoPanel.feluletAdatFrissites();

            // Mozgás panel frissítése
            mozgasPanel.frissit();
        }
        
        // Térkép újrarajzolása a megváltozott koordinátákkal
        terkepPanel.jarmuGrafikusObjektumokFrissitese();
        terkepPanel.repaint();
    }
}