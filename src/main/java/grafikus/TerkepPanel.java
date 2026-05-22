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
 * (hó, jég, zúzalék) és a járművek grafikus objektumait.
 */
public class TerkepPanel extends JPanel {

    /** A játéktér modell rétege, ahonnan az adatokat olvassa a panel. */
    private Jatekter jatekter;

    /** A térképen tartózkodó járművek grafikus csomagolóobjektumainak listája. */
    private List<GraphicObject> rajzolandoObjektumok;

    /** Télies háttérszín a pálya körüli tájhoz (nagyon halvány jégkék). */
    private final Color TAJ_S_ZIN = new Color(214, 234, 248);

    /**
     * A TerkepPanel konstruktora.
     * Beállítja a panel méreteit, hátterét és inicializálja a grafikus listát.
     *
     * @param jatekter A központi szimulációs játéktér referenciája.
     */
    public TerkepPanel(Jatekter jatekter) {
        this.jatekter = jatekter;
        this.rajzolandoObjektumok = new ArrayList<>();
        setBackground(TAJ_S_ZIN);
        setBorder(BorderFactory.createLineBorder(new Color(174, 214, 241), 3));
        
        // Első adatbetöltés végrehajtása
        jarmuGrafikusObjektumokFrissitese();
    }

    /**
     * A Swing beépített újrarajzoló metódusának felülbírálása.
     * Itt történik az utak, sávok és a járművek réteges kirajzolása.
     *
     * @param g A grafikus kontextus.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Aktiváljuk az élsimítást a szebb textúrákért és feliratokért
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. RÉTEG: Úthálózat és környezeti elemek kirajzolása
        utakKirajzolasa(g2d);

        // 2. RÉTEG: Járművek kirajzolása (hókotrók, buszok, autók)
        for (GraphicObject obj : rajzolandoObjektumok) {
            obj.rajzol(g2d);
        }
    }

    /**
     * Végigmegy a Játéktér útjain és sávjain, és kirajzolja őket az időjárási viszonyoknak megfelelően.
     */
    private void utakKirajzolasa(Graphics2D g) {
        if (jatekter == null || jatekter.getTerkep() == null) return;

        List<Ut> teljesHalozat = jatekter.getTerkep().getTeljesHalozat();
        int xOffset = 100;

        // Végigmegyünk a térkép összes útján
        for (int i = 0; i < teljesHalozat.size(); i++) {
            Ut ut = teljesHalozat.get(i);
            
            // Út alapjának megrajzolása (alapértelmezetten sötétszürke aszfalt)
            g.setColor(new Color(44, 62, 80));
            g.fillRect(xOffset, 50, 120, 300);

            // Út nevének és típusának kiírása az út fölé
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(ut.getNev(), xOffset + 10, 45);

            // Sávok bejárása az út első szakaszában a hóviszonyok érzékeltetéséhez
            if (!ut.getSzakaszok().isEmpty()) {
                List<Sav> savok = ut.getSzakaszok().get(0);
                for (int j = 0; j < savok.size(); j++) {
                    Sav sav = savok.get(j);
                    int yPos = 80 + (j * 35);

                    // Időjárási fizika vizualizációja: minél nagyobb a hó, annál fehérebb a sáv
                    int ho = sav.getHo();
                    if (ho > 0) {
                        // Hóréteg kirajzolása áttetsző fehér színnel (minél több a hó, annál tömöttebb)
                        int alfa = Math.min(255, ho * 8); 
                        g.setColor(new Color(255, 255, 255, alfa));
                        g.fillRect(xOffset + 2, yPos + 2, 116, 31);
                    }

                    // Jégpáncél jelölése (csillogó világoskék réteg)
                    if (sav.jegesE()) {
                        g.setColor(new Color(133, 193, 233, 130)); // 130-as áttetszőség
                        g.fillRect(xOffset + 2, yPos + 2, 116, 31);
                    }

                    // Zúzalék kő szórás jelölése (apró szürke keret/pöttyözés)
                    if (sav.isZuzalekos()) {
                        g.setColor(Color.LIGHT_GRAY);
                        g.drawRect(xOffset + 4, yPos + 4, 112, 27);
                    }

                    // Sávok közötti szaggatott elválasztóvonalak meghúzása
                    g.setColor(Color.WHITE);
                    Stroke regiStroke = g.getStroke();
                    g.setStroke(new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{9}, 0));
                    g.drawLine(xOffset, yPos, xOffset + 120, yPos);
                    g.getStroke();
                }
            }
            
            // Következő út eltolása jobbra a képernyőn
            xOffset += 150;
        }
    }

    /**
     * Pull-alapú architektúra szerint kiolvassa a modell járműveit, 
     * és becsomagolja őket a megfelelő GraphicObject osztályokba.
     */
    public void jarmuGrafikusObjektumokFrissitese() {
        if (jatekter == null) return;

        rajzolandoObjektumok.clear();
        List<Jarmu> modelJarmuvek = jatekter.getJarmuvek();

        // Típusellenőrzéssel legyártjuk a grafikus reprezentációkat
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

    /**
     * Kívülről hívható frissítő parancs. Újratölti a járművek listáját és kikényszeríti az ablak újrarajzolását.
     */
    public void frissit() {
        jarmuGrafikusObjektumokFrissitese();
        repaint(); // Swing újrarajzolás indítása
    }
}