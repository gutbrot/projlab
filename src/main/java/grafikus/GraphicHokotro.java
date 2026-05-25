package grafikus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import jarmu.Hokotro;
import jatekos.Jatekter;
import terkep.Sav;
import terkep.Terkep;
import terkep.Ut;

public class GraphicHokotro extends GraphicObject {

    private Hokotro hokotro;
    private boolean vanKotrofej;

    public GraphicHokotro(Hokotro hokotro) {
        super();
        this.hokotro = hokotro;
        frissitPozicio();
    }

    @Override
    public void rajzol(Graphics g) {
        frissitPozicio();
        vanKotrofej = (hokotro.getFelszereltFej() != null);

        // Hókotró test (Sötétsárga/narancs masszív blokk)
        g.setColor(new Color(230, 126, 34));
        g.fillRect(x + 5, y + 2, 30, 26);

        // Vezérlő fülke (Szürke)
        g.setColor(new Color(127, 140, 141));
        g.fillRect(x + 12, y + 6, 12, 18);

        // Kerekek
        g.setColor(Color.BLACK);
        g.fillRoundRect(x + 8, y + 0, 8, 4, 2, 2);
        g.fillRoundRect(x + 24, y + 0, 8, 4, 2, 2);
        g.fillRoundRect(x + 8, y + 26, 8, 4, 2, 2);
        g.fillRoundRect(x + 24, y + 26, 8, 4, 2, 2);

        // Kotrófej jelzése (ha van felszerelve)
        if (vanKotrofej) {
            g.setColor(new Color(192, 57, 43)); // Jól látható narancssárga/piros fej
            g.fillRect(x + 35, y + 2, 4, 26);
        }

        // Jármű azonosítójának kiírása
        g.setColor(Color.WHITE);
        g.drawString(hokotro.getId(), x + 7, y + 19);
    }

   @Override
    public void frissitPozicio() {
        if (hokotro == null || hokotro.getPozicio() == null || hokotro.getPozicio().getUt() == null) return;

        Sav sav = hokotro.getPozicio().getSav();
        Ut ut = hokotro.getPozicio().getUt();
        List<Sav> szakasz = hokotro.getPozicio().getSzakasz();

        if (sav == null || ut == null || szakasz == null) return;

        // JAVÍTÁS: Lekérjük a TerkepPanel közös statikus koordinátáját!
        Point alapPoz = TerkepPanel.getUtAlapPozicio(ut.getNev());

        int szakaszIndex = ut.getSzakaszok().indexOf(szakasz);
        int savIndex = sav.getSavSzama();

        int szakaszMagassag = ut.getSzakaszok().size() > 0 ? 400 / ut.getSzakaszok().size() : 400;
        int osszSav = ut.getPozSavokSzama() + ut.getNegSavokSzama();
        int savSzelesseg = osszSav > 0 ? 120 / osszSav : 120;

        // Kiszámítjuk a pontos X és Y pozíciót a kiválasztott úthoz képest
        this.x = alapPoz.x + (savIndex * savSzelesseg) + 10; 
        this.y = alapPoz.y + (szakaszIndex * szakaszMagassag) + 15; 
    }
}