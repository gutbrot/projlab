package grafikus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import jarmu.Auto;
import terkep.Sav;
import terkep.Ut;

/**
 * A szimulációban közlekedő környezeti NPC autók grafikus megjelenítője.
 */
public class GraphicAuto extends GraphicObject {

    /** A reprezentált NPC autó modell referenciája. */
    private Auto auto;

    /**
     * A GraphicAuto konstruktora.
     *
     * @param auto A kirajzolni kívánt autó.
     */
    public GraphicAuto(Auto auto) {
        super();
        this.auto = auto;
        frissitPozicio();
    }

    /**
     * Kirajzolja a személyautót. Egy piros, kompakt dobozforma.
     *
     * @param g A Swing grafikus felülete.
     */
    @Override
    public void rajzol(Graphics g) {
        frissitPozicio();

        // Autótest (Klasszikus piros, hogy elüssön a havas úttól)
        g.setColor(new Color(192, 57, 43));
        g.fillRect(x + 8, y + 8, 24, 14);

        // Szélvédők és tetővonal
        g.setColor(new Color(231, 76, 60));
        g.fillRect(x + 14, y + 10, 12, 10);
        g.setColor(Color.BLACK);
        g.fillRect(x + 22, y + 11, 3, 8); // Első ablak

        // Baleset/Mozgásképtelenség jelzése (ha a számlálója nagyobb, mint 0)
        if (auto.getMozgaskeptelenKorokSzama() > 0) {
            g.setColor(Color.ORANGE);
            g.fillOval(x + 6, y + 4, 6, 6); // Vészvillogó sárga pötty
            g.fillOval(x + 28, y + 20, 6, 6);
        }

        // ID kiírása
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 9));
        g.drawString(auto.getId().replace("Auto_", "A"), x + 10, y + 18);
    }

    /**
     * Szinkronizálja a koordinátákat a modell pozíciója alapján.
     */
    @Override
    public void frissitPozicio() {
        if (auto != null && auto.getPozicio() != null) {
            Sav sav = auto.getPozicio().getSav();
            Ut ut = auto.getPozicio().getUt();

            int utIndex = ut.getNev().hashCode() % 5;
            int savIndex = sav.getSavSzama();

            this.x = 150 + (utIndex * 140);
            this.y = 80 + (savIndex * 35);
        }
    }
}