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

        g.setColor(new Color(230, 126, 34));
        g.fillRoundRect(x, y, 26, 34, 5, 5);

        g.setColor(new Color(127, 140, 141));
        g.fillRect(x + 5, y + 15, 16, 12);

        g.setColor(Color.BLACK);
        g.fillRoundRect(x - 2, y + 4, 4, 8, 2, 2);
        g.fillRoundRect(x + 24, y + 4, 4, 8, 2, 2);
        g.fillRoundRect(x - 2, y + 22, 4, 8, 2, 2);
        g.fillRoundRect(x + 24, y + 22, 4, 8, 2, 2);

        if (vanKotrofej) {
            g.setColor(new Color(192, 57, 43)); 
            g.fillRect(x - 2, y - 4, 30, 6); 
        }

        g.setColor(Color.WHITE);
        g.drawString(hokotro.getId(), x - 10, y + 20);
    }

   @Override
    public void frissitPozicio() {
        if (hokotro == null || hokotro.getPozicio() == null || hokotro.getPozicio().getUt() == null) return;

        Sav sav = hokotro.getPozicio().getSav();
        Ut ut = hokotro.getPozicio().getUt();
        List<Sav> szakasz = hokotro.getPozicio().getSzakasz();

        if (sav == null || ut == null || szakasz == null) return;

        Point cella = TerkepPanel.getPontosCellaPozicio(ut, szakasz, sav);

        this.x = cella.x + (TerkepPanel.SAV_SZELESSEG - 26) / 2;
        this.y = cella.y + (TerkepPanel.SZAKASZ_MAGASSAG - 34) / 2;
    }
}