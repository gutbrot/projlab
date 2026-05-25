package grafikus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;
import jarmu.Auto;
import jatekos.Jatekter;
import terkep.Sav;
import terkep.Terkep;
import terkep.Ut;

public class GraphicAuto extends GraphicObject {

    private Auto auto;

    public GraphicAuto(Auto auto) {
        super();
        this.auto = auto;
        frissitPozicio();
    }

    @Override
    public void rajzol(Graphics g) {
        frissitPozicio();

        // Autótest (Klasszikus piros)
        g.setColor(new Color(192, 57, 43));
        g.fillRect(x + 5, y + 5, 24, 14);

        // Szélvédők és tetővonal
        g.setColor(new Color(231, 76, 60));
        g.fillRect(x + 11, y + 7, 12, 10);
        g.setColor(Color.BLACK);
        g.fillRect(x + 19, y + 8, 3, 8); // Első ablak

        // Baleset/Mozgásképtelenség jelzése
        if (auto.getMozgaskeptelenKorokSzama() > 0) {
            g.setColor(Color.ORANGE);
            g.fillOval(x + 3, y + 2, 6, 6); // Vészvillogó sárga pötty
            g.fillOval(x + 25, y + 15, 6, 6);
        }

        // ID kiírása
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(auto.getId().replace("Auto_", "A"), x + 7, y + 16);
    }

    @Override
    public void frissitPozicio() {
        if (auto == null || auto.getPozicio() == null || auto.getPozicio().getUt() == null) return;

        Sav sav = auto.getPozicio().getSav();
        Ut ut = auto.getPozicio().getUt();
        List<Sav> szakasz = auto.getPozicio().getSzakasz();

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