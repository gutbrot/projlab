package grafikus;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;
import jarmu.Hokotro;
import jatekos.Jatekter;
import terkep.Sav;
import terkep.Terkep;
import terkep.Ut;

/**
 * A hókotrót rajzolja ki a térképen: narancssárga test, szürke keret, fekete kerekek.
 * Ha van felszerelt kotrófej, piros pengét is kap az orra elé.
 * Pozitív sávon haladva (lefelé) megfordul, hogy a penge mindig előre nézzen.
 */
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

        boolean lefeleMegy = hokotro.getPozicio() != null
                && hokotro.getPozicio().getSav() != null
                && hokotro.getPozicio().getUt() != null
                && hokotro.getPozicio().getSav().getSavSzama() < hokotro.getPozicio().getUt().getPozSavokSzama();

        Graphics2D g2d = (Graphics2D) g.create();
        if (lefeleMegy) {
            // pozitív sáv = lefelé haladás → elő (pengefej) legyen lent
            g2d.rotate(Math.PI, x + 13, y + 17);
        }

        g2d.setColor(new Color(230, 126, 34));
        g2d.fillRoundRect(x, y, 26, 34, 5, 5);

        g2d.setColor(new Color(127, 140, 141));
        g2d.fillRect(x + 5, y + 15, 16, 12);

        g2d.setColor(Color.BLACK);
        g2d.fillRoundRect(x - 2, y + 4, 4, 8, 2, 2);
        g2d.fillRoundRect(x + 24, y + 4, 4, 8, 2, 2);
        g2d.fillRoundRect(x - 2, y + 22, 4, 8, 2, 2);
        g2d.fillRoundRect(x + 24, y + 22, 4, 8, 2, 2);

        if (vanKotrofej) {
            g2d.setColor(new Color(192, 57, 43));
            g2d.fillRect(x - 2, y - 4, 30, 6);
        }

        g2d.dispose();

        // Felirat mindig egyenesen, a jármű bounding box felett
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.drawString(hokotro.getId(), x - 5, y - 2);
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