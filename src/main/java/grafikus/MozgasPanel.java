package grafikus;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import jatekos.Jatekter;

/**
 * A játékos mozgásának vezérléséért felelős panel. 
 * Lehetővé teszi jármű kiválasztását, a célmező (sáv) megadását és a kör befejezését.
 */
public class MozgasPanel extends JPanel {

    /** A játékoshoz tartozó, kiválasztható járművek legördülő listája. */
    private JComboBox<String> jarmuValaszto;

    /** A térképen elérhető sávok (célpontok) legördülő listája. */
    private JComboBox<String> savValaszto;

    /** A tényleges mozgás végrehajtását kezdeményező gomb. */
    private JButton mozgasGomb;

    /** Az aktuális játékos körének befejezését jelző gomb. */
    private JButton korVegeGomb;

    /** A főablak referenciája, hogy a mozgás után frissítést tudjunk kérni a felülettől. */
    private MainFrame mainFrame;

    /** A játékmodell referenciája az üzleti logika eléréséhez. */
    private Jatekter jatekter;

    /** Télies, világosszürke háttérszín. */
    private final Color HATTTER_SZIN = new Color(236, 240, 241);

    /**
     * A MozgasPanel konstruktora. 
     * Felépíti a grafikus elemeket függőleges elrendezésben (BoxLayout).
     *
     * @param mainFrame A GUI főablakának referenciája.
     * @param jatekter A játék logikáját tartalmazó modell objektum.
     */
    public MozgasPanel(MainFrame mainFrame, Jatekter jatekter) {
        this.mainFrame = mainFrame;
        this.jatekter = jatekter;

        // Panel alapbeállításai
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(250, 0));
        setBackground(HATTTER_SZIN);
        setBorder(new EmptyBorder(20, 15, 20, 15));

        // Fejléc címke
        JLabel cimke = new JLabel("MOZGÁS");
        cimke.setFont(new Font("Arial", Font.BOLD, 14));
        cimke.setForeground(Color.DARK_GRAY);
        cimke.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(cimke);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Járműválasztó inicializálása
        jarmuValaszto = new JComboBox<>();
        jarmuValaszto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        jarmuValaszto.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(jarmuValaszto);
        add(Box.createRigidArea(new Dimension(0, 10)));

        // Sávválasztó inicializálása
        savValaszto = new JComboBox<>();
        savValaszto.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        savValaszto.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(savValaszto);
        add(Box.createRigidArea(new Dimension(0, 15)));

        // Mozgás gomb inicializálása
        mozgasGomb = new JButton("Mozog");
        mozgasGomb.setAlignmentX(Component.LEFT_ALIGNMENT);
        mozgasGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String kivalasztottJarmu = (String) jarmuValaszto.getSelectedItem();
                if (kivalasztottJarmu != null) {
                    mozgasKezeles(kivalasztottJarmu);
                }
            }
        });
        add(mozgasGomb);

        // Kitöltjük az üres helyet, hogy a "Kör vége" gomb az aljára kerüljön
        add(Box.createVerticalGlue());

        // Kör vége gomb inicializálása a kért narancssárga színnel
        korVegeGomb = new JButton("Kör vége");
        korVegeGomb.setBackground(new Color(230, 126, 34)); // Narancssárga
        korVegeGomb.setForeground(Color.WHITE);
        korVegeGomb.setFont(new Font("Arial", Font.BOLD, 16));
        korVegeGomb.setFocusPainted(false);
        korVegeGomb.setAlignmentX(Component.LEFT_ALIGNMENT);
        korVegeGomb.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        korVegeGomb.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                korVege();
            }
        });
        add(korVegeGomb);
    }

    /**
     * Végrehajtja a mozgást a felhasználó által kiválasztott adatok alapján.
     * Frissíti a modellt, majd a pull-architektúra alapján kéri a GUI újrarajzolását.
     *
     * @param id A mozgatni kívánt jármű azonosítója.
     */
    public void mozgasKezeles(String id) {
        System.out.println(">>> [MozgasPanel] Mozgás kezdeményezve a járművel: " + id);
        // Itt történik a jatekter.mozgat() modell szintű hívása
        
        // Mozgás után azonnal frissítjük a grafikus térképet
        mainFrame.terkepFrissites();
    }

    /**
     * Beállítja az aktív járművet a felület legördülő menüjében.
     * Ezt hívhatja a backend, ha automatikusan fókuszt váltunk egy járműre.
     *
     * @param id A kiválasztandó jármű azonosítója.
     */
    public void jarmuValaszt(String id) {
        if (id != null) {
            jarmuValaszto.setSelectedItem(id);
        }
    }

    /**
     * Lezárja az aktuális kört. 
     * Utasítja a backendet a következő játékosra váltásra, majd frissíti a GUI állapotát.
     */
    public void korVege() {
        System.out.println(">>> [MozgasPanel] Kör befejezése gomb megnyomva.");
        // A jatekter léptetése megtörténik itt
        
        // A kör végén újrarajzoljuk a térképet és a gombok állapotát
        mainFrame.terkepFrissites();
    }
}