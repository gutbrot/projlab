package grafikus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

        boolean lefeleMegy = auto.getPozicio() != null
                && auto.getPozicio().getSav() != null
                && auto.getPozicio().getUt() != null
                && auto.getPozicio().getSav().getSavSzama() < auto.getPozicio().getUt().getPozSavokSzama();

        Graphics2D g2d = (Graphics2D) g.create();
        if (lefeleMegy) {
            g2d.rotate(Math.PI, x + 10, y + 15);
        }

        // Autótest
        g2d.setColor(new Color(192, 57, 43));
        g2d.fillRoundRect(x, y, 20, 30, 4, 4);

        // Első szélvédő (felül)
        g2d.setColor(new Color(174, 214, 241));
        g2d.fillRect(x + 3, y + 3, 14, 8);

        // Hátsó ablak (alul)
        g2d.setColor(new Color(174, 214, 241));
        g2d.fillRect(x + 3, y + 19, 14, 6);

        // Kerekek
        g2d.setColor(Color.BLACK);
        g2d.fillRect(x - 2, y + 4,  4, 6);
        g2d.fillRect(x + 18, y + 4,  4, 6);
        g2d.fillRect(x - 2, y + 20, 4, 6);
        g2d.fillRect(x + 18, y + 20, 4, 6);

        g2d.dispose();

        // Baleset jelzése
        if (auto.getMozgaskeptelenKorokSzama() > 0) {
            g.setColor(Color.ORANGE);
            g.fillOval(x + 6, y - 4, 8, 8);
        }

        // ID felirat – mindig egyenesen
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 9));
        g.drawString(auto.getId().replace("Auto_", "A"), x + 3, y - 2);
    }

    @Override
    public void frissitPozicio() {
        if (auto == null || auto.getPozicio() == null || auto.getPozicio().getUt() == null) return;

        Sav sav = auto.getPozicio().getSav();
        Ut ut = auto.getPozicio().getUt();
        List<Sav> szakasz = auto.getPozicio().getSzakasz();

        if (sav == null || ut == null || szakasz == null) return;

        Point cella = TerkepPanel.getPontosCellaPozicio(ut, szakasz, sav);

        this.x = cella.x + (TerkepPanel.SAV_SZELESSEG - 20) / 2;
        this.y = cella.y + (TerkepPanel.SZAKASZ_MAGASSAG - 30) / 2;
    }
}