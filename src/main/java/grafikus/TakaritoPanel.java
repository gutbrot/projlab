package grafikus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import jatekos.Jatekter;
import jatekos.Jatekos;
import jatekos.Takarito;
import jatekos.Buszvezeto;
import jarmu.Hokotro;
import kotrofej.KotroFej;
import bolt.Bolt;
import bolt.FogyoAnyag;
import bolt.IBoltiCikk;
import java.util.List;

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

    private JPanel statuszAlPanel;
    private JPanel boltAlPanel;
    private JPanel szerelesAlPanel;
    private JPanel buszvezetoAlPanel;
    private JLabel pontInfoLabel;

    private CardLayout cardLayout;
    private JPanel cardPanel;

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

        setBackground(PANEL_HATTER);
        setBorder(new EmptyBorder(6, 15, 6, 15));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setBackground(PANEL_HATTER);

        // Takarító nézet: 3 alpanel
        JPanel takaritoPanels = new JPanel(new GridLayout(1, 3, 15, 0));
        takaritoPanels.setBackground(PANEL_HATTER);
        statuszAlPanel = createStatuszAlPanel();
        boltAlPanel = createBoltAlPanel();
        szerelesAlPanel = createSzerelesAlPanel();
        takaritoPanels.add(statuszAlPanel);
        takaritoPanels.add(boltAlPanel);
        takaritoPanels.add(szerelesAlPanel);
        cardPanel.add(takaritoPanels, "takarito");

        // Buszvezető nézet: pontszám kijelző
        buszvezetoAlPanel = createBuszvezetoAlPanel();
        cardPanel.add(buszvezetoAlPanel, "buszvezeto");

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
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
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 4));
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

    private JPanel createBuszvezetoAlPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(PANEL_HATTER);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY), "Buszvezető",
            TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 11), Color.DARK_GRAY));

        pontInfoLabel = new JLabel("Pontszám: 0");
        pontInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
        pontInfoLabel.setForeground(new Color(30, 100, 180));
        panel.add(pontInfoLabel);
        return panel;
    }

    /**
     * Lebonyolítja a vásárlást a kiválasztott termékre a modell üzleti logikáján keresztül.
     * @param termekID A megvásárolni kívánt cikk neve.
     */
    private void vasarlasTranzakcio(String termekID) {
        System.out.println(">>> [TakaritoPanel] Tranzakció indítása: " + termekID);
        Jatekos aktJatekos = jatekter.getAktivJatekos();
        if (!(aktJatekos instanceof Takarito)) return;

        Takarito takarito = (Takarito) aktJatekos;
        if (takarito.getAkcioPont() <= 0) {
            System.out.println(">>> [TakaritoPanel] Nincs elég akciópont!");
            return;
        }

        // Ha fogyóanyagot vásárol és több hókotrója is van, popup segít kiválasztani
        IBoltiCikk cikk = jatekter.getBolt().getKinalat().get(termekID);
        if (cikk instanceof FogyoAnyag && takarito.getIranyitottHokotrok().size() > 1) {
            Hokotro kivalasztott = hokotroValasztasDialog(takarito);
            if (kivalasztott == null) return; // felhasználó megszakította
            takarito.aktivJarmu = kivalasztott;
        }

        // Hokotró vásárlásnál jegyezzük meg az előtti méretet
        int hokotrokElotte = takarito.getIranyitottHokotrok().size();

        boolean siker = jatekter.getBolt().vasarlas(takarito, termekID);
        if (siker) {
            takarito.akcioPontKezelo();

            // Ha hókotrót vett, az újonnan létrehozott példányt regisztrálni kell a játéktérre
            if (termekID.equals("Hokotro")) {
                List<Hokotro> hokotrok = takarito.getIranyitottHokotrok();
                if (hokotrok.size() > hokotrokElotte) {
                    jatekter.hozzaadJarmu(hokotrok.get(hokotrok.size() - 1));
                }
            }
        }
        System.out.println(">>> [TakaritoPanel] Tranzakció: " + (siker ? "SIKERES" : "SIKERTELEN"));

        jatekter.autoKorvaltas();
        feluletAdatFrissites();
        mainFrame.korFrissites();
    }

    /**
     * Popup dialog, amellyel a játékos kiválaszthatja, melyik hókotrójához rendelje a fogyóanyagot.
     * @return A kiválasztott Hokotro, vagy null ha a felhasználó bezárta az ablakot.
     */
    private Hokotro hokotroValasztasDialog(Takarito takarito) {
        List<Hokotro> hokotrok = takarito.getIranyitottHokotrok();
        String[] options = new String[hokotrok.size()];
        for (int i = 0; i < hokotrok.size(); i++) {
            options[i] = hokotrok.get(i).getId();
        }
        String kivalasztott = (String) JOptionPane.showInputDialog(
            this,
            "Melyik hókotróhoz rendeljük a fogyóanyagot?",
            "Hókotró kiválasztása",
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]
        );
        if (kivalasztott == null) return null;
        for (Hokotro h : hokotrok) {
            if (h.getId().equals(kivalasztott)) return h;
        }
        return null;
    }

    /**
     * Lecseréli a hókotrón lévő fejet a játékos által kiválasztott raktári darabra.
     * @param fejTipus A felszerelni kívánt új kotrófej osztályneve.
     */
    private void fejCsereVégrehajtas(String fejTipus) {
        System.out.println(">>> [TakaritoPanel] Fejcsere indítása: " + fejTipus);
        Jatekos aktJatekos = jatekter.getAktivJatekos();
        if (!(aktJatekos instanceof Takarito)) return;

        Takarito takarito = (Takarito) aktJatekos;
        List<Hokotro> hokotrok = takarito.getIranyitottHokotrok();
        if (hokotrok.isEmpty()) return;

        Hokotro hokotro;
        if (hokotrok.size() == 1) {
            hokotro = hokotrok.get(0);
        } else {
            hokotro = hokotroValasztasDialog(takarito);
            if (hokotro == null) return;
        }

        boolean siker = takarito.kotrofejValt(hokotro, fejTipus);
        System.out.println(">>> [TakaritoPanel] Fejcsere: " + (siker ? "SIKERES" : "SIKERTELEN"));

        jatekter.autoKorvaltas();
        feluletAdatFrissites();
        mainFrame.korFrissites();
    }

    /**
     * Pull-alapú adatolvasással lekéri a modellből az aktuális Takarító értékeit,
     * és naprakészen tartja a gombokat, feliratokat, legördülő listákat.
     */
    public void feluletAdatFrissites() {
        if (jatekter == null) return;

        Jatekos aktJatekos = jatekter.getAktivJatekos();

        if (aktJatekos instanceof Buszvezeto) {
            Buszvezeto bv = (Buszvezeto) aktJatekos;
            pontInfoLabel.setText("Pontszám: " + bv.getPont());
            cardLayout.show(cardPanel, "buszvezeto");
            return;
        }

        cardLayout.show(cardPanel, "takarito");
        boolean isTakarito = aktJatekos instanceof Takarito;
        if (!isTakarito) return;

        Takarito takarito = (Takarito) aktJatekos;
        penzInfoLabel.setText("Egyenleg: " + takarito.getPenz() + " pénz");

        if (!takarito.getIranyitottHokotrok().isEmpty()) {
            Hokotro h = takarito.getIranyitottHokotrok().get(0);
            KotroFej fej = h.getFelszereltFej();
            aktivFejInfoLabel.setText("Aktív eszköz: " + (fej != null ? fej.getClass().getSimpleName() : "Nincs"));
        } else {
            aktivFejInfoLabel.setText("Aktív eszköz: Nincs hókotró");
        }

        raktarFejekDoboz.removeAllItems();
        for (KotroFej fej : takarito.getEszkoztar().getKotroFejek()) {
            raktarFejekDoboz.addItem(fej.getClass().getSimpleName());
        }
        felszerelesGomb.setEnabled(raktarFejekDoboz.getItemCount() > 0);
    }
}