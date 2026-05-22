package grafikus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.util.List;
import jarmu.Busz;
import jatekos.Jatekter;
import terkep.Sav;
import terkep.Terkep;
import terkep.Ut;

/**
 * A menetrend szerinti buszok grafikus megjelenítéséért felelős osztály.
 */
public class GraphicBusz extends GraphicObject {

    /** A háttérben lévő busz modell objektum. */
    private Busz busz;

    /**
     * A GraphicBusz konstruktora.
     *
     * @param busz A megjelenítendő busz objektum referenciája.
     */
    public GraphicBusz(Busz busz) {
        super();
        this.busz = busz;
        frissitPozicio();
    }

    /**
     * Kirajzolja a buszt a térképre. A busz egy nyújtott sárga téglalap sötét ablakokkal.
     *
     * @param g A Swing grafikus kontextusa.
     */
    @Override
    public void rajzol(Graphics g) {
        frissitPozicio();

        // Busztest (Télies, feltűnő sárga szín)
        g.setColor(new Color(241, 196, 15));
        g.fillRect(x + 2, y + 6, 45, 18);

        // Kerekek (3 pár kerék a busz hosszúsága miatt)
        g.setColor(Color.BLACK);
        g.fillRect(x + 6, y + 3, 7, 3);
        g.fillRect(x + 32, y + 3, 7, 3);
        g.fillRect(x + 6, y + 24, 7, 3);
        g.fillRect(x + 32, y + 24, 7, 3);

        // Utasablakok sora (Sötétszürke téglalapok)
        g.setColor(new Color(52, 73, 94));
        g.fillRect(x + 8, y + 10, 6, 4);
        g.fillRect(x + 18, y + 10, 6, 4);
        g.fillRect(x + 28, y + 10, 6, 4);
        g.fillRect(x + 38, y + 10, 5, 4);

        // Busz ID felirat
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(busz.getId(), x + 12, y + 21);
    }

    /**
     * Szinkronizálja a busz képernyő-koordinátáit a sáv valós helyzetével.
     */
    @Override
    public void frissitPozicio() {
        if (busz != null && busz.getPozicio() != null) {
            Sav sav = busz.getPozicio().getSav();
            Ut ut = busz.getPozicio().getUt();

            int utIndex = 0;
            Terkep globalTerkep = Jatekter.getGlobalTerkep();
            if (globalTerkep != null) {
                List<Ut> halozat = globalTerkep.getTeljesHalozat();
                int idx = halozat.indexOf(ut);
                if (idx >= 0) utIndex = idx;
            }

            int savIndex = sav.getSavSzama();

            // TerkepPanel képletével azonos, kis x-eltolás a hokotróhoz képest
            this.x = 100 + (utIndex * 150) + 35;
            this.y = 80 + (savIndex * 35) + 5;
        }
    }
}