package grafikus;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Font;
import java.util.List;
import jarmu.Busz;
import jatekos.Jatekter;
import terkep.Sav;
import terkep.Terkep;
import terkep.Ut;

public class GraphicBusz extends GraphicObject {

    private Busz busz;

    public GraphicBusz(Busz busz) {
        super();
        this.busz = busz;
        frissitPozicio();
    }

    @Override
    public void rajzol(Graphics g) {
        frissitPozicio();

        // Busztest (Télies, feltűnő sárga szín)
        g.setColor(new Color(241, 196, 15));
        g.fillRect(x + 5, y + 2, 34, 26);

        // Szélvédő és fényszórók
        g.setColor(new Color(41, 128, 185)); // Kék szélvédő
        g.fillRect(x + 33, y + 4, 6, 22);

        // Kerekek
        g.setColor(Color.BLACK);
        g.fillRect(x + 6, y + 1, 7, 3);
        g.fillRect(x + 30, y + 1, 7, 3);
        g.fillRect(x + 6, y + 26, 7, 3);
        g.fillRect(x + 30, y + 26, 7, 3);

        // Utasablakok sora
        g.setColor(new Color(52, 73, 94));
        g.fillRect(x + 8, y + 10, 6, 4);
        g.fillRect(x + 16, y + 10, 6, 4);
        g.fillRect(x + 24, y + 10, 6, 4);

        // Busz ID felirat
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(busz.getId(), x + 9, y + 23);
    }

    
    @Override
    public void frissitPozicio() {
        if (busz == null || busz.getPozicio() == null || busz.getPozicio().getUt() == null) return;

        Sav sav = busz.getPozicio().getSav();
        Ut ut = busz.getPozicio().getUt();
        List<Sav> szakasz = busz.getPozicio().getSzakasz();

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