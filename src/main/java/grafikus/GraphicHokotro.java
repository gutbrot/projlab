package grafikus;

import java.awt.Color;
import java.awt.Graphics;
import jarmu.Hokotro;
import terkep.Sav;
import terkep.Ut;

/**
 * A hókotró járművek grafikus reprezentációjáért felelős osztály.
 * Kirajzolja a hókotró testét és jelzi annak aktuális felszereltségi állapotát.
 */
public class GraphicHokotro extends GraphicObject {

    /** A reprezentált hókotró modell-szintű objektuma. */
    private Hokotro hokotro;

    /** Jelzi, hogy a hókotrón van-e jelenleg működő, felszerelt kotrófej. */
    private boolean vanKotrofej;

    /**
     * A GraphicHokotro konstruktora.
     * Összeköti a grafikus reprezentációt a valós hókotró modellel.
     *
     * @param hokotro A kirajzolni kívánt hókotró jármű referenciája.
     */
    public GraphicHokotro(Hokotro hokotro) {
        super();
        this.hokotro = hokotro;
        frissitPozicio();
    }

    /**
     * Kirajzolja a hókotrót a megadott grafikus kontextusra.
     * Egy sötétkék téglalapként jelenik meg, elején egy narancssárga/sárga kotrófej-jelzéssel.
     *
     * @param g A Swing grafikus objektuma.
     */
    @Override
    public void rajzol(Graphics g) {
        // Pozíció frissítése a biztonság kedvéért a kirajzolás előtt
        frissitPozicio();

        // Hókotró alaptestének színezése (Sötét acélkék a télies hangulathoz)
        g.setColor(new Color(41, 128, 185));
        g.fillRect(x + 5, y + 5, 30, 20);

        // Kerekek kirajzolása (fekete kis téglalapok)
        g.setColor(Color.BLACK);
        g.fillRect(x + 8, y + 2, 8, 4);
        g.fillRect(x + 24, y + 2, 8, 4);
        g.fillRect(x + 8, y + 24, 8, 4);
        g.fillRect(x + 24, y + 24, 8, 4);

        // Ablak/Szélvédő (világoskék)
        g.setColor(new Color(174, 214, 241));
        g.fillRect(x + 25, y + 8, 5, 14);

        // Ha van felszerelt kotrófej, húzunk az elejére egy narancssárga tolólapot
        frissitAllapot();
        if (vanKotrofej) {
            g.setColor(new Color(230, 126, 34)); // Specifikáció szerinti narancssárga
            g.fillRect(x + 35, y + 2, 4, 26);
        }

        // Jármű azonosítójának kiírása a tetőre fehér színnel
        g.setColor(Color.WHITE);
        g.drawString(hokotro.getId(), x + 7, y + 19);
    }

    /**
     * Kiszámítja és frissíti a képernyő-koordinátákat a hókotró sávhelyzete alapján.
     */
    @Override
    public void frissitPozicio() {
        if (hokotro != null && hokotro.getPozicio() != null) {
            Sav sav = hokotro.getPozicio().getSav();
            Ut ut = hokotro.getPozicio().getUt();
            
            // Egyszerűsített dinamikus koordináta-leképezés az úthálózat alapján
            int utIndex = ut.getNev().hashCode() % 5; 
            int savIndex = sav.getSavSzama();
            
            // Kiszámítjuk a pixel-pozíciókat a térképen
            this.x = 120 + (utIndex * 140);
            this.y = 80 + (savIndex * 35);
        }
    }

    /**
     * Frissíti a hókotró belső grafikus állapotát (pl. kotrófej megléte) a modell alapján.
     */
    public void frissitAllapot() {
        if (hokotro != null) {
            this.vanKotrofej = (hokotro.getFelszereltFej() != null);
        }
    }
}