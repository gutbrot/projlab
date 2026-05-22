package grafikus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import jatekos.Jatekter;
import jatekos.Jatekos;
import jatekos.Takarito;
import jatekos.Buszvezeto;
import jarmu.Jarmu;
import jarmu.Hokotro;
import jarmu.Busz;
import terkep.Lokacio;
import terkep.Sav;

/**
 * A játékos mozgásának vezérléséért felelős panel.
 * Lehetővé teszi jármű kiválasztását, a célsáv megadását és a kör befejezését.
 */
public class MozgasPanel extends JPanel {

    private JComboBox<String> jarmuValaszto;
    private JComboBox<String> savValaszto;
    private JButton mozgasGomb;
    private JButton korVegeGomb;
    private MainFrame mainFrame;
    private Jatekter jatekter;
    private final Color HATTER_SZIN = new Color(236, 240, 241);

    /** Az aktuális játékos járműveinek listája (párhuzamos a jarmuValaszto elemekkel). */
    private List<Jarmu> aktualisJarmuvek = new ArrayList<>();
    /** Az aktuálisan kiválasztott jármű elérhető sávjainak listája. */
    private List<Sav> elerhetoSavok = new ArrayList<>();

    public MozgasPanel(MainFrame mainFrame, Jatekter jatekter) {
        this.mainFrame = mainFrame;
        this.jatekter = jatekter;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 0));
        setBackground(HATTER_SZIN);
        setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel cimke = new JLabel("MOZGÁS");
        cimke.setFont(new Font("Arial", Font.BOLD, 14));
        cimke.setForeground(Color.DARK_GRAY);
        cimke.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(cimke);
        add(Box.createRigidArea(new Dimension(0, 15)));

        jarmuValaszto = new JComboBox<>();
        jarmuValaszto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        jarmuValaszto.setAlignmentX(Component.LEFT_ALIGNMENT);
        jarmuValaszto.addActionListener(e -> savokFrissit());
        add(jarmuValaszto);
        add(Box.createRigidArea(new Dimension(0, 10)));

        savValaszto = new JComboBox<>();
        savValaszto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        savValaszto.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(savValaszto);
        add(Box.createRigidArea(new Dimension(0, 15)));

        mozgasGomb = new JButton("Mozog");
        mozgasGomb.setAlignmentX(Component.LEFT_ALIGNMENT);
        mozgasGomb.addActionListener(e -> mozgasKezeles());
        add(mozgasGomb);

        add(Box.createVerticalGlue());

        korVegeGomb = new JButton("Kör vége");
        korVegeGomb.setBackground(new Color(230, 126, 34));
        korVegeGomb.setForeground(Color.WHITE);
        korVegeGomb.setFont(new Font("Arial", Font.BOLD, 16));
        korVegeGomb.setFocusPainted(false);
        korVegeGomb.setAlignmentX(Component.LEFT_ALIGNMENT);
        korVegeGomb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        korVegeGomb.addActionListener(e -> korVege());
        add(korVegeGomb);
    }

    /**
     * Frissíti a jármű- és sávválasztó dropdownokat az aktuális játékos alapján.
     * Ezt hívja a MainFrame.korFrissites() körváltáskor.
     */
    public void frissit() {
        // Leállítjuk az ActionListenert, hogy ne váltson ki savokFrissit()-et minden addItem()-nél
        ActionListener[] listeners = jarmuValaszto.getActionListeners();
        for (ActionListener l : listeners) jarmuValaszto.removeActionListener(l);

        aktualisJarmuvek.clear();
        jarmuValaszto.removeAllItems();

        Jatekos aktJatekos = jatekter.getAktivJatekos();
        if (aktJatekos instanceof Takarito) {
            for (Hokotro h : ((Takarito) aktJatekos).getIranyitottHokotrok()) {
                aktualisJarmuvek.add(h);
                jarmuValaszto.addItem(h.getId());
            }
        } else if (aktJatekos instanceof Buszvezeto) {
            for (Busz b : ((Buszvezeto) aktJatekos).getIranyithatoBuszok()) {
                aktualisJarmuvek.add(b);
                jarmuValaszto.addItem(b.getId());
            }
        }

        for (ActionListener l : listeners) jarmuValaszto.addActionListener(l);

        savokFrissit();
    }

    /**
     * Frissíti a sávválasztó dropdownt a kiválasztott jármű elérhető sávjaival.
     */
    private void savokFrissit() {
        savValaszto.removeAllItems();
        elerhetoSavok.clear();

        int idx = jarmuValaszto.getSelectedIndex();
        if (idx < 0 || idx >= aktualisJarmuvek.size()) return;

        Jarmu kivalasztott = aktualisJarmuvek.get(idx);
        Lokacio poz = kivalasztott.getPozicio();
        if (poz == null) {
            savValaszto.addItem("(nincs pozíció)");
            return;
        }

        List<Sav> jelenlegiSzakasz = poz.getSzakasz();
        elerhetoSavok = kivalasztott.getElerhetoSavok();

        for (Sav s : elerhetoSavok) {
            boolean savvaltas = (jelenlegiSzakasz != null && jelenlegiSzakasz.contains(s));
            String leiras = (savvaltas ? "Sávváltás" : "Előre") + " → Sáv " + s.getSavSzama();
            savValaszto.addItem(leiras);
        }

        if (elerhetoSavok.isEmpty()) {
            savValaszto.addItem("(nincs elérhető sáv)");
        }
    }

    /**
     * Végrehajtja a mozgást: a kiválasztott jármű a kiválasztott sávra lép.
     */
    private void mozgasKezeles() {
        int savIdx = savValaszto.getSelectedIndex();
        if (savIdx < 0 || savIdx >= elerhetoSavok.size()) {
            System.out.println(">>> [MozgasPanel] Nincs érvényes sáv kiválasztva.");
            return;
        }

        int jarmuIdx = jarmuValaszto.getSelectedIndex();
        if (jarmuIdx < 0 || jarmuIdx >= aktualisJarmuvek.size()) return;

        Jatekos aktJatekos = jatekter.getAktivJatekos();
        if (aktJatekos == null || aktJatekos.getAkcioPont() <= 0) {
            System.out.println(">>> [MozgasPanel] Nincs elég akciópont!");
            return;
        }

        Jarmu kivalasztott = aktualisJarmuvek.get(jarmuIdx);
        // A mozgas() a Sav.savSzama értékét indexként használja az elerhetoSavok listában
        boolean siker = kivalasztott.mozgas(new Sav(savIdx));
        System.out.println(">>> [MozgasPanel] Mozgás: " + (siker ? "SIKERES" : "SIKERTELEN"));

        if (siker) {
            aktJatekos.akcioPontKezelo();
        }

        jatekter.autoKorvaltas();
        frissit();
        mainFrame.korFrissites();
    }

    /**
     * Lezárja az aktuális kört és vált a következő játékosra.
     */
    public void korVege() {
        System.out.println(">>> [MozgasPanel] Kör befejezése gomb megnyomva.");
        jatekter.korVegeVegrehajtas();
        mainFrame.korFrissites();
    }
}
