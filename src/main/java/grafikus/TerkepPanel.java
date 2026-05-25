package grafikus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jatekos.Jatekter;
import jarmu.*;
import terkep.*;

public class TerkepPanel extends JPanel {

    private Jatekter jatekter;
    private List<GraphicObject> rajzolandoObjektumok;
    
    // Konstansok a dinamikus méretezéshez
    public static final int SAV_SZELESSEG = 40;
    public static final int SZAKASZ_MAGASSAG = 50;

    // Fix pozíciók az utak BAL FELSŐ sarkához
    private static final Map<String, Point> poziciok = new HashMap<>();

    public TerkepPanel(Jatekter jatekter) {
        this.jatekter = jatekter;
        this.rajzolandoObjektumok = new ArrayList<>();
        setPreferredSize(new Dimension(1000, 800));
        setBackground(new Color(236, 240, 241)); // Havas, világos háttér

        // Az XML-ben lévő nevek alapján
        poziciok.put("EszakiUt", new Point(400, 50));
        poziciok.put("NyugatiUt", new Point(100, 350));
        poziciok.put("Kozpont", new Point(400, 350));
        poziciok.put("KeletiHid", new Point(700, 350));
        poziciok.put("DeliAlagut", new Point(400, 600));

        // A rajzodon lévő számos nevek alapján is
        poziciok.put("67", new Point(400, 50));   
        poziciok.put("1",  new Point(100, 350));  
        poziciok.put("99", new Point(400, 350));  
        poziciok.put("42", new Point(700, 350));  
        poziciok.put("23", new Point(700, 600));  
        poziciok.put("11", new Point(400, 750));
    }

    public static Point getUtAlapPozicio(String nev) {
        return poziciok.getOrDefault(nev, new Point(50, 50));
    }

    public static Point getPontosCellaPozicio(Ut ut, List<Sav> szakasz, Sav sav) {
        Point alap = poziciok.get(ut.getNev());
        if (alap == null) return new Point(50, 50);

        int szakaszIdx = ut.getSzakaszok().indexOf(szakasz);
        if (szakaszIdx == -1) szakaszIdx = 0;

        int savIdx = -1;
        if (szakasz != null) {
            savIdx = szakasz.indexOf(sav);
        }
        if (savIdx == -1) savIdx = 0;

        int x = alap.x + (savIdx * SAV_SZELESSEG);
        int y = alap.y + (szakaszIdx * SZAKASZ_MAGASSAG);
        return new Point(x, y);
    }

