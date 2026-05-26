package grafikus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import jatekos.Jatekter;
import jarmu.*;
import terkep.*;

public class TerkepPanel extends JPanel {

    private Jatekter jatekter;
    private List<GraphicObject> rajzolandoObjektumok;
    
    // Konstansok a dinamikus méretezéshez
    public static final int SAV_SZELESSEG = 40;
    public static final int SZAKASZ_MAGASSAG = 50;

    // Statikus override-ok ismert térképekhez
    private static final Map<String, Point> poziciok = new HashMap<>();
    // Aktuálisan használt pozíciók (statikus override + dinamikus BFS)
    private static Map<String, Point> aktualisPoziciok = new HashMap<>();

    public TerkepPanel(Jatekter jatekter) {
        this.jatekter = jatekter;
        this.rajzolandoObjektumok = new ArrayList<>();
        setPreferredSize(new Dimension(1500, 1300));
        setBackground(new Color(236, 240, 241)); // Havas, világos háttér

        // Az XML-ben lévő nevek alapján (névvel ellátott utak)
        poziciok.put("EszakiUt", new Point(400, 50));
        poziciok.put("NyugatiUt", new Point(100, 350));
        poziciok.put("Kozpont", new Point(400, 350));
        poziciok.put("KeletiHid", new Point(700, 350));
        poziciok.put("DeliAlagut", new Point(400, 600));

        // uj_teszt_terkep.xml utak
        poziciok.put("67", new Point(400, 50));
        poziciok.put("1",  new Point(100, 350));
        poziciok.put("99", new Point(400, 350));
        poziciok.put("42", new Point(700, 350));
        poziciok.put("23", new Point(700, 600));
        poziciok.put("11", new Point(400, 750));

        // nagy_teszt_terkep.xml utak (3 sor × 4 oszlop + 1 déli zsákút)
        // Északi sor (y=50)
        poziciok.put("10",  new Point(300,  50));
        poziciok.put("20",  new Point(600,  50));
        poziciok.put("30",  new Point(900,  50));
        // Középső sor (y=350)
        poziciok.put("40",  new Point(50,  350));
        poziciok.put("50",  new Point(300, 350));
        poziciok.put("60",  new Point(600, 350));
        poziciok.put("70",  new Point(900, 350));
        poziciok.put("80",  new Point(1150, 350));
        // Alsó sor (y=700)
        poziciok.put("90",  new Point(300, 700));
        poziciok.put("100", new Point(600, 700));
        poziciok.put("110", new Point(900, 700));
        // Legdélibb zsákút (y=1000)
        poziciok.put("120", new Point(600, 1000));
    }

    public static Point getUtAlapPozicio(String nev) {
        return aktualisPoziciok.getOrDefault(nev, new Point(50, 50));
    }

    public static Point getPontosCellaPozicio(Ut ut, List<Sav> szakasz, Sav sav) {
        Point alap = aktualisPoziciok.get(ut.getNev());
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

        // Pozíciók frissítése az aktuális térkép alapján
        if (jatekter.getTerkep() != null) {
            frissitPoziciok(jatekter.getTerkep().getTeljesHalozat());
        }

        rajzolandoObjektumok.clear();
        for (Jarmu j : jatekter.getJarmuvek()) {
            if (j.getPozicio() == null || j.getPozicio().getUt() == null) continue;
            if (j instanceof Hokotro) rajzolandoObjektumok.add(new GraphicHokotro((Hokotro) j));
            else if (j instanceof Busz) rajzolandoObjektumok.add(new GraphicBusz((Busz) j));
            else if (j instanceof Auto) rajzolandoObjektumok.add(new GraphicAuto((Auto) j));
        }
    }

    /**
     * Ha minden útnak van statikus override-ja, azt használja.
     * Különben BFS-alapú automatikus rácsba rendezi az utakat.
     */
    private void frissitPoziciok(List<Ut> halozat) {
        boolean mindStatikus = halozat.stream().allMatch(u -> poziciok.containsKey(u.getNev()));
        if (mindStatikus) {
            aktualisPoziciok = new HashMap<>(poziciok);
            return;
        }

        final int CELL_X = 220;
        final int CELL_Y = 380;
        final int MARGIN = 50;

        Map<String, int[]> racs = new HashMap<>();
        Set<String> foglalt = new HashSet<>();
        Set<String> latogatott = new HashSet<>();
        Queue<Ut> sor = new LinkedList<>();
        int[][] iranyok = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};

        Ut gyoker = halozat.get(0);
        racs.put(gyoker.getNev(), new int[]{0, 0});
        foglalt.add("0,0");
        latogatott.add(gyoker.getNev());
        sor.add(gyoker);

        while (!sor.isEmpty()) {
            Ut aktualis = sor.poll();
            int[] pos = racs.get(aktualis.getNev());

            List<Ut> szomszedok = new ArrayList<>(aktualis.getSzomszedok(1));
            for (Ut s : aktualis.getSzomszedok(-1)) {
                if (!szomszedok.contains(s)) szomszedok.add(s);
            }

            for (Ut szomszed : szomszedok) {
                if (latogatott.contains(szomszed.getNev())) continue;
                for (int[] d : iranyok) {
                    int nx = pos[0] + d[0];
                    int ny = pos[1] + d[1];
                    String key = nx + "," + ny;
                    if (!foglalt.contains(key)) {
                        racs.put(szomszed.getNev(), new int[]{nx, ny});
                        foglalt.add(key);
                        latogatott.add(szomszed.getNev());
                        sor.add(szomszed);
                        break;
                    }
                }
            }
        }

        // Leválasztott (nem összefüggő) utak kezelése
        int fc = 0, fr = halozat.size() + 2;
        for (Ut u : halozat) {
            if (!racs.containsKey(u.getNev())) {
                while (foglalt.contains(fc + "," + fr)) fc++;
                racs.put(u.getNev(), new int[]{fc, fr});
                foglalt.add(fc + "," + fr);
                fc++;
            }
        }

        // Normalizálás (legkisebb col/row = 0)
        int minC = racs.values().stream().mapToInt(p -> p[0]).min().orElse(0);
        int minR = racs.values().stream().mapToInt(p -> p[1]).min().orElse(0);

        aktualisPoziciok = new HashMap<>();
        for (Map.Entry<String, int[]> e : racs.entrySet()) {
            int px = MARGIN + (e.getValue()[0] - minC) * CELL_X;
            int py = MARGIN + (e.getValue()[1] - minR) * CELL_Y;
            aktualisPoziciok.put(e.getKey(), new Point(px, py));
        }

        // Preferred size igazítása a térképhez
        int maxX = aktualisPoziciok.values().stream().mapToInt(p -> p.x).max().orElse(800) + 300;
        int maxY = aktualisPoziciok.values().stream().mapToInt(p -> p.y).max().orElse(600) + 300;
        setPreferredSize(new Dimension(Math.max(maxX, 800), Math.max(maxY, 600)));
        revalidate();
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
            Point p1 = aktualisPoziciok.get(forrasUt.getNev());
            if (p1 == null) continue;
            
            int szelesseg1 = (forrasUt.getPozSavokSzama() + forrasUt.getNegSavokSzama()) * SAV_SZELESSEG;
            int magassag1 = forrasUt.getHossz() * SZAKASZ_MAGASSAG;
            Point kozep1 = new Point(p1.x + szelesseg1 / 2, p1.y + magassag1 / 2);

            for (Ut celUt : forrasUt.getSzomszedok(1)) {
                Point p2 = aktualisPoziciok.get(celUt.getNev());
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
            Point p = aktualisPoziciok.get(ut.getNev());
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