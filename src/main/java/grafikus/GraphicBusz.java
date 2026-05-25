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

        g.setColor(new Color(241, 196, 15));
        g.fillRoundRect(x, y, 24, 40, 5, 5);

        g.setColor(new Color(41, 128, 185)); 
        g.fillRect(x + 2, y + 2, 20, 6);

        g.setColor(Color.BLACK);
        g.fillRect(x - 1, y + 5, 3, 6);
        g.fillRect(x + 22, y + 5, 3, 6);
        g.fillRect(x - 1, y + 30, 3, 6);
        g.fillRect(x + 22, y + 30, 3, 6);

        g.setColor(new Color(52, 73, 94));
        g.fillRect(x + 4, y + 10, 16, 25);

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(busz.getId(), x - 5, y - 2);
    }

    @Override
    public void frissitPozicio() {
        if (busz == null || busz.getPozicio() == null || busz.getPozicio().getUt() == null) return;

        Sav sav = busz.getPozicio().getSav();
        Ut ut = busz.getPozicio().getUt();
        List<Sav> szakasz = busz.getPozicio().getSzakasz();

        if (sav == null || ut == null || szakasz == null) return;

        // Lekérjük a pontos cella bal felső sarkát
        Point cella = TerkepPanel.getPontosCellaPozicio(ut, szakasz, sav);

        // Középre igazítjuk a járművet a cellán belül
        this.x = cella.x + (TerkepPanel.SAV_SZELESSEG - 24) / 2;
        this.y = cella.y + (TerkepPanel.SZAKASZ_MAGASSAG - 40) / 2;
    }
}