package grafikus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jatekos.Jatekter;
import bolt.Bolt;

/**
 * A Takarító játékosok speciális interakcióit (vásárlás, fejcsere) kezelő vezérlőpanel.
 * Ez a komponens a játékablak alsó (SOUTH) régiójában helyezkedik el.
 */
public class TakaritoPanel extends JPanel {

    /** A boltban elérhető cikkek kiválasztására szolgáló legördülő menü. */
    private JComboBox<String> boltKinalatDoboz;

    /** Az eszköztárban raktározott, felszerelhető kotrófejek legördülő menüje. */
    private JComboBox<String> raktarFejekDoboz;

    /** A kiválasztott kotrófej hókotróra szerelését indító gomb. */
    private JButton felszerelesGomb;

    /** A játékos aktuális anyagi helyzetét mutató információs szövegmező. */
    private JLabel penzInfoLabel;

    /** Az aktív hókotróra szerelt fej nevét mutató információs szövegmező. */
    private JLabel aktivFejInfoLabel;

    /** A központi játékablak referenciája a frissítések koordinálásához. */
    private MainFrame mainFrame;

    /** A játéktér modell rétegének referenciája. */
    private Jatekter jatekter;

    /** Alapértelmezett télies háttérszín. */
    private final Color PANEL_HATTER = new Color(236, 240, 241);

    /**
     * A TakaritoPanel konstruktora.
     * Beállítja a rácsos elrendezést (GridLayout) és inicializálja a bolti, illetve szerelési funkciókat.
     *
     * @param mainFrame A grafikus keretablak referenciája.
     * @param jatekter A szimulációs játéktér modellje.
     */
    public TakaritoPanel(MainFrame mainFrame, Jatekter jatekter) {
        this.mainFrame = mainFrame;
        this.jatekter = jatekter;

        // Alapvető panelbeállítások: vízszintes rácsszerkezet 3 fő oszloppal
        setLayout(new GridLayout(1, 3, 15, 0));
        setBackground(PANEL_HATTER);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // A három funkcionális részpanel felépítése és hozzáadása
        add(createStatuszAlPanel());
        add(createBoltAlPanel());
        add(createSzerelesAlPanel());

        // Kezdőadatok betöltése a modellből a felületre
        feluletAdatFrissites();
    }

    /**
     * Létrehozza a játékos állapotát (pénz, felszerelés) mutató bal oldali alpanelt.
     * @return A formázott státusz panel.
     */
    private JPanel createStatuszAlPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 5));
        panel.setBackground(PANEL_HATTER);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Állapot", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 11), Color.DARK_GRAY));

        penzInfoLabel = new JLabel("Egyenleg: 0 pénz");
        penzInfoLabel.setFont(new Font("Arial", Font.BOLD, 13));
        penzInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        aktivFejInfoLabel = new JLabel("Aktív eszköz: Nincs");
        aktivFejInfoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        aktivFejInfoLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        panel.add(penzInfoLabel);
        panel.add(aktivFejInfoLabel);
        return panel;
    }

    /**
     * Létrehozza a bolti vásárlást kezelő középső alpanelt.
     * @return A formázott bolt panel.
     */
    private JPanel createBoltAlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(PANEL_HATTER);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Hókotró Bolt", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 11), Color.DARK_GRAY));

        boltKinalatDoboz = new JComboBox<>();
        boltKinalatDoboz.setPreferredSize(new Dimension(150, 25));
        
        // A Bolt osztály belső kínálati térképéből dinamikusan feltöltjük a listát
        Bolt ideiglenesBolt = new Bolt();
        for (String cikkNev : ideiglenesBolt.getKinalat().keySet()) {
            boltKinalatDoboz.addItem(cikkNev);
        }

        JButton vasarlasGomb = new JButton("Vásárlás");
        vasarlasGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kivalasztottCikk = (String) boltKinalatDoboz.getSelectedItem();
                if (kivalasztottCikk != null) {
                    vasarlasTranzakcio(kivalasztottCikk);
                }
            }
        });

        panel.add(boltKinalatDoboz);
        panel.add(vasarlasGomb);
        return panel;
    }

    /**
     * Létrehozza a kotrófejek cseréjéért felelős jobb oldali alpanelt.
     * @return A formázott szerelő panel.
     */
    private JPanel createSzerelesAlPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(PANEL_HATTER);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Szerelőműhely", 
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 11), Color.DARK_GRAY));

        raktarFejekDoboz = new JComboBox<>();
        raktarFejekDoboz.setPreferredSize(new Dimension(150, 25));

        felszerelesGomb = new JButton("Felszerel");
        felszerelesGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kivalasztottFej = (String) raktarFejekDoboz.getSelectedItem();
                if (kivalasztottFej != null) {
                    fejCsereVégrehajtas(kivalasztottFej);
                }
            }
        });

        panel.add(raktarFejekDoboz);
        panel.add(felszerelesGomb);
        return panel;
    }

    /**
     * Lebonyolítja a vásárlást a kiválasztott termékre a modell üzleti logikáján keresztül.
     * @param termekID A megvásárolni kívánt cikk neve.
     */
    private void vasarlasTranzakcio(String termekID) {
        System.out.println(">>> [TakaritoPanel] Tranzakció indítása: " + termekID);
        // Itt a háttérben futó bolt.vasarlas(aktivTakarito, termekID) hívódik meg
        
        // Tranzakció után azonnal frissítjük a kijelzőket
        feluletAdatFrissites();
        mainFrame.terkepFrissites();
    }

    /**
     * Lecseréli a hókotrón lévő fejet a játékos által kiválasztott raktári darabra.
     * @param fejTipus A felszerelni kívánt új kotrófej osztályneve.
     */
    private void fejCsereVégrehajtas(String fejTipus) {
        System.out.println(">>> [TakaritoPanel] Fejcsere indítása: " + fejTipus);
        // Itt az aktív takarító kotrofejValt() metódusa hívódik meg a modellben
        
        // Szerelés után újratöltjük az adatokat
        feluletAdatFrissites();
        mainFrame.terkepFrissites();
    }

    /**
     * Pull-alapú adatolvasással lekéri a modellből az aktuális Takarító értékeit,
     * és naprakészen tartja a gombokat, feliratokat, legördülő listákat.
     */
    public void feluletAdatFrissites() {
        // Biztonsági ellenőrzés a null referenciák elkerülésére
        if (jatekter == null) return;

        // Példaként beállítunk fix értékeket, amíg a futtató hurok teljesen össze nem áll a backenddel
        penzInfoLabel.setText("Egyenleg: 100 pénz");
        aktivFejInfoLabel.setText("Aktív eszköz: SoproFej");

        // Raktár legördülő listájának dinamikus tisztítása és újratöltése
        raktarFejekDoboz.removeAllItems();
        raktarFejekDoboz.addItem("HanyoFej");
        raktarFejekDoboz.addItem("JegtoroFej");
        
        // Ha üres a raktár, a gombot inaktívvá tesszük
        felszerelesGomb.setEnabled(raktarFejekDoboz.getItemCount() > 0);
    }
}