    public void jarmuGrafikusObjektumokFrissitese() {
        if (jatekter == null || jatekter.getJarmuvek() == null) return;
        rajzolandoObjektumok.clear();
        for (Jarmu j : jatekter.getJarmuvek()) {
            if (j.getPozicio() == null || j.getPozicio().getUt() == null) continue;
            if (j instanceof Hokotro) rajzolandoObjektumok.add(new GraphicHokotro((Hokotro) j));
            else if (j instanceof Busz) rajzolandoObjektumok.add(new GraphicBusz((Busz) j));
            else if (j instanceof Auto) rajzolandoObjektumok.add(new GraphicAuto((Auto) j));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (jatekter == null || jatekter.getTerkep() == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        List<Ut> halozat = jatekter.getTerkep().getTeljesHalozat();

        // 1. ÖSSZEKÖTŐ VONALAK RAJZOLÁSA
        g2d.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.setColor(new Color(52, 152, 219, 150)); 
        
        for (Ut forrasUt : halozat) {
            Point p1 = poziciok.get(forrasUt.getNev());
            if (p1 == null) continue;
            
            int szelesseg1 = (forrasUt.getPozSavokSzama() + forrasUt.getNegSavokSzama()) * SAV_SZELESSEG;
            int magassag1 = forrasUt.getHossz() * SZAKASZ_MAGASSAG;
            Point kozep1 = new Point(p1.x + szelesseg1 / 2, p1.y + magassag1 / 2);

            for (Ut celUt : forrasUt.getSzomszedok(1)) {
                Point p2 = poziciok.get(celUt.getNev());
                if (p2 != null) {
                    int szelesseg2 = (celUt.getPozSavokSzama() + celUt.getNegSavokSzama()) * SAV_SZELESSEG;
                    int magassag2 = celUt.getHossz() * SZAKASZ_MAGASSAG;
                    Point kozep2 = new Point(p2.x + szelesseg2 / 2, p2.y + magassag2 / 2);
                    
                    g2d.drawLine(kozep1.x, kozep1.y, kozep2.x, kozep2.y);
                }
            }
        }

        // 2. UTAK, SZAKASZOK, SÁVOK ÉS IRÁNYOK RAJZOLÁSA
        for (Ut ut : halozat) {
            Point p = poziciok.get(ut.getNev());
            if (p == null) continue;

            int pozSav = ut.getPozSavokSzama();
            int negSav = ut.getNegSavokSzama();
            int osszSav = pozSav + negSav;
            int utSzelesseg = osszSav * SAV_SZELESSEG;
            int utMagassag = ut.getHossz() * SZAKASZ_MAGASSAG;

            // Aszfalt alap
            g2d.setColor(new Color(149, 165, 166));
            g2d.fillRoundRect(p.x, p.y, utSzelesseg, utMagassag, 10, 10);
            g2d.setColor(Color.DARK_GRAY);
            g2d.drawRoundRect(p.x, p.y, utSzelesseg, utMagassag, 10, 10);

            // CELLÁNKÉNTI RAJZOLÁS: hó/jég, majd iránynyíl
            for (int szakaszIdx = 0; szakaszIdx < ut.getHossz(); szakaszIdx++) {
                List<Sav> szakaszSavok = ut.getSzakaszok().get(szakaszIdx);
                for (int savIdx = 0; savIdx < osszSav; savIdx++) {
                    int cellX = p.x + (savIdx * SAV_SZELESSEG);
                    int cellY = p.y + (szakaszIdx * SZAKASZ_MAGASSAG);
                    int cx = cellX + SAV_SZELESSEG / 2;
                    int cy = cellY + SZAKASZ_MAGASSAG / 2;
                    boolean isPositive = (savIdx < pozSav);

                    // Hóviszonyok megjelenítése
                    if (savIdx < szakaszSavok.size()) {
                        Sav sav = szakaszSavok.get(savIdx);
                        int ho = sav.getHo();
                        if (ho > 0) {
                            int alfa = Math.min(200, ho * 8);
                            g2d.setColor(new Color(255, 255, 255, alfa));
                            g2d.fillRect(cellX + 1, cellY + 1, SAV_SZELESSEG - 2, SZAKASZ_MAGASSAG - 2);
                        }
                        if (sav.jegesE()) {
                            g2d.setColor(new Color(133, 193, 233, 160));
                            g2d.fillRect(cellX + 1, cellY + 1, SAV_SZELESSEG - 2, SZAKASZ_MAGASSAG - 2);
                        }
                        if (sav.isZuzalekos()) {
                            g2d.setColor(new Color(180, 180, 180, 150));
                            g2d.drawRect(cellX + 2, cellY + 2, SAV_SZELESSEG - 4, SZAKASZ_MAGASSAG - 4);
                        }
                    }

                    // Irányjelző nyíl
                    g2d.setColor(new Color(255, 255, 255, 90));
                    int size = 8;
                    if (isPositive) {
                        int[] xPoints = {cx - size, cx + size, cx};
                        int[] yPoints = {cy - size, cy - size, cy + size};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                    } else {
                        int[] xPoints = {cx - size, cx + size, cx};
                        int[] yPoints = {cy + size, cy + size, cy - size};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                    }
                }
            }

            // Szakaszok elválasztó vonalai (vízszintes)
            g2d.setColor(new Color(127, 140, 141, 100));
            g2d.setStroke(new BasicStroke(1));
            for (int i = 1; i < ut.getHossz(); i++) {
                int y = p.y + (i * SZAKASZ_MAGASSAG);
                g2d.drawLine(p.x, y, p.x + utSzelesseg, y);
            }

            // Sávok elválasztó vonalai (függőleges)
            Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0);
            for (int i = 1; i < osszSav; i++) {
                int x = p.x + (i * SAV_SZELESSEG);
                if (i == pozSav) {
                    // Elválasztó vonal a pozitív és negatív sávok között (Záróvonal)
                    g2d.setColor(new Color(241, 196, 15));
                    g2d.setStroke(new BasicStroke(3));
                } else {
                    // Sima sávelválasztó
                    g2d.setColor(Color.WHITE);
                    g2d.setStroke(dashed);
                }
                g2d.drawLine(x, p.y, x, p.y + utMagassag);
            }

            // Út neve
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString("Út: " + ut.getNev(), p.x, p.y - 5);
        }

        // 3. JÁRMŰVEK RAJZOLÁSA
        for (GraphicObject go : rajzolandoObjektumok) {
            go.rajzol(g2d);
        }
    }
}