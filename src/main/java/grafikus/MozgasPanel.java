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
import terkep.Ut;

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

    private List<Jarmu> aktualisJarmuvek = new ArrayList<>();
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

    public void frissit() {
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
        
        // JAVÍTÁS: Ha van jármű, automatikusan válasszuk ki az elsőt a lista frissítése előtt
        if (jarmuValaszto.getItemCount() > 0 && jarmuValaszto.getSelectedIndex() == -1) {
            jarmuValaszto.setSelectedIndex(0);
        } else {
            savokFrissit();
        }
    }

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

        Ut jelenlegiUt = poz.getUt();
        List<Sav> jelenlegiSzakasz = poz.getSzakasz();
        elerhetoSavok = kivalasztott.getElerhetoSavok();
        boolean isPozitivDir = (poz.getSav().getSavSzama() < jelenlegiUt.getPozSavokSzama());

        for (Sav s : elerhetoSavok) {
            String akcio = "";

            if (jelenlegiSzakasz != null && jelenlegiSzakasz.contains(s)) {
                boolean sIsPozitiv = (s.getSavSzama() < jelenlegiUt.getPozSavokSzama());
                if (isPozitivDir != sIsPozitiv) {
                    akcio = "Megfordulás";
                } else {
                    akcio = "Sávváltás";
                }
            } else {
                boolean isUjUt = true;
                for (List<Sav> szakasz : jelenlegiUt.getSzakaszok()) {
                    if (szakasz.contains(s)) {
                        isUjUt = false;
                        break;
                    }
                }
                
                if (isUjUt) {
                    akcio = "Kanyarodás";
                } else {
                    akcio = "Előrehaladás";
                }
            }

            // JAVÍTÁS: Segít a hibakeresésben is, ha látjuk az utat is
            String utNeve = "";
            for (Ut u : jatekter.getTerkep().getTeljesHalozat()) {
                for (List<Sav> szakasz : u.getSzakaszok()) {
                    if (szakasz.contains(s)) {
                        utNeve = u.getNev();
                        break;
                    }
                }
            }
            
            String leiras = akcio + " → " + utNeve + " Sáv " + s.getSavSzama();
            savValaszto.addItem(leiras);
        }

        if (elerhetoSavok.isEmpty()) {
            savValaszto.addItem("(Zsákutca / Nincs lépés)");
        }
    }

    private void mozgasKezeles() {
        int savIdx = savValaszto.getSelectedIndex();
        if (savIdx < 0 || savIdx >= elerhetoSavok.size()) return;

        int jarmuIdx = jarmuValaszto.getSelectedIndex();
        if (jarmuIdx < 0 || jarmuIdx >= aktualisJarmuvek.size()) return;

        Jatekos aktJatekos = jatekter.getAktivJatekos();
        if (aktJatekos == null || aktJatekos.getAkcioPont() <= 0) {
            System.out.println(">>> [MozgasPanel] Nincs elég akciópont!");
            return;
        }

        Jarmu kivalasztott = aktualisJarmuvek.get(jarmuIdx);
        Sav valodiSav = elerhetoSavok.get(savIdx);
        
        System.out.println(">>> [MozgasPanel] Mozgás kérése a(z) " + valodiSav.getSavSzama() + ". sávra...");
        boolean siker = kivalasztott.mozgas(valodiSav);
        
        if (siker) {
            System.out.println(">>> [MozgasPanel] Mozgás sikeres!");
            aktJatekos.akcioPontKezelo();
        } else {
             System.out.println(">>> [MozgasPanel] Mozgás SIKERTELEN!");
        }

        // FONTOS JAVÍTÁS: Újra kell rajzolni a térképet
        mainFrame.korFrissites(); 
    }

    public void korVege() {
        jatekter.korVegeVegrehajtas();
        mainFrame.korFrissites();
    }
}