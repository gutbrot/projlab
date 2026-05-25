package grafikus;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import jatekos.Jatekter;
import jarmu.*;
import terkep.*;

/**
 * A játéktér központi térképmegjelenítő panelje.
 * Pull-alapú adatolvasással kirajzolja az utakat, a sávok időjárási viszonyait 
 * (hó, jég, zúzalék) és a járművek grafikus objektumait egy összefüggő 2D-s úthálózatban.
 */
public class TerkepPanel extends JPanel {

    private Jatekter jatekter;
    private List<GraphicObject> rajzolandoObjektumok;
    private final Color TAJ_S_ZIN = new Color(214, 234, 248);

    public TerkepPanel(Jatekter jatekter) {
        this.jatekter = jatekter;
        this.rajzolandoObjektumok = new ArrayList<>();
        // Megnövelt méret a görgetősávnak, hogy a teljes 2D-s hálózat kényelmesen elférjen
        setPreferredSize(new Dimension(1000, 1450));
        setBackground(TAJ_S_ZIN);
    }

    /**
     * Statikus segédmetódus, amely visszaadja egy adott út alapértelmezett koordinátáit a képernyőn.
     * Intelligens szövegegyezést használ, így mindegy, hogy "EszakUt" vagy "EszakiUt" a név az XML-ben.
     */
    public static Point getUtAlapPozicio(String nev) {
        if (nev == null) return new Point(100, 80);
        String n = nev.toLowerCase();
        
        if (n.contains("kozpont"))   return new Point(400, 500); // Középen
        if (n.contains("eszak"))     return new Point(400, 50);  // Fent
        if (n.contains("kelet"))     return new Point(700, 500); // Jobbra
        if (n.contains("nyugat"))    return new Point(100, 500); // Balra
        if (n.contains("del"))       return new Point(400, 950); // Lent
        
        return new Point(100, 80); // Alapértelmezett fallback pozíció
    }

    public void jarmuGrafikusObjektumokFrissitese() {
        if (jatekter == null) return;

        rajzolandoObjektumok.clear();
        List<Jarmu> modelJarmuvek = jatekter.getJarmuvek();

        for (Jarmu j : modelJarmuvek) {
            if (j instanceof Hokotro) {
                rajzolandoObjektumok.add(new GraphicHokotro((Hokotro) j));
            } else if (j instanceof Busz) {
                rajzolandoObjektumok.add(new GraphicBusz((Busz) j));
            } else if (j instanceof Auto) {
                rajzolandoObjektumok.add(new GraphicAuto((Auto) j));
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if (jatekter == null || jatekter.getTerkep() == null) return;

        List<Ut> halozat = jatekter.getTerkep().getTeljesHalozat();

        for (Ut ut : halozat) {
            int szakaszSzam = ut.getHossz(); 
            int osszSav = ut.getPozSavokSzama() + ut.getNegSavokSzama();
            
            // Kiszámoljuk egy cella szélességét (X) és magasságát (Y)
            int szakaszMagassag = szakaszSzam > 0 ? 400 / szakaszSzam : 400;
            int savSzelesseg = osszSav > 0 ? 120 / osszSav : 120;

            // Lekérjük az aktuális út fix pozícióját a hálózatban
            Point alapPoz = getUtAlapPozicio(ut.getNev());

            // Út alap aszfaltjának megrajzolása (Fix 120x400-as téglalapok)
            g2d.setColor(new Color(189, 195, 199)); 
            g2d.fillRect(alapPoz.x, alapPoz.y, 120, 400);

            // Végigmegyünk a rácson és kirajzoljuk az időjárást / cellahatárokat
            List<List<Sav>> szakaszok = ut.getSzakaszok();
            if (szakaszok != null) {
                for (int i = 0; i < szakaszok.size(); i++) {
                    List<Sav> savok = szakaszok.get(i);
                    
                    for (int j = 0; j < savok.size(); j++) {
                        Sav sav = savok.get(j);
                        
                        int cellX = alapPoz.x + (j * savSzelesseg);
                        int cellY = alapPoz.y + (i * szakaszMagassag);

                        // 1. Hó réteg kirajzolása
                        if (sav.getHo() >= 30) {
                            g2d.setColor(Color.WHITE); // Áthatolhatatlan vastag hó
                            g2d.fillRect(cellX, cellY, savSzelesseg, szakaszMagassag);
                        } else if (sav.getHo() > 0) {
                            g2d.setColor(new Color(236, 240, 241, 180)); // Vékony hóréteg
                            g2d.fillRect(cellX, cellY, savSzelesseg, szakaszMagassag);
                        }

                        // 2. Jég réteg kirajzolása
                        if (sav.jegesE()) {
                            g2d.setColor(new Color(173, 216, 230, 150)); 
                            g2d.fillRect(cellX, cellY, savSzelesseg, szakaszMagassag);
                        }

                        // 3. Zúzalék réteg kirajzolása
                        if (sav.isZuzalekos()) {
                            g2d.setColor(new Color(139, 69, 19, 100)); 
                            g2d.fillRect(cellX, cellY, savSzelesseg, szakaszMagassag);
                        }

                        // 4. Cella keret rajzolása (Sávok és Szakaszok elválasztóvonalai)
                        g2d.setColor(Color.GRAY);
                        g2d.drawRect(cellX, cellY, savSzelesseg, szakaszMagassag);
                    }
                }
            }

            // Út nevének kiírása az aszfaltcsík fölé
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.drawString(ut.getNev(), alapPoz.x, alapPoz.y - 10);
        }

        // ÖSSZEKÖTŐ VONALAK: Kirajzoljuk a logikai kapcsolatokat a hálózati rajz szerint
        g2d.setColor(new Color(52, 152, 219)); // Esztétikus kék szín
        Stroke regiStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{6}, 0)); // Szaggatott vonal

        // Kereszt-irányú összeköttetések a Központtal
        g2d.drawLine(400 + 60, 50 + 400, 400 + 60, 500);   // Észak -> Központ
        g2d.drawLine(400 + 60, 500 + 400, 400 + 60, 950);  // Központ -> Dél
        g2d.drawLine(100 + 120, 500 + 200, 400, 500 + 200); // Nyugat -> Központ
        g2d.drawLine(400 + 120, 500 + 200, 700, 500 + 200); // Központ -> Kelet

        // Külső körgyűrű összeköttetései (Gyűrű hálózat szemléltetése)
        g2d.drawLine(400, 50 + 50, 100 + 60, 500);          // Észak -> Nyugat
        g2d.drawLine(400 + 120, 50 + 50, 700 + 60, 500);   // Észak -> Kelet
        g2d.drawLine(400, 950 + 350, 100 + 60, 500 + 400);  // Dél -> Nyugat
        g2d.drawLine(400 + 120, 950 + 350, 700 + 60, 500 + 400); // Dél -> Kelet

        g2d.setStroke(regiStroke); // Visszaállítjuk az eredeti vonalstílust

        // Járművek kirajzolása a cellák fölé
        for (GraphicObject go : rajzolandoObjektumok) {
            go.rajzol(g2d);
        }
    }
}